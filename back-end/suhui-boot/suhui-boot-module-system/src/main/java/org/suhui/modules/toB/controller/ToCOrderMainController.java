package org.suhui.modules.toB.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类说明：订单相关
 *
 * @author: 蔡珊珊
 * @create: 2020-04-12 16:06
 **/

@Slf4j
@Api(tags = "ToC订单相关")
@RestController
@RequestMapping("/order")
public class ToCOrderMainController {
    //@Autowired
    //private IToCOrderMainService orderMainService;
    //@Autowired
    //private ToCOrderMainMapper toCOrderMainMapper;
    //
    //@AutoLog(value = "订单表-创建订单")
    //@ApiOperation(value = "订单表-创建订单", notes = "订单表-创建订单")
    //@PostMapping(value = "/add")
    //@Transactional
//    public Result<Object> add(
//            @RequestParam(name = "userNo") String userNo,
//            @RequestParam(name = "userName") String userName,
//            @RequestParam(name = "userContact") String userContact,
//            @RequestParam(name = "sourceCurrency") String sourceCurrency,
//            @RequestParam(name = "targetCurrency") String targetCurrency,
//            @RequestParam(name = "exchangeRate") String exchangeRate,
//            @RequestParam(name = "targetCurrencyMoney") String targetCurrencyMoney,
//            @RequestParam(name = "userPayMethod") String userPayMethod,
//            @RequestParam(name = "userPayAccount") String userPayAccount,
//            @RequestParam(name = "userPayBank") String userPayBank,
//            @RequestParam(name = "userPayBankBranch") String userPayBankBranch,
//            @RequestParam(name = "userPayAreaCode") String userPayAreaCode,
//            @RequestParam(name = "userPayAccountUser") String userPayAccountUser) {
//        Result<Object> result = new Result<Object>();
////        String accessToken = request.getHeader("X-Access-Token");
//        ToCOrderMain toCOrderMain = new ToCOrderMain();
//        toCOrderMain.setUserNo(userNo);
//        toCOrderMain.setUserName(userName);
//        toCOrderMain.setUserContact(userContact);
//        toCOrderMain.setSourceCurrency(sourceCurrency);
//        toCOrderMain.setTargetCurrency(targetCurrency);
//        toCOrderMain.setExchangeRate(Double.parseDouble(exchangeRate));
//        toCOrderMain.setTargetCurrencyMoney(Double.parseDouble(targetCurrencyMoney));
//        toCOrderMain.setUserPayMethod(userPayMethod);
//        toCOrderMain.setUserPayAccount(userPayAccount);
//        toCOrderMain.setUserPayBank(userPayBank);
//        toCOrderMain.setUserPayBankBranch(userPayBankBranch);
//        toCOrderMain.setUserPayAreaCode(userPayAreaCode);
//        toCOrderMain.setUserPayAccountUser(userPayAccountUser);
//        try {
//            result = orderMainService.manageOrderByAuto(toCOrderMain);
//            return result;
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//
//            result.error500("操作失败");
//        }
//        return result;
//    }

