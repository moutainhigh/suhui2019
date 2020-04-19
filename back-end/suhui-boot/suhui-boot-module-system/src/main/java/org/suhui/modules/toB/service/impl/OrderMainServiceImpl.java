package org.suhui.modules.toB.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.suhui.common.api.vo.Result;
import org.suhui.modules.toB.entity.*;
import org.suhui.modules.toB.mapper.OrderMainMapper;
import org.suhui.modules.toB.mapper.OrderMerchantAccountMapper;
import org.suhui.modules.toB.mapper.OrderMerchantMapper;
import org.suhui.modules.toB.mapper.OrderPlatformAccountMapper;
import org.suhui.modules.toB.service.IOrderMainService;
import org.suhui.modules.utils.BaseUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.suhui.modules.toB.service.impl.ToCOrderMainServiceImpl.getOrderNoByUUID;

/**
 * 类说明：订单相关
 *
 * @author: 蔡珊珊
 * @create: 2020-04-14 13:59
 */
@Slf4j
@Service
public class OrderMainServiceImpl extends ServiceImpl<OrderMainMapper, OrderMain> implements IOrderMainService {
    @Autowired
    PayCurrencyRateServiceImpl payCurrencyRateServiceImpl;
    @Autowired
    OrderPlatformAccountServiceImpl orderPlatformAccountServiceImpl;
    @Autowired
    OrderPlatformAccountMapper orderPlatformAccountMapper;
    @Autowired
    OrderMerchantMapper orderMerchantMapper;
    @Autowired
    OrderMerchantAccountMapper orderMerchantAccountMapper;
    @Autowired
    OrderMerchantAccountServiceImpl orderMerchantAccountServiceImpl;
    @Autowired
    OrderAssurerServiceImpl orderAssurerServiceImpl;
    @Autowired
    OrderAssurerAccountServiceImpl orderAssurerAccountServiceImpl;
    @Autowired
    private OrderAssurerMoneyChangeServiceImpl orderAssurerMoneyChangeServiceImpl;
    @Autowired
    private OrderMainMapper orderMainMapper;


