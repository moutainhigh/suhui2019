package org.suhui.modules.order.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.suhui.common.api.vo.Result;
import org.suhui.common.system.query.QueryGenerator;
import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.order.entity.OrderAssurerPaySingle;
import org.suhui.modules.order.service.IOrderAssurerPaySingleService;

import java.util.Date;

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

/**
 * @Description: 缴费单管理
 * @Author: jeecg-boot
 * @Date: 2020-02-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "缴费单管理")
@RestController
@RequestMapping("/order/PaySingle")
public class OrderAssurerPaySingleController {
    @Autowired
    private IOrderAssurerPaySingleService orderAssurerPaySingleService;


    @PostMapping(value = "/changeState")
    @Transactional
    public Result<Object> changeState(@RequestBody JSONObject jsonObject) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderAssurerPaySingleService.changeSingleState(jsonObject.getString("ids"),jsonObject.getString("state"));
            return result;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error500("操作失败");
        }
        return result;
    }


    /**
     * 分页列表查询
     *
     * @param orderAssurerPaySingle
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "缴费单管理-分页列表查询")
    @ApiOperation(value = "缴费单管理-分页列表查询", notes = "缴费单管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<OrderAssurerPaySingle>> queryPageList(OrderAssurerPaySingle orderAssurerPaySingle,
                                                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                              HttpServletRequest req) {
        Result<IPage<OrderAssurerPaySingle>> result = new Result<IPage<OrderAssurerPaySingle>>();
        QueryWrapper<OrderAssurerPaySingle> queryWrapper = QueryGenerator.initQueryWrapper(orderAssurerPaySingle, req.getParameterMap());
        Page<OrderAssurerPaySingle> page = new Page<OrderAssurerPaySingle>(pageNo, pageSize);
        IPage<OrderAssurerPaySingle> pageList = orderAssurerPaySingleService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param orderAssurerPaySingle
     * @return
     */
    @AutoLog(value = "缴费单管理-添加")
    @ApiOperation(value = "缴费单管理-添加", notes = "缴费单管理-添加")
    @PostMapping(value = "/add")
    public Result<OrderAssurerPaySingle> add(@RequestBody OrderAssurerPaySingle orderAssurerPaySingle) {
        Result<OrderAssurerPaySingle> result = new Result<OrderAssurerPaySingle>();
        try {
            orderAssurerPaySingleService.save(orderAssurerPaySingle);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑
     *
     * @param orderAssurerPaySingle
     * @return
     */
    @AutoLog(value = "缴费单管理-编辑")
    @ApiOperation(value = "缴费单管理-编辑", notes = "缴费单管理-编辑")
    @PutMapping(value = "/edit")
    public Result<OrderAssurerPaySingle> edit(@RequestBody OrderAssurerPaySingle orderAssurerPaySingle) {
        Result<OrderAssurerPaySingle> result = new Result<OrderAssurerPaySingle>();
        OrderAssurerPaySingle orderAssurerPaySingleEntity = orderAssurerPaySingleService.getById(orderAssurerPaySingle.getId());
        if (orderAssurerPaySingleEntity == null) {
            result.error500("未找到对应实体");
        } else {
            if(!orderAssurerPaySingleEntity.getPaySingleState().equals("pass")){
                boolean ok = orderAssurerPaySingleService.updateById(orderAssurerPaySingle);
                if (ok) {
                    result.success("修改成功!");
                }
            }else{
                result.error500("审核通过的缴费单不可进行修改");
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
    @AutoLog(value = "缴费单管理-通过id删除")
    @ApiOperation(value = "缴费单管理-通过id删除", notes = "缴费单管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<OrderAssurerPaySingle> delete(@RequestParam(name = "id", required = true) String id) {
        Result<OrderAssurerPaySingle> result = new Result<OrderAssurerPaySingle>();
        OrderAssurerPaySingle orderAssurerPaySingle = orderAssurerPaySingleService.getById(id);
        if (orderAssurerPaySingle == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = orderAssurerPaySingleService.removeById(id);
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
    @AutoLog(value = "缴费单管理-批量删除")
    @ApiOperation(value = "缴费单管理-批量删除", notes = "缴费单管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<OrderAssurerPaySingle> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<OrderAssurerPaySingle> result = new Result<OrderAssurerPaySingle>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            String arr[] = ids.split(",");
            for (String id : arr) {
                OrderAssurerPaySingle orderAssurerPaySingle = orderAssurerPaySingleService.getById(id);
                if (!orderAssurerPaySingle.getPaySingleState().equals("pass")) {
                    orderAssurerPaySingle.setDelFlag("1");
                    orderAssurerPaySingleService.updateById(orderAssurerPaySingle);
                }
            }
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
    @AutoLog(value = "缴费单管理-通过id查询")
    @ApiOperation(value = "缴费单管理-通过id查询", notes = "缴费单管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<OrderAssurerPaySingle> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<OrderAssurerPaySingle> result = new Result<OrderAssurerPaySingle>();
        OrderAssurerPaySingle orderAssurerPaySingle = orderAssurerPaySingleService.getById(id);
        if (orderAssurerPaySingle == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(orderAssurerPaySingle);
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
        QueryWrapper<OrderAssurerPaySingle> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                OrderAssurerPaySingle orderAssurerPaySingle = JSON.parseObject(deString, OrderAssurerPaySingle.class);
                queryWrapper = QueryGenerator.initQueryWrapper(orderAssurerPaySingle, request.getParameterMap());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<OrderAssurerPaySingle> pageList = orderAssurerPaySingleService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "缴费单管理列表");
        mv.addObject(NormalExcelConstants.CLASS, OrderAssurerPaySingle.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("缴费单管理列表数据", "导出人:Jeecg", "导出信息"));
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
                List<OrderAssurerPaySingle> listOrderAssurerPaySingles = ExcelImportUtil.importExcel(file.getInputStream(), OrderAssurerPaySingle.class, params);
                for (OrderAssurerPaySingle orderAssurerPaySingleExcel : listOrderAssurerPaySingles) {
                    orderAssurerPaySingleService.save(orderAssurerPaySingleExcel);
                }
                return Result.ok("文件导入成功！数据行数:" + listOrderAssurerPaySingles.size());
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
