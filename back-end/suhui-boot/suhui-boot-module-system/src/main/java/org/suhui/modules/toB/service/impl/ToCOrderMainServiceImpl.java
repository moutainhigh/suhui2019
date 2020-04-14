package org.suhui.modules.toB.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.suhui.common.api.vo.Result;
import org.suhui.modules.toB.entity.OrderAssurer;
import org.suhui.modules.toB.entity.OrderAssurerAccount;
import org.suhui.modules.toB.entity.OrderAssurerMoneyChange;
import org.suhui.modules.toB.entity.ToCOrderMain;
import org.suhui.modules.toB.mapper.ToCOrderMainMapper;
import org.suhui.modules.toB.service.IToCOrderMainService;
import org.suhui.modules.utils.BaseUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 类说明：订单相关
 *
 * @author: 蔡珊珊
 * @create: 2020-04-12 17:16
 **/
@Service
public class ToCOrderMainServiceImpl extends ServiceImpl<ToCOrderMainMapper, ToCOrderMain> implements IToCOrderMainService {
    @Autowired
    PayCurrencyRateServiceImpl payCurrencyRateServiceImpl;
    @Autowired
    private PayIdentityChannelAccountServiceImpl iPayIdentityChannelAccountServiceImpl;
    @Autowired
    OrderAssurerServiceImpl orderAssurerServiceImpl;
    @Autowired
    OrderAssurerAccountServiceImpl orderAssurerAccountServiceImpl;
    @Autowired
    private OrderAssurerMoneyChangeServiceImpl orderAssurerMoneyChangeServiceImpl;
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 创建订单主方法
     *
     * @param
     * @return
     */
    @Override
    @Transactional
    public Result<Object> manageOrderByAuto(ToCOrderMain toCOrderMain) {
        Result<Object> result = new Result<Object>();
        JSONObject obj = new JSONObject();
        // 判断必填项是否有值
        String checkValue = toCOrderMain.checkCreateRequireValue();
        if (BaseUtil.Base_HasValue(checkValue)) {
            return Result.error(531, checkValue);
        }
        // 转换金额为最小单位
        toCOrderMain.changeMoneyToPoints();
        //通过源货币和目标货币获取汇率计算金额
        JSONObject rateObj = this.getUserPayMoney(toCOrderMain.getSourceCurrency(), toCOrderMain.getTargetCurrency(), toCOrderMain.getTargetCurrencyMoney().toString());
        if (BaseUtil.Base_HasValue(rateObj)) {
            toCOrderMain.setSourceCurrencyMoney(rateObj.getDouble("money"));
            toCOrderMain.setExchangeRate(rateObj.getDouble("rate"));
        }
        setAssurerCnyMoney(toCOrderMain);
        //查询用户收款账号
        toCOrderMain = this.getUserCollectionAccount(toCOrderMain);
        if (!BaseUtil.Base_HasValue(toCOrderMain)) {
            return Result.error(532, "获取用户收款账户失败");
        }
        // 为订单选择最优承兑商
        Map resutMap = orderAssurerServiceImpl.getAssurerByOrder(toCOrderMain);
        if (BaseUtil.Base_HasValue(resutMap) && resutMap.get("state").equals("success")) {
            dispatchAssurerToOrder(toCOrderMain, resutMap);
        } else {
            dispatchAssurerToOrder(toCOrderMain, resutMap);
        }
        toCOrderMain.changeMoneyToBig();
        result.setResult(toCOrderMain);
        result.success("订单创建成功");
        return result;
    }

