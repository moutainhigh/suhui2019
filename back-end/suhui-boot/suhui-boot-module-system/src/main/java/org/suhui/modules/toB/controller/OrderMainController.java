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
import org.suhui.modules.toB.entity.OrderAssurer;
import org.suhui.modules.toB.entity.OrderMain;
import org.suhui.modules.toB.mapper.OrderMainMapper;
import org.suhui.modules.toB.service.IOrderMainService;
import org.suhui.modules.utils.BaseUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类说明：订单相关
 *
 * @author: 蔡珊珊
 * @create: 2020-04-12 16:06
 **/

@Slf4j
@Api(tags = "订单相关")
@RestController
@RequestMapping("/order")
public class OrderMainController {
    @Autowired
    private IOrderMainService orderMainService;
    @Autowired
    private OrderMainMapper orderMainMapper;

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
            @RequestParam(name = "userPayAccount") String userPayAccount,
            @RequestParam(name = "userPayBank") String userPayBank,
            @RequestParam(name = "userPayBankBranch") String userPayBankBranch,
            @RequestParam(name = "userPayAreaCode") String userPayAreaCode,
            @RequestParam(name = "userPayAccountUser") String userPayAccountUser) {
        Result<Object> result = new Result<Object>();
//        String accessToken = request.getHeader("X-Access-Token");
        OrderMain orderMain = new OrderMain();
        orderMain.setUserNo(userNo);
        orderMain.setUserName(userName);
        orderMain.setUserContact(userContact);
        orderMain.setSourceCurrency(sourceCurrency);
        orderMain.setTargetCurrency(targetCurrency);
        orderMain.setExchangeRate(Double.parseDouble(exchangeRate));
        orderMain.setTargetCurrencyMoney(Double.parseDouble(targetCurrencyMoney));
        orderMain.setUserPayMethod(userPayMethod);
        orderMain.setUserPayAccount(userPayAccount);
        orderMain.setUserPayBank(userPayBank);
        orderMain.setUserPayBankBranch(userPayBankBranch);
        orderMain.setUserPayAreaCode(userPayAreaCode);
        orderMain.setUserPayAccountUser(userPayAccountUser);
        try {
            result = orderMainService.manageOrderByAuto(orderMain);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value = "订单表-取消订单")
    @ApiOperation(value = "订单表-取消订单", notes = "订单表-取消订单")
    @PostMapping(value = "/revokeOrder")
    @Transactional
    public Result<Object> revokeOrder(HttpServletRequest request, @RequestParam(name = "orderId", required = true) String orderId) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderMainService.revokeOrderAdmin(orderId);
            return result;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value = "订单表-通过用户id查询订单")
    @ApiOperation(value = "订单表-通过用户id查询订单", notes = "订单表-通过用户id查询订单")
    @PostMapping(value = "/orderByUser")
    public Result<List<OrderMain>> queryByUserId(@RequestParam(name = "userNo", required = true) String userNo) {
        Result<List<OrderMain>> result = new Result<List<OrderMain>>();
        Map<String,String> param = new HashMap<>();
        param.put("userId", userNo);
        List<OrderMain> orderMains = orderMainMapper.findByUserId(param);
        if (BaseUtil.Base_HasValue(orderMains)) {
            for (int i = 0; i < orderMains.size(); i++) {
                orderMains.get(i).changeMoneyToBig();
            }
        }
        result.setResult(orderMains);
        result.setSuccess(true);
        return result;
    }

    @AutoLog(value = "订单表-用户确认已支付")
    @ApiOperation(value = "订单表-用户确认已支付", notes = "订单表-用户确认已支付")
    @PostMapping(value = "/userPay")
    @Transactional
    public Result<Object> userPay(HttpServletRequest request, @RequestParam(name = "orderId", required = true) String orderId
            , @RequestParam(name = "voucher", required = true) String voucher) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderMainService.userPayConfirm(orderId,voucher);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value = "订单表-承兑商确认已收款")
    @ApiOperation(value = "订单表-承兑商确认已收款", notes = "订单表-承兑商确认已收款")
    @PostMapping(value = "/assurerCollection")
    @Transactional
    public Result<Object> assurerCollection(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderMainService.assurerCollectionConfirm(jsonObject.getString("orderIds"));
            return result;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value = "订单表-承兑商确认已兑付")
    @ApiOperation(value = "订单表-承兑商确认已兑付", notes = "订单表-承兑商确认已兑付")
    @PostMapping(value = "/assurerPay")
    @Transactional
    public Result<Object> assurerPay(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderMainService.assurerPayConfirm(jsonObject.getString("orderId"),jsonObject.getString("fileList"));
            return result;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value = "订单表-通过id查询")
    @ApiOperation(value = "订单表-通过id查询", notes = "订单表-通过id查询")
    @PostMapping(value = "/order")
    public Result<OrderMain> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<OrderMain> result = new Result<OrderMain>();
        OrderMain orderMain = orderMainService.getById(id);
        if (orderMain == null) {
            result.error500("未找到对应实体");
        } else {
            orderMain.changeMoneyToBig();
            result.setResult(orderMain);
            result.setSuccess(true);
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

}
