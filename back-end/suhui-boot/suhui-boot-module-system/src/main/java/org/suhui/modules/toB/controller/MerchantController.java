package org.suhui.modules.toB.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.suhui.common.api.vo.Result;
import org.suhui.modules.toB.entity.OrderAssurer;
import org.suhui.modules.toB.entity.OrderMerchant;
import org.suhui.modules.toB.service.IOrderAssurerService;
import org.suhui.modules.toB.service.IOrderMerchantService;

/**
 * 类说明：商户业务
 *
 * @author: 蔡珊珊
 * @create: 2020-04-07 22:55
 **/
@RestController
@RequestMapping("/merchant")
@Slf4j
public class MerchantController {
    @Autowired
    private IOrderMerchantService orderMerchantService;

    /**
     * 商户基本信息（userNo）
     *
     * @param
     * @return
     */
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