    /**
     * 创建收款订单
     *
     * @param
     * @return
     */
    @Override
    @Transactional
    public Result<Object> createPaymentOrder(OrderMain orderMain, String userNo) {
        //// 判断必填项是否有值
        //String checkValue = orderMain.checkPaymentOrderRequireValue();
        //if (!BaseUtil.Base_HasValue(userNo)) {
        //    return Result.error(531, "缺少值商户编号");
        //}
        //if (BaseUtil.Base_HasValue(checkValue)) {
        //    return Result.error(531, checkValue);
        //}
        OrderMerchant orderMerchant = orderMerchantMapper.getMerchantByUserNo(userNo);
        if (!BaseUtil.Base_HasValue(orderMerchant)) {
            return Result.error(601, "商户不存在");
        }
        orderMain.setMerchantId(orderMerchant.getId());
        orderMain.setMerchantName(orderMerchant.getMerchantName());

        //根据源货币获取区域代码
        String sourceAreaCode = getAreaCodeByCurrency(orderMain.getSourceCurrency());
        if (!BaseUtil.Base_HasValue(sourceAreaCode)) {
            return Result.error(603, "没有合适的区域代码");
        }
        //根据区域代码和商户Id获取商户的账户
        OrderMerchantAccount orderMerchantAccount = orderMerchantAccountMapper.getAccountByMerchantId(orderMain.getMerchantId(), sourceAreaCode);
        if (!BaseUtil.Base_HasValue(orderMerchantAccount)) {
            return Result.error(602, "商户付款账户不存在");
        }
        //绑定商户账户Id 关联tob_order_merchant(为了方便在订单完成的变更账户金额)
        orderMain.setMerchantAccountId(orderMerchantAccount.getId());

        //1.转换金额为最小单位
        orderMain.changeMoneyToPoints();
        //2.通过源货币和目标货币获取汇率和计算金额
        JSONObject rateObj = this.getUserPayMoney(orderMain.getSourceCurrency(), orderMain.getTargetCurrency(), orderMain.getTargetCurrencyMoney().toString());
        if (!BaseUtil.Base_HasValue(rateObj)) {
            return Result.error(515, "获取汇率失败");
        }
        if (BaseUtil.Base_HasValue(rateObj)) {
            orderMain.setSourceCurrencyMoney(rateObj.getDouble("money"));
            orderMain.setExchangeRate(rateObj.getDouble("rate"));
        }
        //<editor-fold desc="3.根据区域代码，获取平台支付账号">

        //根据目标货币获取区域代码
        String targetAreaCode = getAreaCodeByCurrency(orderMain.getTargetCurrency());
        if (!BaseUtil.Base_HasValue(targetAreaCode)) {
            return Result.error(603, "没有合适的区域代码");
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("areaCode", targetAreaCode);
        List<OrderPlatformAccount> platformAccountList = orderPlatformAccountServiceImpl.getAccountListByAreaCode(paramMap);
        if (!BaseUtil.Base_HasValue(platformAccountList)) {
            return Result.error(532, "获取平台收款账户失败");
        }
        OrderPlatformAccount platformAccount = platformAccountList.get(0);
        //</editor-fold>

        //保存收款订单
        return savePaymentOrder(orderMain, platformAccount);
    }

    /**
     * 平台确认已收款
     */
    @Override
    @Transactional
    public Result<Object> confirmPaymentOrder(String orderNos) {
        String[] idArr = orderNos.split(",");
        Result<Object> result = new Result<>();
        boolean check = true;
        for (String orderNo : idArr) {
            OrderMain orderMain = orderMainMapper.queryByOrderNo(orderNo);
            if (!BaseUtil.Base_HasValue(orderMain)) {
                result = Result.error(513, "订单不存在");
                check = false;
                break;
            }
            if (!orderMain.getOrderState().equals("2")) {
                result = Result.error(524, "订单状态异常");
                check = false;
                break;
            }
            orderMain.setOrderFinishTime(new Date());
            orderMain.setOrderState("3");// 确认收款
            updateById(orderMain);
            //账户金额变动
            Result<Object> finishResult = orderPaymentFinish(orderMain);
            if (!finishResult.isSuccess()) {
                check = false;
                result = finishResult;
                break;
            }

            //<editor-fold desc="回调notify_url">
            if (!BaseUtil.Base_HasValue(orderMain.getNotifyUrl())) {
                check = false;
                result = Result.error(608, "回调notify_url失败");
                break;
            }
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("orderNo", orderMain.getOrderCode());
            paramMap.add("orderState", orderMain.getOrderState());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, headers);
            log.info("发起确认收款通知（" + orderMain.getOrderCode() + "）：NotifyUrl为" + orderMain.getNotifyUrl() + " paramMap为" + JSON.toJSONString(paramMap));
            ResponseEntity<String> response = restTemplate.postForEntity(orderMain.getNotifyUrl(), request, String.class);
            log.info("发起确认收款通知结果（" + orderMain.getOrderCode() + "）：" + JSON.toJSONString(response));
            if (!response.getBody().equals("success")) {
                check = false;
                result = Result.error(608, "回调notify_url失败");
                break;
            }
            //</editor-fold>
        }
        if (!check) {
            return result;
        }

        return Result.ok("操作成功");
    }

