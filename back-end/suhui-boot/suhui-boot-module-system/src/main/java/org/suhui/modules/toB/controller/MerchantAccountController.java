package org.suhui.modules.toB.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.suhui.common.api.vo.Result;
import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.modules.toB.entity.OrderMerchantAccount;
import org.suhui.modules.toB.service.IOrderMerchantAccountService;

import javax.servlet.http.HttpServletRequest;

/**
 * 类说明：商户账户相关
 *
 * @author: 蔡珊珊
 * @create: 2020-04-17 10:46
 */
@Slf4j
@Api(tags="商户账户")
@RestController
@RequestMapping("/merchant/account")
public class MerchantAccountController {
    @Autowired
    public HttpServletRequest request;
    @Autowired
    private IOrderMerchantAccountService orderMerchantAccountService;

    /**
     *   添加
     * @param
     * @return
     */
    @AutoLog(value = "添加")
    @ApiOperation(value = "添加", notes = "添加")
    @PostMapping(value = "/add")
    public Result<OrderMerchantAccount> add(@RequestBody OrderMerchantAccount orderMerchant) {
        Result<OrderMerchantAccount> result = new Result<OrderMerchantAccount>();
        try {
            OrderMerchantAccount orderMerchantAccount = new OrderMerchantAccount();
            BeanUtils.copyProperties(orderMerchant, orderMerchantAccount);
            orderMerchantAccountService.save(orderMerchantAccount);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
        }
        return result;
    }

}