    /**
     * 取消订单
     *
     * @param
     * @return
     */
    @Override
    public Result<Object> revokeOrderAdmin(String orderId) {
        ToCOrderMain toCOrderMain = getById(orderId);
        if (!BaseUtil.Base_HasValue(toCOrderMain)) {
            return Result.error(523, "订单不存在");
        }
        if (!toCOrderMain.getOrderState().equals("1") && !toCOrderMain.getOrderState().equals("2")) {
            return Result.error(524, "订单状态异常");
        }
        toCOrderMain.setOrderState("0");
        toCOrderMain.setUserCollectionTime(new Date());
        updateById(toCOrderMain);
        OrderAssurer orderAssurer = orderAssurerServiceImpl.getById(toCOrderMain.getAssurerId());
        OrderAssurerAccount oaac = orderAssurerAccountServiceImpl.getById(toCOrderMain.getAssurerCollectionAccountId());
        OrderAssurerAccount oaap = orderAssurerAccountServiceImpl.getById(toCOrderMain.getAssurerPayAccountId());
        if (!BaseUtil.Base_HasValue(orderAssurer)) {
            return Result.error(525, "承兑商不存在");
        }
        if (!BaseUtil.Base_HasValue(oaac)) {
            return Result.error(526, "承兑商收款账户不存在");
        }
        if (!BaseUtil.Base_HasValue(oaap)) {
            return Result.error(527, "承兑商支付账户不存在");
        }
        Double orderMoney = toCOrderMain.getAssurerCnyMoney();

        // 更新可用金额
        Double canUseLimit = orderAssurer.getCanUseLimit() + orderMoney;

        // 更新锁定金额
        Double payLockMoney = orderAssurer.getPayLockMoney() - orderMoney;
        if (canUseLimit + orderAssurer.getUsedLimit() + payLockMoney != orderAssurer.getTotalLimit()) {
            return Result.error(528, "承兑商金额异常(已用金额+可用金额+锁定金额不等于总金额)");
        }
        orderAssurer.setCanUseLimit(canUseLimit);
        orderAssurer.setPayLockMoney(payLockMoney);
        orderAssurerServiceImpl.updateById(orderAssurer);
        // 目前支付宝账户才进行锁定金额
        if ("alipay".equals(oaap.getAccountType())) {
            // 更新账户已使用金额 = 已用金额+该订单金额
            Double canUseLimitAccount = oaap.getPayCanUseLimit() + orderMoney;
            // 更新账户锁定金额
            Double payLockMoneyAccount = oaap.getPayLockMoney() - orderMoney;
            if (canUseLimitAccount + oaap.getPayUsedLimit() + oaap.getPayLockMoney() != oaap.getPayLimit()) {
                return Result.error(529, "承兑商账户金额异常(已用金额+可用金额+锁定金额不等于总金额)");
            }
            oaap.setPayCanUseLimit(canUseLimitAccount);
            oaap.setPayLockMoney(payLockMoneyAccount);
        }
        // 收款账户已用金额增加
        if (!oaac.getId().equals(oaap.getId())) {
            orderAssurerAccountServiceImpl.updateById(oaac);
        }
        orderAssurerAccountServiceImpl.updateById(oaap);
        return Result.ok("操作成功");
    }


    /**
     * 用户确认支付
     */
    @Override
    public Result<Object> userPayConfirm(String orderId, String voucher) {
        ToCOrderMain toCOrderMain = getById(orderId);
        if (!BaseUtil.Base_HasValue(toCOrderMain)) {
            return Result.error(523, "订单不存在");
        }
        if (!toCOrderMain.getOrderState().equals("2")) {
            return Result.error(524, "订单状态异常");
        }
        toCOrderMain.setOrderState("3");
        toCOrderMain.setUserPayTime(new Date());
        toCOrderMain.setUserPayVoucher(voucher);
        updateById(toCOrderMain);
        return Result.ok("操作成功");
    }

    /**
     * 承兑商确认已收款
     */
    @Override
    public Result<Object> assurerCollectionConfirm(String orderIds) {
        String[] idArr = orderIds.split(",");
        Result<Object> result = new Result<>();
        boolean check = true;
        for (String orderId : idArr) {
            ToCOrderMain toCOrderMain = getById(orderId);
            if (!BaseUtil.Base_HasValue(toCOrderMain)) {
                result = Result.error(513, "订单不存在");
                check = false;
                break;
            }
            if (!toCOrderMain.getOrderState().equals("3")) {
                result = Result.error(514, "【待承兑商收款】状态的订单可执行该操作");
                check = false;
                break;
            }
            toCOrderMain.setOrderState("4");
            toCOrderMain.setAssurerCollectionTime(new Date());
            updateById(toCOrderMain);
        }
        if (!check) {
            return result;
        }
        return Result.ok("操作成功");
    }

    /**
     * 承兑商确认已兑付
     */
    @Override
    public Result<Object> assurerPayConfirm(String orderId, String fileList) {
        Result<Object> result = new Result<>();

        ToCOrderMain toCOrderMain = getById(orderId);
        if (!BaseUtil.Base_HasValue(toCOrderMain)) {
            return Result.error(513, "订单不存在");
        }
        if (!toCOrderMain.getOrderState().equals("4")) {
            return Result.error(514, "【待承兑商兑付】状态的订单可执行该操作");
        }
        toCOrderMain.setOrderState("5");
        toCOrderMain.setAssurerPayVoucher(fileList);
        toCOrderMain.setAssurerPayTime(new Date());
        updateById(toCOrderMain);
        return Result.ok("操作成功");
    }