    /**
     * 创建提款订单
     *
     * @param
     * @return
     */
    @Override
    @Transactional
    public Result<Object> createWithdrawalOrder(OrderMain orderMain, String userNo) {
        //// 判断必填项是否有值
        //String checkValue = orderMain.checkWithdrawalOrderRequireValue();
        //if (!BaseUtil.Base_HasValue(userNo)) {
        //    return Result.error(531, "缺少值商户编号");
        //}
        //if (BaseUtil.Base_HasValue(checkValue)) {
        //    return Result.error(531, checkValue);
        //}
        OrderMerchant orderMerchant = orderMerchantMapper.getMerchantByUserNo(userNo);
        if (!BaseUtil.Base_HasValue(orderMerchant)) {
            return Result.error(601, "商户不存在");
        }
        orderMain.setMerchantId(orderMerchant.getId());
        orderMain.setMerchantName(orderMerchant.getMerchantName());

        //根据源货币获取区域代码
        String sourceAreaCode = getAreaCodeByCurrency(orderMain.getSourceCurrency());
        if (!BaseUtil.Base_HasValue(sourceAreaCode)) {
            return Result.error(603, "没有合适的区域代码");
        }
        //赋值商户收款账户区域编码
        orderMain.setMerchantCollectionAreaCode(sourceAreaCode);
        //根据区域代码和商户Id获取商户的账户
        OrderMerchantAccount orderMerchantAccount = orderMerchantAccountMapper.getAccountByMerchantId(orderMain.getMerchantId(), sourceAreaCode);
        if (!BaseUtil.Base_HasValue(orderMerchantAccount)) {
            return Result.error(602, "商户收款账户不存在");
        }
        if (!orderMerchantAccount.getAccountNo().equals(orderMain.getMerchantCollectionAccount())) {
            return Result.error(604, "商户收款账户不一致");
        }
        //绑定商户账户Id 关联tob_order_merchant(为了方便在订单完成的变更账户金额)
        orderMain.setMerchantAccountId(orderMerchantAccount.getId());
        orderMain.setMerchantCollectionBank(orderMerchantAccount.getOpenBank());
        orderMain.setMerchantCollectionBankBranch(orderMerchantAccount.getOpenBankBranch());

        //1.转换金额为最小单位
        orderMain.changeMoneyToPoints();
        //2.通过源货币和目标货币获取汇率和计算金额
        JSONObject rateObj = this.getUserPayMoney(orderMain.getSourceCurrency(), orderMain.getTargetCurrency(), orderMain.getTargetCurrencyMoney().toString());
        if (!BaseUtil.Base_HasValue(rateObj)) {
            return Result.error(515, "获取汇率失败");
        }
        if (BaseUtil.Base_HasValue(rateObj)) {
            orderMain.setSourceCurrencyMoney(rateObj.getDouble("money"));
            orderMain.setExchangeRate(rateObj.getDouble("rate"));
        }
        //计算承兑商需支付的人民币金额
        setAssurerCnyMoney(orderMain);

        //根据目标货币获取区域代码
        String targetAreaCode = getAreaCodeByCurrency(orderMain.getTargetCurrency());
        if (!BaseUtil.Base_HasValue(targetAreaCode)) {
            return Result.error(603, "没有合适的区域代码");
        }

        // 为订单选择最优承兑商
        Map resutMap = orderAssurerServiceImpl.getAssurerByOrder(orderMain, targetAreaCode);
        if (BaseUtil.Base_HasValue(resutMap) && resutMap.get("state").equals("success")) {
            dispatchAssurerToOrder(orderMain, resutMap);
        } else {
            return Result.error(605, resutMap.get("message").toString());
        }
        //try {
        //
        //} catch (Exception ex) {
        //    log.error(String.format("异常：创建提款订单>>>给订单分配一个承兑商和账户，orderMain参数：{0}，异常信息：{1}", JSON.toJSONString(orderMain), ex + ""));
        //}
        Result<Object> result = new Result<Object>();
        JSONObject data = new JSONObject();
        //<editor-fold desc="接口返回信息">
        orderMain.changeMoneyToBig();

        data.put("order_no", orderMain.getOrderCode());
        data.put("order_state", orderMain.getOrderState());
        data.put("merchant_id", orderMain.getMerchantId());
        data.put("merchant_name", orderMain.getMerchantName());
        data.put("merchant_contact", orderMain.getMerchantContact());
        data.put("source_currency", orderMain.getSourceCurrency());
        data.put("target_currency", orderMain.getTargetCurrency());
        data.put("source_currency_money", orderMain.getSourceCurrencyMoney());
        data.put("target_currency_money", orderMain.getTargetCurrencyMoney());
        data.put("exchange_rate", orderMain.getExchangeRate());
        data.put("collectionMethod", orderMain.getMerchantCollectionMethod());
        data.put("collectionAccount", orderMain.getMerchantCollectionAccount());
        data.put("collectionAccountUser", orderMain.getMerchantCollectionAccountUser());
        //</editor-fold>
        result.setResult(data);
        result.success("订单创建成功");
        return result;
    }

