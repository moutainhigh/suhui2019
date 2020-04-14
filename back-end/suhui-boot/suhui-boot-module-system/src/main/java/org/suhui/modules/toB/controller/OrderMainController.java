package org.suhui.modules.toB.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.suhui.common.api.vo.Result;
import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.modules.toB.entity.OrderMain;
import org.suhui.modules.toB.service.IOrderMainService;

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

    @AutoLog(value = "订单表-创建订单")
    @ApiOperation(value = "订单表-创建订单", notes = "订单表-创建订单")
    @PostMapping(value = "/add")
    @Transactional
    public Result<Object> add(
            @RequestParam(name = "userNo") String userNo,
            @RequestParam(name = "userName") String userName,
            @RequestParam(name = "userContact") String userContact,
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
        orderMain.setUserNo(userNo);
        orderMain.setUserName(userName);
        orderMain.setUserContact(userContact);
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
}