    /**
     * 用户确认已收款-订单完成
     */
    @Override
    public Result<Object> userCollectionConfirm(String orderId) {
        ToCOrderMain toCOrderMain = getById(orderId);
        if (!BaseUtil.Base_HasValue(toCOrderMain)) {
            return Result.error(523, "订单不存在");
        }
        if (!toCOrderMain.getOrderState().equals("5")) {
            return Result.error(524, "订单状态异常");
        }
        toCOrderMain.setOrderState("6");
        toCOrderMain.setUserCollectionTime(new Date());
        updateById(toCOrderMain);
        return orderFinishChangeAussurerMoney(toCOrderMain);
    }

    //<editor-fold desc="辅助方法">

    /**
     * 计算承兑商需支付的人民币金额,扣减承兑商额度需要
     */
    public void setAssurerCnyMoney(ToCOrderMain toCOrderMain) {
        if (!toCOrderMain.getTargetCurrency().equals("CNY")) {
            if (!toCOrderMain.getSourceCurrency().equals("CNY")) {
                JSONObject rateObj = this.getUserPayMoney("CNY", toCOrderMain.getTargetCurrency(), toCOrderMain.getTargetCurrencyMoney().toString());
                if (BaseUtil.Base_HasValue(rateObj)) {
                    toCOrderMain.setAssurerCnyMoney(rateObj.getDouble("money"));
                }
            } else {
                toCOrderMain.setAssurerCnyMoney(toCOrderMain.getSourceCurrencyMoney());
            }
        } else {
            toCOrderMain.setAssurerCnyMoney(toCOrderMain.getTargetCurrencyMoney());
        }
    }

