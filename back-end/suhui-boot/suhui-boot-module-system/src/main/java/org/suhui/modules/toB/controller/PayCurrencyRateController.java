package org.suhui.modules.toB.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.suhui.common.api.vo.Result;
import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.modules.toB.service.IPayCurrencyRateService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @AutoLog(value = "获取当前汇率（根据源货币和目标货币）")
    @ApiOperation(value = "获取当前汇率（根据源货币和目标货币）", notes = "获取当前汇率（根据源货币和目标货币）")
    @GetMapping(value = "/getCurrencyRateByRateCode")
    public Result<JSONObject> getCurrencyRateByRateCode(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params) {
        return iPayCurrencyRateService.getCurrencyRateValue(params);
    }
}
