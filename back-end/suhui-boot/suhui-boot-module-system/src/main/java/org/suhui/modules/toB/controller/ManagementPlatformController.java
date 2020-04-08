package org.suhui.modules.toB.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.suhui.common.api.vo.Result;
import org.suhui.modules.toB.entity.OrderAssurer;
import org.suhui.modules.toB.service.IOrderAssurerService;
import org.suhui.modules.toB.entity.OrderMerchant;
import org.suhui.modules.toB.service.IOrderMerchantService;
import org.suhui.modules.utils.BaseUtil;

/**
 * 类说明：管理平台业务
 *
 * @author: 蔡珊珊
 * @create: 2020-04-07 22:14
 **/

@RestController
@RequestMapping("/managementPlatform")
@Slf4j
@Api(tags = "管理平台业务",description="接口说明")
public class ManagementPlatformController {
    @Autowired
    private IOrderAssurerService orderAssurerService;
    @Autowired
    private IOrderMerchantService orderMerchantService;

    /**
     * 添加承兑商
     *
     * @param
     * @return
     */
    @PostMapping(value = "/addAcceptor")
    @ApiOperation(value = "添加承兑商", notes = "添加承兑商", response = OrderAssurer.class)
    public Result<OrderAssurer> addAssurer(@ApiParam(value = "承兑商对象", required = true) @RequestBody JSONObject jsonObject) {
        Result<OrderAssurer> result = new Result<OrderAssurer>();
        OrderAssurer orderAssurer = orderAssurerService.addAssurerMain(jsonObject);
        if (!BaseUtil.Base_HasValue(orderAssurer)) {
            result.error500("操作失败");
            return result;
        }
        result.success("添加成功！");
        result.setResult(orderAssurer);
        return result;
    }

    /**
     * 添加商户
     *
     * @param
     * @return
     */
    @PostMapping(value = "/addMerchant")
    public Result<OrderMerchant> addMerchant(@RequestBody JSONObject jsonObject) {
        Result<OrderMerchant> result = new Result<OrderMerchant>();
        OrderMerchant orderMerchant = orderMerchantService.addMerchant(jsonObject);
        if (!BaseUtil.Base_HasValue(orderMerchant)) {
            result.error500("操作失败");
            return result;
        }
        result.success("添加成功！");
        result.setResult(orderMerchant);
        return result;
    }
}
