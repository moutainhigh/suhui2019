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

    @AutoLog(value = "创建订单")
    @ApiOperation(value = "创建订单", notes = "创建订单")
    @PostMapping(value = "/add")
    @Transactional
    public Result<Object> add(
            @RequestParam(name = "merchantId") String merchantId,
            @RequestParam(name = "merchantName") String merchantName,
            @RequestParam(name = "merchantContact") String merchantContact,
            @RequestParam(name = "sourceCurrency") String sourceCurrency,
            @RequestParam(name = "targetCurrency") String targetCurrency,
            @RequestParam(name = "exchangeRate") String exchangeRate,
            @RequestParam(name = "targetCurrencyMoney") String targetCurrencyMoney,
            @RequestParam(name = "userPayMethod") String userPayMethod,
            @RequestParam(name = "orderText") String orderText,
            @RequestParam(name = "notifyUrl") String notifyUrl,
            @RequestParam(name = "payMethod") String payMethod
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
        orderMain.setUserPayMethod(userPayMethod);
        orderMain.setOrderText(orderText);
        orderMain.setUserPayBank(notifyUrl);
        orderMain.setUserPayMethod(payMethod);
        try {
            result = orderMainService.add(orderMain);
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
    @PostMapping(value = "/confirmReceivedMoney")
    @Transactional
    public Result<Object> confirmReceivedMoney (HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderMainService.confirmReceivedMoney(jsonObject.getString("orderIds"));
            return result;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }
}