    //@AutoLog(value = "订单表-取消订单")
    //@ApiOperation(value = "订单表-取消订单", notes = "订单表-取消订单")
    //@PostMapping(value = "/revokeOrder")
    //@Transactional
    //public Result<Object> revokeOrder(HttpServletRequest request, @RequestParam(name = "orderId", required = true) String orderId) {
    //    Result<Object> result = new Result<Object>();
    //    try {
    //        result = orderMainService.revokeOrderAdmin(orderId);
    //        return result;
    //    } catch (Exception e) {
    //        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    //        log.error(e.getMessage(), e);
    //        result.error500("操作失败");
    //    }
    //    return result;
    //}
    //
    //@AutoLog(value = "订单表-通过用户id查询订单")
    //@ApiOperation(value = "订单表-通过用户id查询订单", notes = "订单表-通过用户id查询订单")
    //@PostMapping(value = "/orderByUser")
    //public Result<List<ToCOrderMain>> queryByUserId(@RequestParam(name = "userNo", required = true) String userNo) {
    //    Result<List<ToCOrderMain>> result = new Result<List<ToCOrderMain>>();
    //    Map<String,String> param = new HashMap<>();
    //    param.put("userId", userNo);
    //    List<ToCOrderMain> toCOrderMains = toCOrderMainMapper.findByUserId(param);
    //    if (BaseUtil.Base_HasValue(toCOrderMains)) {
    //        for (int i = 0; i < toCOrderMains.size(); i++) {
    //            toCOrderMains.get(i).changeMoneyToBig();
    //        }
    //    }
    //    result.setResult(toCOrderMains);
    //    result.setSuccess(true);
    //    return result;
    //}
    //
    //@AutoLog(value = "订单表-用户确认已支付")
    //@ApiOperation(value = "订单表-用户确认已支付", notes = "订单表-用户确认已支付")
    //@PostMapping(value = "/userPay")
    //@Transactional
    //public Result<Object> userPay(HttpServletRequest request, @RequestParam(name = "orderId", required = true) String orderId
    //        , @RequestParam(name = "voucher", required = true) String voucher) {
    //    Result<Object> result = new Result<Object>();
    //    try {
    //        result = orderMainService.userPayConfirm(orderId,voucher);
    //        return result;
    //    } catch (Exception e) {
    //        log.error(e.getMessage(), e);
    //        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    //        result.error500("操作失败");
    //    }
    //    return result;
    //}
    //
    //@AutoLog(value = "订单表-承兑商确认已收款")
    //@ApiOperation(value = "订单表-承兑商确认已收款", notes = "订单表-承兑商确认已收款")
    //@PostMapping(value = "/assurerCollection")
    //@Transactional
    //public Result<Object> assurerCollection(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
    //    Result<Object> result = new Result<Object>();
    //    try {
    //        result = orderMainService.assurerCollectionConfirm(jsonObject.getString("orderIds"));
    //        return result;
    //    } catch (Exception e) {
    //        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    //        log.error(e.getMessage(), e);
    //        result.error500("操作失败");
    //    }
    //    return result;
    //}
    //
    //@AutoLog(value = "订单表-承兑商确认已兑付")
    //@ApiOperation(value = "订单表-承兑商确认已兑付", notes = "订单表-承兑商确认已兑付")
    //@PostMapping(value = "/assurerPay")
    //@Transactional
    //public Result<Object> assurerPay(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
    //    Result<Object> result = new Result<Object>();
    //    try {
    //        result = orderMainService.assurerPayConfirm(jsonObject.getString("orderId"),jsonObject.getString("fileList"));
    //        return result;
    //    } catch (Exception e) {
    //        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    //        log.error(e.getMessage(), e);
    //        result.error500("操作失败");
    //    }
    //    return result;
    //}
    //
    //@AutoLog(value = "用户确认已收款-订单完成")
    //@ApiOperation(value = "用户确认已收款-订单完成", notes = "用户确认已收款-订单完成")
    //@PostMapping(value = "/userCollection")
    //@Transactional
    //public Result<Object> userCollection(HttpServletRequest request, @RequestParam(name = "orderId", required = true) String orderId) {
    //    Result<Object> result = new Result<Object>();
    //    try {
    //        result = orderMainService.userCollectionConfirm(orderId);
    //        return result;
    //    } catch (Exception e) {
    //        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    //        log.error(e.getMessage(), e);
    //        result.error500("操作失败");
    //    }
    //    return result;
    //}
    //
    //@AutoLog(value = "订单表-通过id查询")
    //@ApiOperation(value = "订单表-通过id查询", notes = "订单表-通过id查询")
    //@PostMapping(value = "/order")
    //public Result<ToCOrderMain> queryById(@RequestParam(name = "id", required = true) String id) {
    //    Result<ToCOrderMain> result = new Result<ToCOrderMain>();
    //    ToCOrderMain toCOrderMain = orderMainService.getById(id);
    //    if (toCOrderMain == null) {
    //        result.error500("未找到对应实体");
    //    } else {
    //        toCOrderMain.changeMoneyToBig();
    //        result.setResult(toCOrderMain);
    //        result.setSuccess(true);
    //    }
    //    return result;
    //}
    //
    //@AutoLog(value = "订单表-通过OrderNo查询")
    //@ApiOperation(value = "订单表-通过OrderNo查询", notes = "订单表-通过OrderNo查询")
    //@PostMapping(value = "/queryByOrderNo")
    //public Result<ToCOrderMain> queryByOrderNo(@RequestParam(name = "orderNo", required = true) String orderNo) {
    //    Result<ToCOrderMain> result = new Result<ToCOrderMain>();
    //    ToCOrderMain toCOrderMain = toCOrderMainMapper.queryByOrderNo(orderNo);
    //    if (toCOrderMain == null) {
    //        result.error500("未找到对应实体");
    //    } else {
    //        toCOrderMain.changeMoneyToBig();
    //        result.setResult(toCOrderMain);
    //        result.setSuccess(true);
    //    }
    //    return result;
    //}

}
