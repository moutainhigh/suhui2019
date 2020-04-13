package org.suhui.modules.toB.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.suhui.common.api.vo.Result;
import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.modules.toB.entity.OrderAssurer;
import org.suhui.modules.toB.service.IOrderAssurerService;

/**
 * 类说明：承兑商业务
 *
 * @author: 蔡珊珊
 * @create: 2020-04-07 22:11
 **/
@Slf4j
@Api(tags="承兑商/商户平台")
@RestController
@RequestMapping("/assurer")
public class AssurerController {
    @Autowired
    private IOrderAssurerService orderAssurerService;

    @AutoLog(value = "承兑商-根据userNo获取基本信息")
    @ApiOperation(value = "承兑商-根据userNo获取基本信息", notes = "承兑商-根据userNo获取基本信息")
    @PostMapping(value = "/queryAssurerByUserNo")
    public Result<OrderAssurer> queryAssurerByUserNo(@RequestBody OrderAssurer data) {
        Result<OrderAssurer> result = new Result<OrderAssurer>();
        OrderAssurer orderAssurer = orderAssurerService.getAssurerByUserNo(data.getUserNo());
        if (orderAssurer == null) {
            result.error500("该承兑商不存在");
        } else {
            orderAssurer.changeMoneyToBig();
            result.setResult(orderAssurer);
            result.setSuccess(true);
            result.setCode(200);
        }
        return result;
    }
}
