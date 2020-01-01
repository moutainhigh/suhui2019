package org.suhui.modules.order.controller;

import java.util.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.suhui.common.api.vo.Result;
import org.suhui.common.system.query.QueryGenerator;
import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.order.entity.OrderMain;
import org.suhui.modules.order.service.IOrderMainService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.suhui.modules.utils.BaseUtil;

/**
 * @Description: 订单
 * @Author: jeecg-boot
 * @Date: 2019-12-29
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "订单表")
@RestController
@RequestMapping("/order/orderMain")
public class OrderMainController {
    @Autowired
    private IOrderMainService orderMainService;

    /**
     * 分页列表查询
     *
     * @param orderMain
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "订单表-分页列表查询")
    @ApiOperation(value = "订单表-分页列表查询", notes = "订单表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<OrderMain>> queryPageList(OrderMain orderMain,
                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                  HttpServletRequest req) {
        Result<IPage<OrderMain>> result = new Result<IPage<OrderMain>>();
        QueryWrapper<OrderMain> queryWrapper = QueryGenerator.initQueryWrapper(orderMain, req.getParameterMap());
        Page<OrderMain> page = new Page<OrderMain>(pageNo, pageSize);
        IPage<OrderMain> pageList = orderMainService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 创建订单-app
     */
    @AutoLog(value = "创建订单-app")
    @ApiOperation(value = "创建订单-app", notes = "创建订单-app")
    @PostMapping(value = "/add")
    public Result<Object> add(HttpServletRequest request, @RequestBody OrderMain orderMain) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderMainService.manageOrderByAuto(orderMain);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 订单分配承兑商-后台
     */
    @AutoLog(value = "订单分配承兑商-后台")
    @ApiOperation(value = "订单分配承兑商-后台", notes = "订单分配承兑商-后台")
    @PostMapping(value = "/dispatchOrder")
    public Result<Object> dispatchOrder(HttpServletRequest request,
                              @RequestParam(name = "orderId", required = true) String orderId,
                              @RequestParam(name = "assurerId", required = true) String assurerId) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderMainService.dispatchOrderAdmin(orderId,assurerId);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 用户确认已支付
     */
    @AutoLog(value = "用户确认已支付")
    @ApiOperation(value = "用户确认已支付", notes = "用户确认已支付")
    @PostMapping(value = "/userPay")
    public Result<Object> userPay(HttpServletRequest request, @RequestParam(name = "orderId", required = true) String orderId) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderMainService.userPayConfirm(orderId);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }


    /**
     * 承兑商确认已收款
     */
    @AutoLog(value = "承兑商确认已收款")
    @ApiOperation(value = "承兑商确认已收款", notes = "承兑商确认已收款")
    @PostMapping(value = "/assurerCollection")
    public Result<Object> assurerCollection(HttpServletRequest request, @RequestParam(name = "orderId", required = true) String orderId) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderMainService.assurerCollectionConfirm(orderId);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 承兑商确认已兑付
     */
    @AutoLog(value = "承兑商确认已兑付")
    @ApiOperation(value = "承兑商确认已兑付", notes = "承兑商确认已兑付")
    @PostMapping(value = "/assurerPay")
    public Result<Object> assurerPay(HttpServletRequest request, @RequestParam(name = "orderId", required = true) String orderId) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderMainService.assurerPayConfirm(orderId);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }


    /**
     * 用户确认已收款-订单完成
     */
    @AutoLog(value = "用户确认已收款-订单完成")
    @ApiOperation(value = "用户确认已收款-订单完成", notes = "用户确认已收款-订单完成")
    @PostMapping(value = "/userCollection")
    public Result<Object> userCollection(HttpServletRequest request, @RequestParam(name = "orderId", required = true) String orderId) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderMainService.userCollectionConfirm(orderId);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 通过源货币和目标货币获取汇率计算金额
     */
    @RequestMapping(value = "/getUserPayMoney", method = RequestMethod.POST)
    public Result<JSONObject> getUserPayMoney(HttpServletRequest request,
                                              @RequestParam(name = "sourceCurrencyCode", required = true) String sourceCurrencyCode,
                                              @RequestParam(name = "targetCurrencyCode", required = true) String targetCurrencyCode,
                                              @RequestParam(name = "targetCurrencyMoney", required = true) String targetCurrencyMoney) {
        Result<JSONObject> result = new Result<JSONObject>();
        String accessToken = request.getHeader("X-Access-Token");
        JSONObject value = orderMainService.getUserPayMoney(sourceCurrencyCode, targetCurrencyCode, targetCurrencyMoney, accessToken);
        if (!BaseUtil.Base_HasValue(value)) {
            value.put("message", "this rate is not in database");
            result.setResult(value);
            result.setCode(515);
            return result;
        }
        result.setResult(value);
        result.setCode(200);
        return result;

    }


    /**
     * 编辑
     *
     * @param orderMain
     * @return
     */
    @AutoLog(value = "订单表-编辑")
    @ApiOperation(value = "订单表-编辑", notes = "订单表-编辑")
    @PutMapping(value = "/edit")
    public Result<OrderMain> edit(@RequestBody OrderMain orderMain) {
        Result<OrderMain> result = new Result<OrderMain>();
        OrderMain orderMainEntity = orderMainService.getById(orderMain.getId());
        if (orderMainEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = orderMainService.updateById(orderMain);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
            }
        }

        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "订单表-通过id删除")
    @ApiOperation(value = "订单表-通过id删除", notes = "订单表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<OrderMain> delete(@RequestParam(name = "id", required = true) String id) {
        Result<OrderMain> result = new Result<OrderMain>();
        OrderMain orderMain = orderMainService.getById(id);
        if (orderMain == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = orderMainService.removeById(id);
            if (ok) {
                result.success("删除成功!");
            }
        }

        return result;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "订单表-批量删除")
    @ApiOperation(value = "订单表-批量删除", notes = "订单表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<OrderMain> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<OrderMain> result = new Result<OrderMain>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.orderMainService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "订单表-通过id查询")
    @ApiOperation(value = "订单表-通过id查询", notes = "订单表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<OrderMain> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<OrderMain> result = new Result<OrderMain>();
        OrderMain orderMain = orderMainService.getById(id);
        if (orderMain == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(orderMain);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
        // Step.1 组装查询条件
        QueryWrapper<OrderMain> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                OrderMain orderMain = JSON.parseObject(deString, OrderMain.class);
                queryWrapper = QueryGenerator.initQueryWrapper(orderMain, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<OrderMain> pageList = orderMainService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "订单表列表");
        mv.addObject(NormalExcelConstants.CLASS, OrderMain.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("订单表列表数据", "导出人:Jeecg", "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<OrderMain> listOrderMains = ExcelImportUtil.importExcel(file.getInputStream(), OrderMain.class, params);
                for (OrderMain orderMainExcel : listOrderMains) {
                    orderMainService.save(orderMainExcel);
                }
                return Result.ok("文件导入成功！数据行数:" + listOrderMains.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.ok("文件导入失败！");
    }

}
