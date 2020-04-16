package org.suhui.modules.toB.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.suhui.common.api.vo.Result;
import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.modules.toB.entity.OrderMain;
import org.suhui.modules.toB.mapper.OrderMainMapper;
import org.suhui.modules.toB.service.IOrderMainService;

import javax.servlet.http.HttpServletRequest;

/**
 * 类说明：订单相关
 *
 * @author: 蔡珊珊
 * @create: 2020-04-14 13:27
 */
@Slf4j
@Api(tags = "订单相关")
@RestController
@RequestMapping("/order")
public class OrderMainController {
    @Autowired
    private IOrderMainService orderMainService;
    @Autowired
    private OrderMainMapper orderMainMapper;

    @AutoLog(value = "创建收款订单")
    @ApiOperation(value = "创建收款订单", notes = "创建收款订单")
    @PostMapping(value = "/createPaymentOrder")
    @Transactional
    public Result<Object> createPaymentOrder(
            @RequestParam(name = "merchantId") String merchantId,
            @RequestParam(name = "merchantName") String merchantName,
            @RequestParam(name = "merchantContact") String merchantContact,
            @RequestParam(name = "sourceCurrency") String sourceCurrency,
            @RequestParam(name = "targetCurrency") String targetCurrency,
            @RequestParam(name = "exchangeRate") String exchangeRate,
            @RequestParam(name = "targetCurrencyMoney") String targetCurrencyMoney,
            @RequestParam(name = "payMethod") String payMethod,
            @RequestParam(name = "orderText") String orderText,
            @RequestParam(name = "notifyUrl") String notifyUrl
            ) {
        Result<Object> result = new Result<Object>();
        OrderMain orderMain = new OrderMain();
        orderMain.setMerchantId(merchantId);
        orderMain.setMerchantName(merchantName);
        orderMain.setMerchantContact(merchantContact);
        orderMain.setSourceCurrency(sourceCurrency);
        orderMain.setTargetCurrency(targetCurrency);
        orderMain.setExchangeRate(Double.parseDouble(exchangeRate));
        orderMain.setTargetCurrencyMoney(Double.parseDouble(targetCurrencyMoney));
        orderMain.setUserPayMethod(payMethod);
        orderMain.setOrderText(orderText);
        orderMain.setNotifyUrl(notifyUrl);
        try {
            result = orderMainService.createPaymentOrder(orderMain);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value = "确认已收款")
    @ApiOperation(value = "确认已收款", notes = "确认已收款")
    @PostMapping(value = "/confirmPaymentOrder")
    @Transactional
    public Result<Object> confirmPaymentOrder (HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderMainService.confirmPaymentOrder(jsonObject.getString("orderIds"));
            return result;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value = "订单表-通过OrderNo查询")
    @ApiOperation(value = "订单表-通过OrderNo查询", notes = "订单表-通过OrderNo查询")
    @PostMapping(value = "/queryByOrderNo")
    public Result<OrderMain> queryByOrderNo(@RequestParam(name = "orderNo", required = true) String orderNo) {
        Result<OrderMain> result = new Result<OrderMain>();
        OrderMain orderMain = orderMainMapper.queryByOrderNo(orderNo);
        if (orderMain == null) {
            result.error500("未找到对应实体");
        } else {
            orderMain.changeMoneyToBig();
            result.setResult(orderMain);
            result.setSuccess(true);
        }
        return result;
    }

    @AutoLog(value = "创建提款订单")
    @ApiOperation(value = "创建提款订单", notes = "创建提款订单")
    @PostMapping(value = "/add")
    @Transactional
    public Result<Object> createWithdrawalOrder(
            @RequestParam(name = "merchantId") String merchantId,
            @RequestParam(name = "merchantName") String merchantName,
            @RequestParam(name = "merchantContact") String merchantContact,
            @RequestParam(name = "sourceCurrency") String sourceCurrency,
            @RequestParam(name = "targetCurrency") String targetCurrency,
            @RequestParam(name = "targetCurrencyMoney") String targetCurrencyMoney,
            @RequestParam(name = "collectionMethod") String collectionMethod,
            @RequestParam(name = "collectionAccount") String collectionAccount,
            @RequestParam(name = "collectionAccountUser") String collectionAccountUser,
            @RequestParam(name = "orderText") String orderText,
            @RequestParam(name = "notifyUrl") String notifyUrl
    ) {
        Result<Object> result = new Result<Object>();
        OrderMain orderMain = new OrderMain();
        orderMain.setMerchantId(merchantId);
        orderMain.setMerchantName(merchantName);
        orderMain.setMerchantContact(merchantContact);
        orderMain.setSourceCurrency(sourceCurrency);
        orderMain.setTargetCurrency(targetCurrency);
        orderMain.setTargetCurrencyMoney(Double.parseDouble(targetCurrencyMoney));
        orderMain.setMerchantCollectionMethod(collectionMethod);
        orderMain.setMerchantCollectionAccount(collectionAccount);
        orderMain.setMerchantCollectionAccountUser(collectionAccountUser);
        orderMain.setOrderText(orderText);
        orderMain.setNotifyUrl(notifyUrl);
        try {
            result = orderMainService.createWithdrawalOrder(orderMain);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value = "确认已处理")
    @ApiOperation(value = "确认已处理", notes = "确认已处理")
    @PostMapping(value = "/confirmWithdrawalOrder")
    @Transactional
    public Result<Object> confirmWithdrawalOrder (HttpServletRequest request, @RequestBody JSONObject jsonObject){
        Result<Object> result = new Result<Object>();
        try {
            result = orderMainService.confirmWithdrawalOrder(jsonObject.getString("orderIds"));
            return result;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

}
