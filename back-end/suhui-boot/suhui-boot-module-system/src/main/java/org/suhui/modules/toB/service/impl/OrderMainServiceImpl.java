package org.suhui.modules.toB.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * 创建收款订单
     *
     * @param
     * @return
     */
    @Override
    @Transactional
    public Result<Object> createPaymentOrder(OrderMain orderMain) {
        // 判断必填项是否有值
        String checkValue = orderMain.checkPaymentOrderRequireValue();
        if (BaseUtil.Base_HasValue(checkValue)) {
            return Result.error(531, checkValue);
        }
        if (BaseUtil.Base_HasValue(orderMerchantMapper.getMerchantById(orderMain.getMerchantId()))) {
            return Result.error(601, "商户不存在");
        }
        //根据目标货币获取区域代码
        String areaCode = getAreaCodeByCurrency(orderMain.getTargetCurrency()).toString();
        //根据区域代码和商户Id获取商户的账户
        OrderMerchantAccount orderMerchantAccount = orderMerchantAccountMapper.getAccountByMerchantId(orderMain.getMerchantId(), areaCode);
        if (BaseUtil.Base_HasValue(orderMerchantAccount)) {
            return Result.error(602, "商户收款账户不存在");
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
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("areaCode", areaCode);
        List<OrderPlatformAccount> platformAccountList = orderPlatformAccountServiceImpl.getAccountListByAreaCode(paramMap);
        if (!BaseUtil.Base_HasValue(platformAccountList)) {
            return Result.error(532, "获取用户收款账户失败");
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
    public Result<Object> confirmReceivedMoney(String orderIds) {
        String[] idArr = orderIds.split(",");
        Result<Object> result = new Result<>();
        boolean check = true;
        for (String orderId : idArr) {
            OrderMain orderMain = getById(orderId);
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

            orderMain.setUpdateTime(new Date());
            orderMain.setUpdateBy("");
            updateById(orderMain);
            //账户金额变动
            Result<Object> finishResult = orderFinishChangeAccountMoney(orderMain, "payment");
            if (!finishResult.isSuccess()) {
                check = false;
                result = finishResult;
            }
        }
        if (!check) {
            return result;
        }
        //回调notify_url

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
    public Result<Object> createWithdrawalOrder(OrderMain orderMain) {
        // 判断必填项是否有值
        String checkValue = orderMain.checkWithdrawalOrderRequireValue();
        if (BaseUtil.Base_HasValue(checkValue)) {
            return Result.error(531, checkValue);
        }
        if (BaseUtil.Base_HasValue(orderMerchantMapper.getMerchantById(orderMain.getMerchantId()))) {
            return Result.error(601, "商户不存在");
        }


        //根据目标货币获取区域代码
        String areaCode = getAreaCodeByCurrency(orderMain.getTargetCurrency()).toString();
        //根据区域代码和商户Id获取商户的账户
        OrderMerchantAccount orderMerchantAccount = orderMerchantAccountMapper.getAccountByMerchantId(orderMain.getMerchantId(), areaCode);
        if (BaseUtil.Base_HasValue(orderMerchantAccount)) {
            return Result.error(602, "商户收款账户不存在");
        }
        if (!orderMerchantAccount.getAccountNo().equals(orderMain.getMerchantCollectionAccount())) {
            return Result.error(604, "商户收款账户不一致");
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
        //计算承兑商需支付的人民币金额
        setAssurerCnyMoney(orderMain);
        ////<editor-fold desc="3.根据区域代码，获取平台支付账号">
        //Map<String, Object> paramMap = new HashMap<>();
        //paramMap.put("areaCode", areaCode);
        //List<OrderPlatformAccount> platformAccountList = orderPlatformAccountServiceImpl.getAccountListByAreaCode(paramMap);
        //if (!BaseUtil.Base_HasValue(platformAccountList)) {
        //    return Result.error(532, "获取用户收款账户失败");
        //}
        //OrderPlatformAccount platformAccount = platformAccountList.get(0);
        ////</editor-fold>

        // 为订单选择最优承兑商
        Map resutMap = orderAssurerServiceImpl.getAssurerByOrder(orderMain);
        if (BaseUtil.Base_HasValue(resutMap) && resutMap.get("state").equals("success")) {
            dispatchAssurerToOrder(orderMain, resutMap);
        } else {
            dispatchAssurerToOrder(orderMain, resutMap);
        }
        //保存提款订单
        //return saveWithdrawalOrder(orderMain, platformAccount);
        return null;
    }


    /**
     * 平台确认已处理
     */
    @Override
    @Transactional
    public Result<Object> confirmProcessed(String orderIds) {
        String[] idArr = orderIds.split(",");
        Result<Object> result = new Result<>();
        boolean check = true;
        for (String orderId : idArr) {
            OrderMain orderMain = getById(orderId);
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

            orderMain.setUpdateTime(new Date());
            orderMain.setUpdateBy("");
            updateById(orderMain);
            //账户金额变动
            Result<Object> finishResult = orderFinishChangeAccountMoney(orderMain, "withdrawal");
            if (!finishResult.isSuccess()) {
                check = false;
                result = finishResult;
            }
        }
        if (!check) {
            return result;
        }
        //回调notify_url

        return Result.ok("操作成功");
    }


    //<editor-fold desc="辅助方法">

    /**
     * 订单完成-金额变动
     */
    public Result<Object> orderFinishChangeAccountMoney(OrderMain orderMain, String type) {

        OrderPlatformAccount orderPlatformAccount = orderPlatformAccountServiceImpl.getById(orderMain.getUserPayAccountId());
        OrderMerchantAccount orderMerchantAccount = orderMerchantAccountServiceImpl.getById(orderMain.getMerchantAccountId());

        if (!BaseUtil.Base_HasValue(orderPlatformAccount)) {
            return Result.error(-1, "获取平台账户失败");
        }
        if (!BaseUtil.Base_HasValue(orderMerchantAccount)) {
            return Result.error(-1, "获取商户账户失败");
        }
        //平台账户增加余额
        Double orderMoney = orderMain.getTargetCurrencyMoney();
        if (type == "payment") {
            Double accountMoney = orderPlatformAccount.getAccountMoney() + orderMoney;
            orderPlatformAccount.setAccountMoney(accountMoney);
        }
        if (type == "withdrawal") {
            Double accountMoney = orderPlatformAccount.getAccountMoney() - orderMoney;
            orderPlatformAccount.setAccountMoney(accountMoney);
        }
        orderPlatformAccount.setUpdateTime(new Date());
        orderPlatformAccount.setUpdateBy("");
        orderPlatformAccountMapper.updateById(orderPlatformAccount);

        //商户账户减少金额
        // 目前支付宝账户才进行锁定金额
        if ("alipay".equals(orderMerchantAccount.getAccountType())) {
            // 更新账户已使用金额 = 已用金额+该订单金额
            Double usedLimitAccount = orderMerchantAccount.getPayUsedLimit() + orderMoney;
            // 更新账户锁定金额
            Double payLockMoneyAccount = orderMerchantAccount.getPayLockMoney() - orderMoney;
            if (usedLimitAccount + orderMerchantAccount.getPayCanUseLimit() + orderMerchantAccount.getPayLockMoney() != orderMerchantAccount.getPayLimit()) {
                return Result.error(529, "商户账户金额异常(已用金额+可用金额+锁定金额不等于总金额)");
            }
            orderMerchantAccount.setPayLockMoney(payLockMoneyAccount);
            orderMerchantAccount.setPayUsedLimit(usedLimitAccount);
        }
        // 收款账户已用金额增加
        if (orderMerchantAccount.getId().equals(orderMerchantAccount.getId())) {
            orderMerchantAccount.setCollectionUsedLimit(orderMerchantAccount.getCollectionUsedLimit() + orderMoney);
        } else {
            orderMerchantAccount.setCollectionUsedLimit(orderMerchantAccount.getCollectionUsedLimit() + orderMoney);
            orderMerchantAccountServiceImpl.updateById(orderMerchantAccount);
        }
        orderMerchantAccountServiceImpl.updateById(orderMerchantAccount);
        return Result.ok("操作成功");
    }

    /**
     * 根据目标货币获取区域代码
     */
    public Result<Object> getAreaCodeByCurrency(String targetCurrency) {
        Map keyMap = new HashMap();
        keyMap.put("CNY", "+86");
        keyMap.put("KRW", "+82");
        keyMap.put("USD", "+1");
        keyMap.put("PHP", "+63");
        keyMap.put("AUD", "+61");
        keyMap.put("JPY", "+81");
        keyMap.put("MYR", "+60");
        if (!BaseUtil.Base_HasValue(keyMap.get(targetCurrency))) {
            return Result.error(603, "没有合适的区域代码");
        }
        Object areaCode = keyMap.get(targetCurrency);
        return Result.ok(areaCode);
    }

    //保存收款订单
    public Result<Object> savePaymentOrder(OrderMain orderMain, OrderPlatformAccount platformAccount) {
        Result<Object> result = new Result<Object>();
        orderMain.setOrderCode(getOrderNoByUUID());
        orderMain.setOrderState("2"); //订单状态(1 创建、2 待确认收款、3 已确认收款、0、已作废0)
        orderMain.setOrderType(1); //订单类型
        //绑定平台账户Id 关联order_platform_account(为了方便在订单完成的变更账户金额)
        orderMain.setUserPayAccountId(platformAccount.getId());
        orderMain.setUserPayAccount(platformAccount.getAccountNo());
        orderMain.setUserPayAccountUser(platformAccount.getRealName());
        orderMain.setUserPayBank(platformAccount.getOpenBank());
        orderMain.setUserPayBankBranch(platformAccount.getOpenBankBranch());
        orderMain.setUserPayAreaCode(platformAccount.getAreaCode());
        JSONObject data = new JSONObject();
        try {
            //4.保存订单
            save(orderMain);

            //<editor-fold desc="接口返回信息">
            orderMain.changeMoneyToBig();
            JSONObject bank = new JSONObject();
            bank.put("account_no", orderMain.getUserPayAccount());
            bank.put("real_name", orderMain.getUserPayAccountUser());
            bank.put("open_bank", orderMain.getUserPayBank());
            bank.put("area_code", orderMain.getUserPayAreaCode());

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
            data.put("user_pay_method", orderMain.getUserPayMethod());
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

    //保存提款订单
    public Result<Object> saveWithdrawalOrder(OrderMain orderMain, OrderPlatformAccount platformAccount) {
        Result<Object> result = new Result<Object>();
        orderMain.setOrderCode(getOrderNoByUUID());
        orderMain.setOrderState("2"); //提款—订单状态(1 创建、2 待处理、3 已处理、0、已作废0)
        orderMain.setOrderType(2); //订单类型 1 支付请求 2 提款请求
        //绑定平台账户Id 关联order_platform_account(为了方便在订单完成的变更账户金额)
        orderMain.setUserPayAccountId(platformAccount.getId());
        orderMain.setUserPayAccount(platformAccount.getAccountNo());
        orderMain.setUserPayAccountUser(platformAccount.getRealName());
        orderMain.setUserPayBank(platformAccount.getOpenBank());
        orderMain.setUserPayBankBranch(platformAccount.getOpenBankBranch());
        orderMain.setUserPayAreaCode(platformAccount.getAreaCode());

        JSONObject data = new JSONObject();
        try {
            //4.保存订单
            save(orderMain);

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
        OrderAssurerAccount collection = (OrderAssurerAccount) resutMap.get("orderAssurerAccountCollection");
        if (BaseUtil.Base_HasValue(orderAssurer) && BaseUtil.Base_HasValue(pay) && BaseUtil.Base_HasValue(collection)) {
            orderMain.setAssurerId(orderAssurer.getId());
            orderMain.setAssurerName(orderAssurer.getAssurerName());
            orderMain.setAssurerCollectionAccount(collection.getAccountNo());
            orderMain.setAssurerCollectionAccountId(collection.getId());
            orderMain.setAssurerCollectionMethod(collection.getAccountType());
            orderMain.setAssurerCollectionBank(collection.getOpenBank());
            orderMain.setAssurerCollectionBankBranch(collection.getOpenBankBranch());
            orderMain.setAssurerPayAccountId(pay.getId());
            orderMain.setAssurerPayAccount(pay.getAccountNo());
            orderMain.setAssurerPayMethod(pay.getAccountType());
            orderMain.setAssurerPayBank(pay.getOpenBank());
            orderMain.setAssurerPayBankBranch(pay.getOpenBankBranch());
            orderMain.setOrderState("2");
            orderMain.setAssurerCollectionAccountUser(collection.getRealName());
            orderMain.setAssurerPayAccountUser(pay.getRealName());
            // 锁定承兑商金额
            lockAssurerMoney(orderMain.getAssurerCnyMoney(), orderAssurer);
            // 锁定承兑账户金额
            lockAssurerAccountMoney(orderMain.getAssurerCnyMoney(), pay);
            // 减少承兑商租赁金
            subAssurerLeaseMoney(orderMain.getAssurerCnyMoney(), orderAssurer, orderMain);
        } else {
            orderMain.setOrderState("1");
            orderMain.setAutoDispatchState(1);
            orderMain.setAutoDispatchText(resutMap.get("message").toString());
        }
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