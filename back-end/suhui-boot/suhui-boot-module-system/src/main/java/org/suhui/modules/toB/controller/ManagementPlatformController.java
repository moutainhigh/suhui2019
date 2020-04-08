package org.suhui.modules.toB.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.suhui.common.api.vo.Result;
import org.suhui.common.system.query.QueryGenerator;
import org.suhui.modules.toB.entity.OrderAssurer;
import org.suhui.modules.toB.service.IOrderAssurerService;
import org.suhui.modules.toB.entity.OrderMerchant;
import org.suhui.modules.toB.service.IOrderMerchantService;
import org.suhui.modules.utils.BaseUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
     * 承兑商 批量审核
     *
     * @param
     * @return
     */
    @PostMapping(value = "/auditPassAssurer")
    public Result<Object> auditPassAssurer(@RequestBody JSONObject jsonObject) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderAssurerService.auditPassAssurer(jsonObject.getString("assurerIds"));
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 更改 保证金、租赁金
     *
     * @param
     * @return
     */
    @PostMapping(value = "/changeAssurerMoney")
    @Transactional
    public Result<Object> changeAssurerMoney(@RequestBody JSONObject data) {
        Result<Object> result = new Result<Object>();
        result = orderAssurerService.changeAssurerMoney(data);
        return result;
    }

    /**
     * 分页列表查询
     *
     * @param orderAssurer
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/assurerList")
    public Result<IPage<OrderAssurer>> queryPageList(OrderAssurer orderAssurer,
                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                     HttpServletRequest req) {
        Result<IPage<OrderAssurer>> result = new Result<IPage<OrderAssurer>>();
        QueryWrapper<OrderAssurer> queryWrapper = QueryGenerator.initQueryWrapper(orderAssurer, req.getParameterMap());
        Page<OrderAssurer> page = new Page<OrderAssurer>(pageNo, pageSize);
        IPage<OrderAssurer> pageList = orderAssurerService.page(page, queryWrapper);
        List<OrderAssurer> orderAssurers = pageList.getRecords();
        if (BaseUtil.Base_HasValue(orderAssurers)) {
            for (int i = 0; i < orderAssurers.size(); i++) {
                orderAssurers.get(i).changeMoneyToBig();
            }
        }
        result.setSuccess(true);
        result.setResult(pageList);
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

    /**
     * 商户 批量审核
     *
     * @param
     * @return
     */
    @PostMapping(value = "/auditPassMerchant")
    public Result<Object> auditPassMerchant(@RequestBody JSONObject jsonObject) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderMerchantService.auditPassMerchant(jsonObject.getString("merchantIds"));
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 更改 保证金、租赁金
     *
     * @param
     * @return
     */
    @PostMapping(value = "/changeAssurerMoney")
    @Transactional
    public Result<Object> changeMerchantMoney(@RequestBody JSONObject data) {
        Result<Object> result = new Result<Object>();
        result = orderMerchantService.changeMerchantMoney(data);
        return result;
    }

    /**
     * 分页列表查询
     *
     * @param orderAssurer
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/assurerList")
    public Result<IPage<OrderMerchant>> queryPageList(OrderMerchant orderMerchant,
                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                     HttpServletRequest req) {
        Result<IPage<OrderMerchant>> result = new Result<IPage<OrderMerchant>>();
        QueryWrapper<OrderMerchant> queryWrapper = QueryGenerator.initQueryWrapper(orderMerchant, req.getParameterMap());
        Page<OrderMerchant> page = new Page<OrderMerchant>(pageNo, pageSize);
        IPage<OrderMerchant> pageList = orderMerchantService.page(page, queryWrapper);
        List<OrderMerchant> orderMerchants = pageList.getRecords();
        if (BaseUtil.Base_HasValue(orderMerchants)) {
            for (int i = 0; i < orderMerchants.size(); i++) {
                orderMerchants.get(i).changeMoneyToBig();
            }
        }
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }
}