    /**
     * 获取用户收款账号
     */
    ToCOrderMain getUserCollectionAccount(ToCOrderMain toCOrderMain) {
        Map map = new HashMap();
        map.put("userno", toCOrderMain.getUserNo());
        map.put("usertype", 0);
        Map keyMap = new HashMap();
        keyMap.put("CNY", "+86");
        keyMap.put("KRW", "+82");
        keyMap.put("USD", "+1");
        if (!BaseUtil.Base_HasValue(keyMap.get(toCOrderMain.getTargetCurrency()))) {
            return null;
        }
        String collectionArea = keyMap.get(toCOrderMain.getTargetCurrency()).toString();
        // 查询用户支付通道
        List<Map> mapDb = iPayIdentityChannelAccountServiceImpl.getChannelAccountInfoByUserNo(map);
        if (BaseUtil.Base_HasValue(mapDb)) {
            Map payAccountMap = new HashMap();
            for (int i = 0; i < mapDb.size(); i++) {
                Map map1 = mapDb.get(i);
                if (map1.get("areacode").toString().equals(collectionArea)) {
                    payAccountMap = map1;
                }
            }
            if (!BaseUtil.Base_HasValue(payAccountMap)) {
                return null;
            }
            Integer type = Integer.parseInt(payAccountMap.get("channel_type").toString());
            toCOrderMain.setUserCollectionAreaCode(payAccountMap.get("areacode").toString());
            if (type > 100) {
                toCOrderMain.setUserCollectionMethod("bank_card");
                // 获取账号开户行
                RestTemplate restTemplate = new RestTemplate();
                String url = "http://localhost:3333/api/login/ChannelTypeCode/getList";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//                headers.add("X-Access-Token", token);
                MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, headers);
                ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, request, JSONObject.class);
                if (response.getBody().getInteger("code") == 200) {
                    JSONObject result = response.getBody().getJSONObject("result");
                    JSONArray dataArr = result.getJSONArray("channelList");
                    if (BaseUtil.Base_HasValue(dataArr)) {
                        for (int i = 0; i < dataArr.size(); i++) {
                            JSONObject jsonObject = JSONObject.parseObject(dataArr.get(i).toString());
                            if (jsonObject.getInteger("channelType").equals(type)) {
                                toCOrderMain.setUserCollectionBank(jsonObject.getString("channelNameLong"));
                                toCOrderMain.setUserCollectionBankBranch("");
                            }
                        }
                    }
                }
            } else if (type < 100) {
                toCOrderMain.setUserCollectionMethod("alipay");
            }
            toCOrderMain.setUserCollectionAccount(payAccountMap.get("channel_account_no").toString());
            toCOrderMain.setUserCollectionAccountUser(payAccountMap.get("channel_account_name").toString());
        }
        return toCOrderMain;
    }

    /**
     * 给订单分配一个承兑商和账户
     */
    public void dispatchAssurerToOrder(ToCOrderMain toCOrderMain, Map resutMap) {
        OrderAssurer orderAssurer = (OrderAssurer) resutMap.get("orderAssurer");
        OrderAssurerAccount pay = (OrderAssurerAccount) resutMap.get("orderAssurerAccountPay");
        OrderAssurerAccount collection = (OrderAssurerAccount) resutMap.get("orderAssurerAccountCollection");
        if (BaseUtil.Base_HasValue(orderAssurer) && BaseUtil.Base_HasValue(pay) && BaseUtil.Base_HasValue(collection)) {
            toCOrderMain.setAssurerId(orderAssurer.getId());
            toCOrderMain.setAssurerName(orderAssurer.getAssurerName());
            toCOrderMain.setAssurerCollectionAccount(collection.getAccountNo());
            toCOrderMain.setAssurerCollectionAccountId(collection.getId());
            toCOrderMain.setAssurerCollectionMethod(collection.getAccountType());
            toCOrderMain.setAssurerCollectionBank(collection.getOpenBank());
            toCOrderMain.setAssurerCollectionBankBranch(collection.getOpenBankBranch());
            toCOrderMain.setAssurerPayAccountId(pay.getId());
            toCOrderMain.setAssurerPayAccount(pay.getAccountNo());
            toCOrderMain.setAssurerPayMethod(pay.getAccountType());
            toCOrderMain.setAssurerPayBank(pay.getOpenBank());
            toCOrderMain.setAssurerPayBankBranch(pay.getOpenBankBranch());
            toCOrderMain.setOrderState("2");
            toCOrderMain.setAssurerCollectionAccountUser(collection.getRealName());
            toCOrderMain.setAssurerPayAccountUser(pay.getRealName());
            // 锁定承兑商金额
            lockAssurerMoney(toCOrderMain.getAssurerCnyMoney(), orderAssurer);
            // 锁定承兑账户金额
            lockAssurerAccountMoney(toCOrderMain.getAssurerCnyMoney(), pay);
            // 减少承兑商租赁金
            subAssurerLeaseMoney(toCOrderMain.getAssurerCnyMoney(), orderAssurer, toCOrderMain);
        } else {
            toCOrderMain.setOrderState("1");
            toCOrderMain.setAutoDispatchState(1);
            toCOrderMain.setAutoDispatchText(resutMap.get("message").toString());
        }
        if (!BaseUtil.Base_HasValue(toCOrderMain.getId())) {
            toCOrderMain.setOrderCode(getOrderNoByUUID());
            save(toCOrderMain);
        } else {
            updateById(toCOrderMain);
        }
    }

    /**
     * 锁定承运商支付金额
     */
    public void lockAssurerMoney(Double money, OrderAssurer orderAssurer) {
        Double lockMoney = orderAssurer.getPayLockMoney() + money;
        Double canUseLimit = orderAssurer.getCanUseLimit() - money;
        orderAssurer.setCanUseLimit(canUseLimit);
        orderAssurer.setPayLockMoney(lockMoney);
        orderAssurerServiceImpl.updateById(orderAssurer);
    }

    /**
     * 锁定承运商账号支付金额
     */
    public void lockAssurerAccountMoney(Double money, OrderAssurerAccount orderAssurerAccount) {
        if (orderAssurerAccount.getAccountType().equals("alipay")) {
            Double lockMoney = orderAssurerAccount.getPayLockMoney() + money;
            Double canUseLimit = orderAssurerAccount.getPayCanUseLimit() - money;
            orderAssurerAccount.setPayCanUseLimit(canUseLimit);
            orderAssurerAccount.setPayLockMoney(lockMoney);
            orderAssurerAccountServiceImpl.updateById(orderAssurerAccount);
        }
    }

    /**
     * 减少承兑商租赁金
     */
    public void subAssurerLeaseMoney(Double money, OrderAssurer orderAssurer, ToCOrderMain toCOrderMain) {
        Double leaseMoney = BaseUtil.mul(money, orderAssurer.getAssurerRate(), 0);
        OrderAssurerMoneyChange orderAssurerMoneyChange = new OrderAssurerMoneyChange();
        orderAssurerMoneyChange.setAssurerId(orderAssurer.getId());
        orderAssurerMoneyChange.setAssurerName(orderAssurer.getAssurerName());
        orderAssurerMoneyChange.setAssurerPhone(orderAssurer.getAssurerPhone());
        orderAssurerMoneyChange.setChangeClass("lease");
        orderAssurerMoneyChange.setChangeType("sub");
        orderAssurerMoneyChange.setChangeMoney(leaseMoney);
        orderAssurerMoneyChange.setChangeText("订单扣减");
        orderAssurerMoneyChange.setOrderId(toCOrderMain.getId());
        orderAssurerMoneyChange.setOrderNo(toCOrderMain.getOrderCode());
        orderAssurerMoneyChangeServiceImpl.save(orderAssurerMoneyChange);
    }

    /**
     * 获取应支付金额（根据目标货币&金额、源货币）
     *
     */
    public JSONObject getUserPayMoney(String source, String target, String money) {
        JSONObject valueObj = new JSONObject();
        try {
            //1.获取汇率
            Map<String, String> map = new HashMap<>();
            map.put("source_currency_code", source);
            map.put("target_currency_code", target);
            Result<JSONObject> rateResult = payCurrencyRateServiceImpl.getCurrencyRateValue(map);
            if (rateResult.getCode() == 0) {
                JSONObject result = rateResult.getResult();
                JSONObject data = result.getJSONObject("data");
                String rate = data.getString("rate_now");
                BigDecimal a1 = new BigDecimal(money);
                BigDecimal b1 = new BigDecimal(rate);
                Double value = a1.divide(b1, 0, BigDecimal.ROUND_UP).doubleValue();
                valueObj.put("money", value);
                valueObj.put("rate", rate);
            }
        } catch (Exception e) {
            return null;
        }
        return valueObj;
    }

    /**
     * 订单完成-释放承兑商锁定金额
     */
    public Result<Object> orderFinishChangeAussurerMoney(ToCOrderMain toCOrderMain) {
        OrderAssurer orderAssurer = orderAssurerServiceImpl.getById(toCOrderMain.getAssurerId());
        OrderAssurerAccount oaac = orderAssurerAccountServiceImpl.getById(toCOrderMain.getAssurerCollectionAccountId());
        OrderAssurerAccount oaap = orderAssurerAccountServiceImpl.getById(toCOrderMain.getAssurerPayAccountId());
        if (!BaseUtil.Base_HasValue(orderAssurer)) {
            return Result.error(525, "承兑商不存在");
        }
        if (!BaseUtil.Base_HasValue(oaac)) {
            return Result.error(526, "承兑商收款账户不存在");
        }
        if (!BaseUtil.Base_HasValue(oaap)) {
            return Result.error(527, "承兑商支付账户不存在");
        }
        Double orderMoney = toCOrderMain.getAssurerCnyMoney();
        // 更新已使用金额 = 已用金额+该订单金额
        Double userdLimit = orderAssurer.getUsedLimit() + orderMoney;
        // 更新锁定金额
        Double payLockMoney = orderAssurer.getPayLockMoney() - orderMoney;
        if (userdLimit + orderAssurer.getCanUseLimit() + payLockMoney != orderAssurer.getTotalLimit()) {
            return Result.error(528, "承兑商金额异常(已用金额+可用金额+锁定金额不等于总金额)");
        }
        orderAssurer.setUsedLimit(userdLimit);
        orderAssurer.setPayLockMoney(payLockMoney);
        orderAssurerServiceImpl.updateById(orderAssurer);
        // 目前支付宝账户才进行锁定金额
        if ("alipay".equals(oaap.getAccountType())) {
            // 更新账户已使用金额 = 已用金额+该订单金额
            Double usedLimitAccount = oaap.getPayUsedLimit() + orderMoney;
            // 更新账户锁定金额
            Double payLockMoneyAccount = oaap.getPayLockMoney() - orderMoney;
            if (usedLimitAccount + oaap.getPayCanUseLimit() + oaap.getPayLockMoney() != oaap.getPayLimit()) {
                return Result.error(529, "承兑商账户金额异常(已用金额+可用金额+锁定金额不等于总金额)");
            }
            oaap.setPayLockMoney(payLockMoneyAccount);
            oaap.setPayUsedLimit(usedLimitAccount);
        }
        // 收款账户已用金额增加
        if (oaac.getId().equals(oaap.getId())) {
            oaap.setCollectionUsedLimit(oaac.getCollectionUsedLimit() + orderMoney);
        } else {
            oaac.setCollectionUsedLimit(oaac.getCollectionUsedLimit() + orderMoney);
            orderAssurerAccountServiceImpl.updateById(oaac);
        }
        orderAssurerAccountServiceImpl.updateById(oaap);
        return Result.ok("操作成功");
    }

    public static synchronized String getOrderNoByUUID() {
        Integer uuidHashCode = UUID.randomUUID().toString().hashCode();
        if (uuidHashCode < 0) {
            uuidHashCode = uuidHashCode * (-1);
        }
        String date = simpleDateFormat.format(new Date());
        return date + uuidHashCode;
    }

    //</editor-fold>
}
