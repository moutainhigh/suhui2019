package org.suhui.modules.toB.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import org.suhui.common.system.query.QueryGenerator;
import org.suhui.modules.toB.entity.OrderMerchantMoneyChange;
import org.suhui.modules.toB.service.IOrderMerchantMoneyChangeService;

import javax.servlet.http.HttpServletRequest;

/**
 * 类说明：商户金额变动
 *
 * @author: 蔡珊珊
 * @create: 2020-04-08 22:51
 **/
@Slf4j
@Api(tags = "承兑商/商户平台")
@RestController
@RequestMapping("/merchantMoneyChange")
public class OrderMerchantMoneyChangeController {
    @Autowired
    private IOrderMerchantMoneyChangeService orderMerchantMoneyChangeService;

    @AutoLog(value = "商户金额变动-分页列表")
    @ApiOperation(value = "商户金额变动-分页列表", notes = "商户金额变动-分页列表")
    @GetMapping(value = "/list")
    public Result<IPage<OrderMerchantMoneyChange>> queryPageList(OrderMerchantMoneyChange orderMerchantMoneyChange,
                                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                HttpServletRequest req) {
        Result<IPage<OrderMerchantMoneyChange>> result = new Result<IPage<OrderMerchantMoneyChange>>();
        QueryWrapper<OrderMerchantMoneyChange> queryWrapper = QueryGenerator.initQueryWrapper(orderMerchantMoneyChange, req.getParameterMap());
        Page<OrderMerchantMoneyChange> page = new Page<OrderMerchantMoneyChange>(pageNo, pageSize);
        IPage<OrderMerchantMoneyChange> pageList = orderMerchantMoneyChangeService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

}
