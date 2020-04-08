package org.suhui.modules.toB.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.suhui.common.api.vo.Result;
import org.suhui.modules.toB.entity.OrderAssurer;
import org.suhui.modules.toB.service.IOrderAssurerService;

/**
 * 类说明：承兑商业务
 *
 * @author: 蔡珊珊
 * @create: 2020-04-07 22:11
 **/
@RestController
@RequestMapping("/assurer")
@Slf4j
public class AssurerController {
    @Autowired
    private IOrderAssurerService orderAssurerService;

    /**
     * 承兑商基本信息（userNo）
     *
     * @param
     * @return
     */
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
