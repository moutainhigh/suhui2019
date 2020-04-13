package org.suhui.modules.toB.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.suhui.common.api.vo.Result;
import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.modules.toB.entity.OrderAssurer;
import org.suhui.modules.toB.entity.OrderMerchant;
import org.suhui.modules.toB.service.IOrderAssurerService;
import org.suhui.modules.toB.service.IOrderMerchantService;
import org.suhui.modules.utils.BaseUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 类说明：商户业务
 *
 * @author: 蔡珊珊
 * @create: 2020-04-07 22:55
 **/
@Slf4j
@Api(tags="承兑商/商户平台")
@RestController
@RequestMapping("/merchant")
public class MerchantController {
    @Autowired
    private IOrderMerchantService orderMerchantService;

    @AutoLog(value = "商户-根据userNo获取基本信息")
    @ApiOperation(value = "商户-根据userNo获取基本信息", notes = "商户-根据userNo获取基本信息")
    @PostMapping(value = "/queryMerchantByUserNo")
    public Result<OrderMerchant> queryMerchantByUserNo(@RequestBody OrderMerchant data) {
        Result<OrderMerchant> result = new Result<OrderMerchant>();
        OrderMerchant orderMerchant = orderMerchantService.getMerchantByUserNo(data.getUserNo());
        if (orderMerchant == null) {
            result.error500("该承兑商不存在");
        } else {
            orderMerchant.changeMoneyToBig();
            result.setResult(orderMerchant);
            result.setSuccess(true);
            result.setCode(200);
        }
        return result;
    }
}

