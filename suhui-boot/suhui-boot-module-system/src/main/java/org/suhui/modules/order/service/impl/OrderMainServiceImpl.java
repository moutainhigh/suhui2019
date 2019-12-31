package org.suhui.modules.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.suhui.common.api.vo.Result;
import org.suhui.modules.order.entity.OrderAssurer;
import org.suhui.modules.order.entity.OrderAssurerAccount;
import org.suhui.modules.order.entity.OrderMain;
import org.suhui.modules.order.mapper.OrderMainMapper;
import org.suhui.modules.order.service.IOrderMainService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.suhui.modules.utils.BaseUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 订单表
 * @Author: jeecg-boot
 * @Date: 2019-12-29
 * @Version: V1.0
 */
@Service
public class OrderMainServiceImpl extends ServiceImpl<OrderMainMapper, OrderMain> implements IOrderMainService {

    @Autowired
    OrderAssurerServiceImpl orderAssurerService;

    @Autowired
    OrderAssurerAccountServiceImpl orderAssurerAccountService;


    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final AtomicInteger atomicInteger = new AtomicInteger(1000000);

    /**
     * 创建订单主方法
     */
    @Override
    @Transactional
    public Result<Object> manageOrderByAuto(OrderMain orderMain) {
        Result<Object> result = new Result<Object>();
        // 判断必填项是否有值
        String checkValue = orderMain.checkCreateRequireValue();
        if (BaseUtil.Base_HasValue(checkValue)) {
            return Result.error(511, checkValue);
        }
        // 为订单选择最优承兑商
        Map resutMap = orderAssurerService.getAssurerByOrder(orderMain);
        if (BaseUtil.Base_HasValue(resutMap) && resutMap.get("state").equals("success")) {
            dispatchAssurerToOrder(orderMain, resutMap);
        } else {
            dispatchAssurerToOrder(orderMain, resutMap);
        }
        result.success("订单创建成功");
        return result;
    }

