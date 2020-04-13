package org.suhui.modules.toB.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.suhui.common.api.vo.Result;
import org.suhui.modules.toB.entity.OrderAssurerAccount;
import org.suhui.modules.toB.service.IOrderAssurerAccountService;

/**
 * 类说明：承兑商账户明细
 *
 * @author: 蔡珊珊
 * @create: 2020-04-10 9:12
 */
@Slf4j
@Api(tags="账户类型")
@RestController
@RequestMapping("/assurer/account")
public class AssurerAccountController {
    @Autowired
    private IOrderAssurerAccountService orderAssurerAccountService;

    /**
     *   添加
     * @param orderAssurerPage
     * @return
     */
    @PostMapping(value = "/add")
    public Result<OrderAssurerAccount> add(@RequestBody OrderAssurerAccount orderAssurerPage) {
        Result<OrderAssurerAccount> result = new Result<OrderAssurerAccount>();
        try {
            OrderAssurerAccount OrderAssurerAccount = new OrderAssurerAccount();
            BeanUtils.copyProperties(orderAssurerPage, OrderAssurerAccount);
            orderAssurerAccountService.save(OrderAssurerAccount);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
        }
        return result;
    }
}