    /**
     * 承兑商确认已处理
     */
    @Override
    @Transactional
    public Result<Object> confirmWithdrawalOrder(String orderNos) {
        String[] idArr = orderNos.split(",");
        Result<Object> result = new Result<>();
        boolean check = true;
        for (String orderNo : idArr) {
            OrderMain orderMain = orderMainMapper.queryByOrderNo(orderNo);
            if (!BaseUtil.Base_HasValue(orderMain)) {
                result = Result.error(513, "订单不存在");
                check = false;
                break;
            }
            if (!orderMain.getOrderState().equals("2")) {
                result = Result.error(524, "订单状态异常");
                check = false;
                break;
            }
            orderMain.setOrderFinishTime(new Date());
            orderMain.setOrderState("3");// 确认已处理

            updateById(orderMain);
            //账户金额变动
            Result<Object> finishResult = orderWithdrawalFinish(orderMain);
            if (!finishResult.isSuccess()) {
                check = false;
                result = finishResult;
            }

            //<editor-fold desc="回调notify_url">
            if (!BaseUtil.Base_HasValue(orderMain.getNotifyUrl())) {
                check = false;
                result = Result.error(608, "回调notify_url失败");
                break;
            }
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("orderNo", orderMain.getOrderCode());
            paramMap.add("orderState", orderMain.getOrderState());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramMap, headers);
            log.info("发起提款处理通知（" + orderMain.getOrderCode() + "）：NotifyUrl为" + orderMain.getNotifyUrl() + " paramMap为" + JSON.toJSONString(paramMap));
            ResponseEntity<String> response = restTemplate.postForEntity(orderMain.getNotifyUrl(), request, String.class);
            log.info("发起提款处理通知结果（" + orderMain.getOrderCode() + "）：" + JSON.toJSONString(response));
            if (!response.getBody().equals("success")) {
                check = false;
                result = Result.error(608, "回调notify_url失败");
                break;
            }
            //</editor-fold>
        }
        if (!check) {
            return result;
        }

