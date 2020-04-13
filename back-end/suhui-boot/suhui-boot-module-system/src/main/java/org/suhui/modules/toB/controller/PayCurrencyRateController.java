package org.suhui.modules.toB.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.suhui.common.api.vo.Result;
import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.modules.toB.service.IPayCurrencyRateService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * 类说明：汇率相关
 *
 * @author: 蔡珊珊
 * @create: 2020-04-12 15:26
 **/
@Slf4j
@Api(tags = "货币种类")
@RestController
@RequestMapping("/payCurrencyRate")
public class PayCurrencyRateController {
    @Autowired
    private IPayCurrencyRateService iPayCurrencyRateService;

    @AutoLog(value = "根据目标货币费率，计算需要支付的源货币")
    @ApiOperation(value = "根据目标货币费率，计算需要支付的源货币", notes = "根据目标货币费率，计算需要支付的源货币")
    @GetMapping(value = "/getCurrencyRateByRateCode")
    public Result<JSONObject> getCurrencyRateByRateCode(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params) {
        return iPayCurrencyRateService.getCurrencyRateValue(params);
    }
}