    /**
     * 订单分配承兑商-后台
     */
    @Override
    public Result<Object> dispatchOrderAdmin(String orderId, String assurerId) {
        OrderMain orderMain = getById(orderId);
        OrderAssurer orderAssurer = orderAssurerService.getById(assurerId);
        if (!BaseUtil.Base_HasValue(orderMain)) {
            return Result.error(513, "订单不存在");
        }
        if (!BaseUtil.Base_HasValue(orderAssurer)) {
            return Result.error(515, "承兑商不存在");
        }
        // 为承兑商选择一个支付账号
        OrderAssurerAccount accountPay = orderAssurerAccountService.getAssurerAccountByOrderPay(assurerId, orderMain.getTargetCurrencyMoney());
        if (!accountPay.Base_HasValue(accountPay)) {
            return Result.error(518, "承兑商找不到合适的支付账号");
        }
        // 为承兑商选择一个收款账号
        OrderAssurerAccount accountCollection = orderAssurerAccountService.getAssurerAccountByOrderCollection(assurerId);
        if (!accountPay.Base_HasValue(accountCollection)) {
            return Result.error(519, "承兑商找不到合适的收款账号");
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("state","success");
        resultMap.put("orderAssurer", orderAssurer);
        resultMap.put("orderAssurerAccountPay", accountPay);
        resultMap.put("orderAssurerAccountCollection", accountCollection);
        dispatchAssurerToOrder(orderMain, resultMap);
        return Result.ok("订单分配成功");
    }

    /**
     * 用户确认支付
     */
    @Override
    public Result<Object> userPayConfirm(String orderId) {
        OrderMain orderMain = getById(orderId);
        if (!BaseUtil.Base_HasValue(orderMain)) {
            return Result.error(513, "订单不存在");
        }
        if (orderMain.getOrderState() != 2) {
            return Result.error(514, "订单状态异常");
        }
        orderMain.setOrderState(3);
        orderMain.setUserPayTime(new Date());
        updateById(orderMain);
        return Result.ok("操作成功");
    }

    /**
     * 承兑商确认已收款
     */
    @Override
    public Result<Object> assurerCollectionConfirm(String orderId) {
        OrderMain orderMain = getById(orderId);
        if (!BaseUtil.Base_HasValue(orderMain)) {
            return Result.error(513, "订单不存在");
        }
        if (orderMain.getOrderState() != 3) {
            return Result.error(514, "订单状态异常");
        }
        orderMain.setOrderState(4);
        orderMain.setAssurerCollectionTime(new Date());
        updateById(orderMain);
        return Result.ok("操作成功");
    }

    /**
     * 承兑商确认已兑付
     */
    @Override
    public Result<Object> assurerPayConfirm(String orderId) {
        OrderMain orderMain = getById(orderId);
        if (!BaseUtil.Base_HasValue(orderMain)) {
            return Result.error(513, "订单不存在");
        }
        if (orderMain.getOrderState() != 4) {
            return Result.error(514, "订单状态异常");
        }
        orderMain.setOrderState(5);
        orderMain.setAssurerPayTime(new Date());
        updateById(orderMain);
        return Result.ok("操作成功");
    }

    /**
     * 用户确认已收款-订单完成
     */
    @Override
    public Result<Object> userCollectionConfirm(String orderId) {
        OrderMain orderMain = getById(orderId);
        if (!BaseUtil.Base_HasValue(orderMain)) {
            return Result.error(513, "订单不存在");
        }
        if (orderMain.getOrderState() != 5) {
            return Result.error(514, "订单状态异常");
        }
        orderMain.setOrderState(6);
        orderMain.setUserCollectionTime(new Date());
        updateById(orderMain);
        return orderFinishChangeAussurerMoney(orderMain);
    }


    /**
     * 订单完成-释放承兑商锁定金额
     */
    public Result<Object> orderFinishChangeAussurerMoney(OrderMain orderMain) {
        OrderAssurer orderAssurer = orderAssurerService.getById(orderMain.getAssurerId());
        OrderAssurerAccount oaac = orderAssurerAccountService.getById(orderMain.getAssurerCollectionAccountId());
        OrderAssurerAccount oaap = orderAssurerAccountService.getById(orderMain.getAssurerPayAccountId());
        if (!BaseUtil.Base_HasValue(orderAssurer)) {
            return Result.error(515, "承兑商不存在");
        }
        if (!BaseUtil.Base_HasValue(oaac)) {
            return Result.error(516, "承兑商收款账户不存在");
        }
        if (!BaseUtil.Base_HasValue(oaap)) {
            return Result.error(517, "承兑商支付账户不存在");
        }
        Integer orderMoney = orderMain.getTargetCurrencyMoney();
        // 更新已使用金额 = 已用金额+该订单金额
        Integer userdLimit = orderAssurer.getUsedLimit() + orderMoney;
        // 更新锁定金额
        Integer payLockMoney = orderAssurer.getPayLockMoney() - orderMoney;
        if (userdLimit + orderAssurer.getCanUseLimit() + payLockMoney != orderAssurer.getTotalLimit()) {
            return Result.error(520, "承兑商金额异常(已用金额+可用金额+锁定金额不等于总金额)");
        }
        orderAssurer.setUsedLimit(userdLimit);
        orderAssurer.setPayLockMoney(payLockMoney);
        orderAssurerService.updateById(orderAssurer);
        // 目前支付宝账户才进行锁定金额
        if ("alipay".equals(oaap.getAccountType())) {
            // 更新账户已使用金额 = 已用金额+该订单金额
            Integer usedLimitAccount = oaap.getPayUsedLimit() + orderMoney;
            // 更新账户锁定金额
            Integer payLockMoneyAccount = oaap.getPayLockMoney() - orderMoney;
            if (usedLimitAccount + oaap.getPayCanUseLimit() + oaap.getPayLockMoney() != oaap.getPayLimit()) {
                return Result.error(520, "承兑商账户金额异常(已用金额+可用金额+锁定金额不等于总金额)");
            }
            oaap.setPayLockMoney(payLockMoneyAccount);
            oaap.setPayUsedLimit(usedLimitAccount);
        }
        // 收款账户已用金额增加
        if (oaac.getId().equals(oaap.getId())) {
            oaap.setCollectionUsedLimit(oaac.getCollectionUsedLimit() + orderMoney);
        } else {
            oaac.setCollectionUsedLimit(oaac.getCollectionUsedLimit() + orderMoney);
            orderAssurerAccountService.updateById(oaac);
        }
        orderAssurerAccountService.updateById(oaap);
        return Result.ok("操作成功");
    }

    /**
     * 通过源货币和目标货币获取汇率计算金额
     */
    @Override
    public JSONObject getUserPayMoney(String source, String target, String money, String token) {
        JSONObject valueObj = new JSONObject();
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:3333/api/login/payCurrencyRate/getCurrencyRateByRateCode";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("X-Access-Token", token);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("source_currency_code", source);
            map.add("target_currency_code", target);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, request, JSONObject.class);
            if (response.getBody().getInteger("code") == 0) {
                JSONObject result = response.getBody().getJSONObject("result");
                JSONObject data = result.getJSONObject("data");
                String rate = data.getString("rate_now");
                BigDecimal a1 = new BigDecimal(money);
                BigDecimal b1 = new BigDecimal(rate);
                BigDecimal rate_now_divide = a1.multiply(b1);
                Double value = rate_now_divide.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                valueObj.put("money", value);
                valueObj.put("rate", rate);
            }
        } catch (Exception e) {
            return null;
        }
        return valueObj;
    }

    /**
     * 给订单分配一个承兑商和账户
     */
    public void dispatchAssurerToOrder(OrderMain orderMain, Map resutMap) {
        OrderAssurer orderAssurer = (OrderAssurer) resutMap.get("orderAssurer");
        OrderAssurerAccount pay = (OrderAssurerAccount) resutMap.get("orderAssurerAccountPay");
        OrderAssurerAccount collection = (OrderAssurerAccount) resutMap.get("orderAssurerAccountCollection");
        if (BaseUtil.Base_HasValue(orderAssurer) && BaseUtil.Base_HasValue(pay) && BaseUtil.Base_HasValue(collection)) {
            orderMain.setAssurerId(orderAssurer.getId());
            orderMain.setAssurerName(orderAssurer.getAssurerName());
            orderMain.setAssurerCollectionAccount(collection.getAccountNo());
            orderMain.setAssurerCollectionAccountId(collection.getId());
            orderMain.setAssurerCollectionMethod(collection.getAccountType());
            orderMain.setAssurerPayAccountId(pay.getId());
            orderMain.setAssurerPayAccount(pay.getAccountNo());
            orderMain.setAssurerPayMethod(pay.getAccountType());
            orderMain.setOrderState(2);
            orderMain.setAutoDispatchState(1);
            // 锁定承兑商金额
            lockAssurerMoney(orderMain.getTargetCurrencyMoney(), orderAssurer);
            // 锁定承兑账户金额
            lockAssurerAccountMoney(orderMain.getTargetCurrencyMoney(), pay);
        } else {
            orderMain.setOrderState(1);
            orderMain.setAutoDispatchState(0);
            orderMain.setAutoDispatchText(resutMap.get("message").toString());
        }
        if (!BaseUtil.Base_HasValue(orderMain.getId())) {
            orderMain.setOrderCode(getOrderNoByUUID());
            save(orderMain);
        } else {
            updateById(orderMain);
        }
    }


    public static synchronized String getOrderNoByUUID() {
        Integer uuidHashCode = UUID.randomUUID().toString().hashCode();
        if (uuidHashCode < 0) {
            uuidHashCode = uuidHashCode * (-1);
        }
        String date = simpleDateFormat.format(new Date());
        return date + uuidHashCode;
    }

    /**
     * 锁定承运商支付金额
     */
    public void lockAssurerMoney(int money, OrderAssurer orderAssurer) {
        int lockMoney = orderAssurer.getPayLockMoney() + money;
        int canUseLimit = orderAssurer.getCanUseLimit() - money;
        orderAssurer.setCanUseLimit(canUseLimit);
        orderAssurer.setPayLockMoney(lockMoney);
        orderAssurerService.updateById(orderAssurer);
    }

    /**
     * 锁定承运商账号支付金额
     */
    public void lockAssurerAccountMoney(int money, OrderAssurerAccount orderAssurerAccount) {
        if (orderAssurerAccount.getAccountType().equals("alipay")) {
            int lockMoney = orderAssurerAccount.getPayLockMoney() + money;
            int canUseLimit = orderAssurerAccount.getPayCanUseLimit() - money;
            orderAssurerAccount.setPayCanUseLimit(canUseLimit);
            orderAssurerAccount.setPayLockMoney(lockMoney);
            orderAssurerAccountService.updateById(orderAssurerAccount);
        }
    }

}