        return Result.ok("操作成功");
    }


    //<editor-fold desc="辅助方法">

    /**
     * 收款确认（变更平台账户、商户账户）
     */
    public Result<Object> orderPaymentFinish(OrderMain orderMain) {
        OrderPlatformAccount orderPlatformAccount = orderPlatformAccountServiceImpl.getById(orderMain.getPlatformCollectionAccountId());
        OrderMerchantAccount orderMerchantAccount = orderMerchantAccountServiceImpl.getById(orderMain.getMerchantAccountId());
        if (!BaseUtil.Base_HasValue(orderPlatformAccount)) {
            return Result.error(606, "平台账户不存在");
        }
        if (!BaseUtil.Base_HasValue(orderMerchantAccount)) {
            return Result.error(607, "商户支付账户不存在");
        }
        Double targetOrderMoney = orderMain.getTargetCurrencyMoney();
        Double sourceOrderMoney = orderMain.getSourceCurrencyMoney();

        //<editor-fold desc="1.平台账户变更内容（仅更新账户金额）">
        Double accountMoney = orderPlatformAccount.getAccountMoney() + targetOrderMoney;
        orderPlatformAccount.setAccountMoney(accountMoney);
        orderPlatformAccountMapper.updateById(orderPlatformAccount);
        //</editor-fold>

        //<editor-fold desc="2.商户账户变更内容（仅更新已使用金额）">
        // 支付已用金额pay_used_limit
        // 支付锁定金额pay_lock_money（仅支付宝）
        // 支付可用额度(日)pay_can_use_limit（不用管）
        // 每日支付限额pay_limit（不用管）
        // 收款已用金额 = 已用金额+该订单金额
        Double collectionUsedLimit = orderMerchantAccount.getCollectionUsedLimit() + sourceOrderMoney;

        //<editor-fold desc="这里暂时不加锁定金额">
        //if ("alipay".equals(orderMerchantAccount.getAccountType())) {
        //    // 目前支付宝账户才进行锁定金额
        //    Double payLockMoneyAccount = orderMerchantAccount.getPayLockMoney() - orderMoney;
        //    if (usedLimitAccount + orderMerchantAccount.getPayCanUseLimit() + orderMerchantAccount.getPayLockMoney() != orderMerchantAccount.getPayLimit()) {
        //        return Result.error(529, "商户账户金额异常(已用金额+可用金额+锁定金额不等于总金额)");
        //    }
        //    orderMerchantAccount.setPayLockMoney(payLockMoneyAccount);
        //}
        //</editor-fold>
        orderMerchantAccount.setCollectionUsedLimit(collectionUsedLimit);
        orderMerchantAccountServiceImpl.updateById(orderMerchantAccount);
        //</editor-fold>

        return Result.ok("操作成功");
    }

    /**
     * 兑付确认（变更 承兑商金额、商户金额）
     */
    public Result<Object> orderWithdrawalFinish(OrderMain orderMain) {
        OrderAssurer orderAssurer = orderAssurerServiceImpl.getById(orderMain.getAssurerId());
        OrderAssurerAccount oaap = orderAssurerAccountServiceImpl.getById(orderMain.getAssurerPayAccountId());
        OrderMerchantAccount orderMerchantAccount = orderMerchantAccountServiceImpl.getById(orderMain.getMerchantAccountId());
        if (!BaseUtil.Base_HasValue(orderAssurer)) {
            return Result.error(525, "承兑商不存在");
        }
        if (!BaseUtil.Base_HasValue(oaap)) {
            return Result.error(527, "承兑商支付账户不存在");
        }
        Double orderMoney = orderMain.getAssurerCnyMoney();

        //<editor-fold desc="1.承兑商变更内容（仅更新已使用金额）">
        // 更新已使用金额 = 已用金额+该订单金额
        Double userdLimit = orderAssurer.getUsedLimit() + orderMoney;

        //<editor-fold desc="这里暂时不加锁定金额">
        //// 更新锁定金额
        //Double payLockMoney = orderAssurer.getPayLockMoney() - orderMoney;
        //if (userdLimit + orderAssurer.getCanUseLimit() + payLockMoney != orderAssurer.getTotalLimit()) {
        //    return Result.error(528, "承兑商金额异常(已用金额+可用金额+锁定金额不等于总金额)");
        //}
        //orderAssurer.setPayLockMoney(payLockMoney);
        //</editor-fold>
        orderAssurer.setUsedLimit(userdLimit);
        orderAssurerServiceImpl.updateById(orderAssurer);
        //</editor-fold>

        //<editor-fold desc="2.承兑商账户变更内容（仅更新支付已用金额）">
        // 支付已用金额pay_used_limit
        // 支付锁定金额pay_lock_money（仅支付宝）
        // 支付可用额度(日)pay_can_use_limit（不用管）
        // 每日支付限额pay_limit（不用管）

        // 支付已用金额 = 已用金额+该订单金额
        Double usedLimitAccount = oaap.getPayUsedLimit() + orderMoney;

        //<editor-fold desc="这里暂时不加锁定金额">
        //if ("alipay".equals(oaap.getAccountType())) {
        //    // 目前支付宝账户才进行锁定金额
        //    Double payLockMoneyAccount = oaap.getPayLockMoney() - orderMoney;
        //    if (usedLimitAccount + oaap.getPayCanUseLimit() + oaap.getPayLockMoney() != oaap.getPayLimit()) {
        //        return Result.error(529, "承兑商账户金额异常(已用金额+可用金额+锁定金额不等于总金额)");
        //    }
        //    oaap.setPayLockMoney(payLockMoneyAccount);
        //}
        //</editor-fold>
        oaap.setPayUsedLimit(usedLimitAccount);
        orderAssurerAccountServiceImpl.updateById(oaap);
        //</editor-fold>

        //<editor-fold desc="3.商户账户金额增加">
        orderMerchantAccount.setPayCanUseLimit(orderMerchantAccount.getPayUsedLimit() + orderMain.getSourceCurrencyMoney());
        orderMerchantAccountServiceImpl.updateById(orderMerchantAccount);
        //</editor-fold>

        return Result.ok("操作成功");
    }


    /**
     * 根据目标货币获取区域代码
     */
    public String getAreaCodeByCurrency(String targetCurrency) {
        Map keyMap = new HashMap();
        keyMap.put("CNY", "+86");
        keyMap.put("KRW", "+82");
        keyMap.put("USD", "+1");
        keyMap.put("PHP", "+63");
        keyMap.put("AUD", "+61");
        keyMap.put("JPY", "+81");
        keyMap.put("MYR", "+60");
        if (!BaseUtil.Base_HasValue(keyMap.get(targetCurrency))) {
            return "";
        }
        Object areaCode = keyMap.get(targetCurrency);
        return areaCode.toString();
    }

    //保存收款订单
    public Result<Object> savePaymentOrder(OrderMain orderMain, OrderPlatformAccount platformAccount) {
        Result<Object> result = new Result<Object>();
        orderMain.setOrderCode(getOrderNoByUUID());
        orderMain.setOrderState("2"); //订单状态(1 创建、2 待确认收款、3 已确认收款、0、已作废0)
        orderMain.setOrderType(1); //订单类型
        //绑定平台账户Id 关联order_platform_account(为了方便在订单完成的变更账户金额)
        orderMain.setPlatformCollectionAccountId(platformAccount.getId());
        orderMain.setPlatformCollectionAccount(platformAccount.getAccountNo());
        orderMain.setPlatformCollectionAccountUser(platformAccount.getRealName());
        orderMain.setPlatformCollectionBank(platformAccount.getOpenBank());
        orderMain.setPlatformCollectionBankBranch(platformAccount.getOpenBankBranch());
        orderMain.setPlatformCollectionAreaCode(platformAccount.getAreaCode());
        JSONObject data = new JSONObject();
        try {
            //4.保存订单
            save(orderMain);

            //<editor-fold desc="接口返回信息">
            orderMain.changeMoneyToBig();
            JSONObject bank = new JSONObject();
            bank.put("account_no", orderMain.getPlatformCollectionAccount());
            bank.put("real_name", orderMain.getPlatformCollectionAccountUser());
            bank.put("open_bank", orderMain.getPlatformCollectionBank());
            bank.put("area_code", orderMain.getPlatformCollectionAreaCode());

            data.put("order_no", orderMain.getOrderCode());
            data.put("order_state", orderMain.getOrderState());
            data.put("merchant_id", orderMain.getMerchantId());
            data.put("merchant_name", orderMain.getMerchantName());
            data.put("merchant_contact", orderMain.getMerchantContact());
            data.put("source_currency", orderMain.getSourceCurrency());
            data.put("target_currency", orderMain.getTargetCurrency());
            data.put("source_currency_money", orderMain.getSourceCurrencyMoney());
            data.put("target_currency_money", orderMain.getTargetCurrencyMoney());
            data.put("exchange_rate", orderMain.getExchangeRate());
            data.put("user_pay_method", orderMain.getPlatformCollectionMethod());
            data.put("notify_url", orderMain.getNotifyUrl());
            data.put("bank_info", bank);
            //</editor-fold>
        } catch (Exception ex) {
            log.error("创建订单失败", ex);
            return Result.error(600, "订单创建失败");
        }
        result.setResult(data);
        result.success("订单创建成功");
        return result;
    }


    /**
     * 获取应支付金额（根据目标货币、金额、源货币）
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
     * 计算承兑商需支付的人民币金额,扣减承兑商额度需要
     */
    public void setAssurerCnyMoney(OrderMain orderMain) {
        if (!orderMain.getTargetCurrency().equals("CNY")) {
            if (!orderMain.getSourceCurrency().equals("CNY")) {
                JSONObject rateObj = this.getUserPayMoney("CNY", orderMain.getTargetCurrency(), orderMain.getTargetCurrencyMoney().toString());
                if (BaseUtil.Base_HasValue(rateObj)) {
                    orderMain.setAssurerCnyMoney(rateObj.getDouble("money"));
                }
            } else {
                orderMain.setAssurerCnyMoney(orderMain.getSourceCurrencyMoney());
            }
        } else {
            orderMain.setAssurerCnyMoney(orderMain.getTargetCurrencyMoney());
        }
    }


    /**
     * 给订单分配一个承兑商和账户
     */
    public void dispatchAssurerToOrder(OrderMain orderMain, Map resutMap) {
        OrderAssurer orderAssurer = (OrderAssurer) resutMap.get("orderAssurer");
        OrderAssurerAccount pay = (OrderAssurerAccount) resutMap.get("orderAssurerAccountPay");
//        OrderAssurerAccount collection = (OrderAssurerAccount) resutMap.get("orderAssurerAccountCollection");
//        if (BaseUtil.Base_HasValue(orderAssurer) && BaseUtil.Base_HasValue(pay) && BaseUtil.Base_HasValue(collection)) {
        if (BaseUtil.Base_HasValue(orderAssurer) && BaseUtil.Base_HasValue(pay)) {
            orderMain.setAssurerId(orderAssurer.getId());
            orderMain.setAssurerName(orderAssurer.getAssurerName());
            //orderMain.setAssurerCollectionAccount(collection.getAccountNo());
            //orderMain.setAssurerCollectionAccountId(collection.getId());
            //orderMain.setAssurerCollectionMethod(collection.getAccountType());
            //orderMain.setAssurerCollectionBank(collection.getOpenBank());
            //orderMain.setAssurerCollectionBankBranch(collection.getOpenBankBranch());
            orderMain.setAssurerPayAccountId(pay.getId());
            orderMain.setAssurerPayAccount(pay.getAccountNo());
            orderMain.setAssurerPayAccountUser(pay.getRealName());
            orderMain.setAssurerPayMethod(pay.getAccountType());
            orderMain.setAssurerPayBank(pay.getOpenBank());
            orderMain.setAssurerPayBankBranch(pay.getOpenBankBranch());
            orderMain.setOrderState("2");
            orderMain.setAutoDispatchState(0);  //自动分配承兑商状态 0 成功 1失败
            // 锁定承兑商金额
            lockAssurerMoney(orderMain.getAssurerCnyMoney(), orderAssurer);
            // 锁定承兑账户金额
            lockAssurerAccountMoney(orderMain.getAssurerCnyMoney(), pay);
            // 减少承兑商租赁金
            subAssurerLeaseMoney(orderMain.getAssurerCnyMoney(), orderAssurer, orderMain);
        } else {
            orderMain.setOrderState("1");
            orderMain.setAutoDispatchState(1);  //自动分配承兑商状态 0 成功 1失败
            orderMain.setAutoDispatchText(resutMap.get("message").toString());  //自动分配失败说明
        }
        orderMain.setOrderType(2); //订单类型 1 支付请求 2 提款请求
        if (!BaseUtil.Base_HasValue(orderMain.getId())) {
            orderMain.setOrderCode(getOrderNoByUUID());
            save(orderMain);
        } else {
            updateById(orderMain);
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
    public void subAssurerLeaseMoney(Double money, OrderAssurer orderAssurer, OrderMain orderMain) {
        Double leaseMoney = BaseUtil.mul(money, orderAssurer.getAssurerRate(), 0);
        OrderAssurerMoneyChange orderAssurerMoneyChange = new OrderAssurerMoneyChange();
        orderAssurerMoneyChange.setAssurerId(orderAssurer.getId());
        orderAssurerMoneyChange.setAssurerName(orderAssurer.getAssurerName());
        orderAssurerMoneyChange.setAssurerPhone(orderAssurer.getAssurerPhone());
        orderAssurerMoneyChange.setChangeClass("lease");
        orderAssurerMoneyChange.setChangeType("sub");
        orderAssurerMoneyChange.setChangeMoney(leaseMoney);
        orderAssurerMoneyChange.setChangeText("订单扣减");
        orderAssurerMoneyChange.setOrderId(orderMain.getId());
        orderAssurerMoneyChange.setOrderNo(orderMain.getOrderCode());
        orderAssurerMoneyChangeServiceImpl.save(orderAssurerMoneyChange);
    }
    //</editor-fold>

}