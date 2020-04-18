package org.suhui.modules.toB.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.suhui.common.api.vo.Result;
import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.common.util.MD5Util;
import org.suhui.modules.toB.entity.OrderMain;
import org.suhui.modules.toB.mapper.OrderMainMapper;
import org.suhui.modules.toB.service.IOrderMainService;
import org.suhui.modules.toB.service.IPayCurrencyRateService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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
    @Autowired
    private IPayCurrencyRateService iPayCurrencyRateService;

    @AutoLog(value = "创建收款订单")
    @ApiOperation(value = "创建收款订单", notes = "创建收款订单")
    @PostMapping(value = "/createPaymentOrder")
    @Transactional
    public Result<Object> createPaymentOrder(@ApiParam(value = "商户编号")
                                             @RequestParam(name = "userNo") String userNo,
                                             @ApiParam(value = "商户联系电话")
                                             @RequestParam(name = "merchantContact") String merchantContact,
                                             @ApiParam(value = "源货币")
                                             @RequestParam(name = "sourceCurrency") String sourceCurrency,
                                             @ApiParam(value = "目标货币")
                                             @RequestParam(name = "targetCurrency") String targetCurrency,
                                             @ApiParam(value = "目标货币金额")
                                             @RequestParam(name = "targetCurrencyMoney") String targetCurrencyMoney,
                                             @ApiParam(value = "支付方式")
                                             @RequestParam(name = "payMethod", defaultValue = "bank_card") String payMethod,
                                             @ApiParam(value = "订单描述")
                                             @RequestParam(name = "orderText") String orderText,
                                             @ApiParam(value = "回调地址")
                                             @RequestParam(name = "notifyUrl") String notifyUrl,
                                             @ApiParam(value = "sign")
                                             @RequestParam(name = "sign") String sign,
                                             @ApiParam(value = "timestamp")
                                             @RequestParam(name = "timestamp") String timestamp
    ) {
        Result<Object> result = new Result<Object>();
        OrderMain orderMain = new OrderMain();
        orderMain.setMerchantContact(merchantContact);
        orderMain.setSourceCurrency(sourceCurrency);
        orderMain.setTargetCurrency(targetCurrency);
        orderMain.setTargetCurrencyMoney(Double.parseDouble(targetCurrencyMoney));
        orderMain.setUserPayMethod(payMethod);
        orderMain.setOrderText(orderText);
        orderMain.setNotifyUrl(notifyUrl);
        try {
            result = orderMainService.createPaymentOrder(orderMain, userNo);
            return result;
        } catch (Exception ex) {
            log.error("异常：创建收款订单", ex);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value = "确认已收款")
    @ApiOperation(value = "确认已收款", notes = "确认已收款")
    @PostMapping(value = "/confirmPaymentOrder")
    @Transactional
    public Result<Object> confirmPaymentOrder(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderMainService.confirmPaymentOrder(jsonObject.getString("orderNos"));
            return result;
        } catch (Exception ex) {
            log.error("异常：平台确认已收款", ex);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
    @PostMapping(value = "/createWithdrawalOrder")
    @Transactional
    public Result<Object> createWithdrawalOrder(
            @ApiParam(value = "商户编号")
            @RequestParam(name = "userNo") String userNo,
            @ApiParam(value = "商户电话")
            @RequestParam(name = "merchantContact") String merchantContact,
            @ApiParam(value = "源货币")
            @RequestParam(name = "sourceCurrency") String sourceCurrency,
            @ApiParam(value = "目标货币")
            @RequestParam(name = "targetCurrency") String targetCurrency,
            @ApiParam(value = "目标金额")
            @RequestParam(name = "targetCurrencyMoney") String targetCurrencyMoney,
            @ApiParam(value = "提款方式")
            @RequestParam(name = "collectionMethod", defaultValue = "bank_card") String collectionMethod,
            @ApiParam(value = "提款账户")
            @RequestParam(name = "collectionAccount") String collectionAccount,
            @ApiParam(value = "提款账户真实姓名")
            @RequestParam(name = "collectionAccountUser") String collectionAccountUser,
            @ApiParam(value = "订单描述")
            @RequestParam(name = "orderText") String orderText,
            @ApiParam(value = "回调地址")
            @RequestParam(name = "notifyUrl") String notifyUrl,
            @ApiParam(value = "sign")
            @RequestParam(name = "sign") String sign,
            @ApiParam(value = "timestamp")
            @RequestParam(name = "timestamp") String timestamp
    ) {
        Result<Object> result = new Result<Object>();
        OrderMain orderMain = new OrderMain();
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
            result = orderMainService.createWithdrawalOrder(orderMain, userNo);
            return result;
        } catch (Exception ex) {
            log.error("异常：创建提款订单", ex);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value = "确认已处理")
    @ApiOperation(value = "确认已处理", notes = "确认已处理")
    @PostMapping(value = "/confirmWithdrawalOrder")
    @Transactional
    public Result<Object> confirmWithdrawalOrder(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderMainService.confirmWithdrawalOrder(jsonObject.getString("orderNos"));
            return result;
        } catch (Exception ex) {
            log.error("异常：提款单-确认已处理", ex);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error500("操作失败");
        }
        return result;
    }

    @AutoLog(value = "获取当前汇率（根据源货币和目标货币）")
    @ApiOperation(value = "获取当前汇率（根据源货币和目标货币）", notes = "获取当前汇率（根据源货币和目标货币）")
    @PostMapping(value = "/getCurrencyRateByRateCode")
    public Result<JSONObject> getCurrencyRateByRateCode(
            @ApiParam(value = "源货币")
            @RequestParam(name = "sourceCurrency") String sourceCurrency,
            @ApiParam(value = "目标货币")
            @RequestParam(name = "targetCurrency") String targetCurrency) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("source_currency_code", sourceCurrency);
        params.put("targetCurrency", targetCurrency);
        return iPayCurrencyRateService.getCurrencyRateValue(params);
    }

    @Value("${appSecret}")
    private String appSecret;

    @AutoLog(value = "md5SignTest")
    @ApiOperation(value = "md5SignTest）", notes = "md5SignTest")
    @PostMapping(value = "/md5SignTest")
    public String md5SignTest() {
//        try {
        HashMap<String, String> map = new HashMap<>();
        map.put("userNo", "1");
        map.put("merchantContact", "1");
        map.put("sourceCurrency", "1");
        map.put("targetCurrency", "1");
        map.put("targetCurrencyMoney", "1");
        map.put("payMethod", "1");
        map.put("orderText", "1");
        map.put("notifyUrl", "1");
        map.put("timestamp", "1");

        String sign = MD5Util.sign(map, appSecret);
        return sign;
//            RestTemplate restTemplate = new RestTemplate();
//            String url = "http://localhost:3333/order/createPaymentOrder";
//            HttpHeaders headers = new HttpHeaders();
//            MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
//            multiValueMap.add("userNo", "");
//            multiValueMap.add("merchantContact", "");
//            multiValueMap.add("sourceCurrency", "");
//            multiValueMap.add("targetCurrency", "");
//            multiValueMap.add("targetCurrencyMoney", "");
//            multiValueMap.add("payMethod", "");
//            multiValueMap.add("orderText", "");
//            multiValueMap.add("notifyUrl", "");
//            multiValueMap.add("sign", sign);
//            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(multiValueMap, headers);
//            ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, request, JSONObject.class);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return Result.error(ex+"");
//        }
//        return Result.ok("成功");
    }

}
