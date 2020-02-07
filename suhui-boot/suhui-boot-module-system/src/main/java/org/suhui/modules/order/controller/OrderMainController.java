package org.suhui.modules.order.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.suhui.common.api.vo.Result;
import org.suhui.common.constant.CommonConstant;
import org.suhui.common.system.query.QueryGenerator;
import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.order.entity.OrderMain;
import org.suhui.modules.order.mapper.OrderMainMapper;
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
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
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

    @Autowired
    private OrderMainMapper orderMainMapper;

    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;

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
        List<OrderMain> orderMains = pageList.getRecords();
        if (BaseUtil.Base_HasValue(orderMains)) {
            for(int i=0;i<orderMains.size();i++){
                orderMains.get(i).changeMoneyToBig();
            }
        }
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
    public Result<Object> add(HttpServletRequest request,
                              @RequestParam(name = "userNo") String userNo,
                              @RequestParam(name = "userName") String userName,
                              @RequestParam(name = "userContact") String userContact,
                              @RequestParam(name = "sourceCurrency") String sourceCurrency,
                              @RequestParam(name = "targetCurrency") String targetCurrency,
                              @RequestParam(name = "exchangeRate") String exchangeRate,
                              @RequestParam(name = "targetCurrencyMoney") String targetCurrencyMoney,
                              @RequestParam(name = "userPayMethod") String userPayMethod,
                              @RequestParam(name = "userPayAccount") String userPayAccount,
                              @RequestParam(name = "userPayBank") String userPayBank,
                              @RequestParam(name = "userPayBankBranch") String userPayBankBranch,
                              @RequestParam(name = "userPayAreaCode") String userPayAreaCode) {
        Result<Object> result = new Result<Object>();
        String accessToken = request.getHeader("X-Access-Token");
        OrderMain orderMain = new OrderMain();
        orderMain.setUserNo(userNo);
        orderMain.setUserName(userName);
        orderMain.setUserContact(userContact);
        orderMain.setSourceCurrency(sourceCurrency);
        orderMain.setTargetCurrency(targetCurrency);
        orderMain.setExchangeRate(Double.parseDouble(exchangeRate));
        orderMain.setTargetCurrencyMoney(Double.parseDouble(targetCurrencyMoney));
        orderMain.setUserPayMethod(userPayMethod);
        orderMain.setUserPayAccount(userPayAccount);
        orderMain.setUserPayBank(userPayBank);
        orderMain.setUserPayBankBranch(userPayBankBranch);
        orderMain.setUserPayAreaCode(userPayAreaCode);
        try {
            result = orderMainService.manageOrderByAuto(orderMain,accessToken);
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
    @RequiresPermissions("order:admin")
    public Result<Object> dispatchOrder(HttpServletRequest request,
                                        @RequestBody JSONObject jsonObject) {
        Result<Object> result = new Result<Object>();
        String accessToken = request.getHeader("X-Access-Token");
        try {
            result = orderMainService.dispatchOrderAdmin(jsonObject.getString("orderId"), jsonObject.getString("assurerId"),accessToken);
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
    public Result<Object> userPay(HttpServletRequest request, @RequestParam(name = "orderId", required = true) String orderId
                                    , @RequestParam(name = "voucher", required = true) String voucher) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderMainService.userPayConfirm(orderId,voucher);
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
    public Result<Object> assurerCollection(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderMainService.assurerCollectionConfirm(jsonObject.getString("orderIds"));
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
    public Result<Object> assurerPay(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderMainService.assurerPayConfirm(jsonObject.getString("orderId"),jsonObject.getString("fileList"));
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
     * 取消订单
     */
    @AutoLog(value = "取消订单")
    @ApiOperation(value = "取消订单", notes = "取消订单")
    @PostMapping(value = "/revokeOrder")
    public Result<Object> revokeOrder(HttpServletRequest request, @RequestParam(name = "orderId", required = true) String orderId) {
        Result<Object> result = new Result<Object>();
        try {
            result = orderMainService.revokeOrderAdmin(orderId);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 通过用户id查询订单
     */
    @AutoLog(value = "订单表-通过用户id查询订单")
    @ApiOperation(value = "订单表-通过用户id查询订单", notes = "订单表-通过用户id查询订单")
    @PostMapping(value = "/orderByUser")
    public Result<List<OrderMain>> queryByUserId(@RequestParam(name = "userNo", required = true) String userNo) {
        Result<List<OrderMain>> result = new Result<List<OrderMain>>();
        Map<String,String> param = new HashMap<>();
        param.put("userId", userNo);
        List<OrderMain> orderMains = orderMainMapper.findByUserId(param);
        if (BaseUtil.Base_HasValue(orderMains)) {
            for (int i = 0; i < orderMains.size(); i++) {
                orderMains.get(i).changeMoneyToBig();
            }
        }
        result.setResult(orderMains);
        result.setSuccess(true);
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
     * 上传图片接口-返回图片访问地址
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Result<Object> uploadImg(HttpServletRequest request, HttpServletResponse response) {
        Result<Object> result = new Result<>();
        try {
            String ctxPath = uploadpath;
            String fileName = null;
            String bizPath = "files";
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
            File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
            multipartRequest = resolver.resolveMultipart(request); 

            MultipartFile mf = multipartRequest.getFile("file");// 获取上传文件对象
            String orgName = mf.getOriginalFilename();// 获取文件名
            fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
            String savePath = file.getPath() + File.separator + fileName;
            File savefile = new File(savePath);
            FileCopyUtils.copy(mf.getBytes(), savefile);
            String dbpath = basePath + File.separator + bizPath + File.separator + nowday + File.separator + fileName;
            if (dbpath.contains("\\")) {
                dbpath = dbpath.replace("\\", "/");
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", dbpath);
            result.setResult(jsonObject);
            result.setCode(CommonConstant.SC_OK_200);
        } catch (IOException e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            log.error(e.getMessage(), e);
        }
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
    @PostMapping(value = "/order")
    public Result<OrderMain> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<OrderMain> result = new Result<OrderMain>();
        OrderMain orderMain = orderMainService.getById(id);
        if (orderMain == null) {
            result.error500("未找到对应实体");
        } else {
            orderMain.changeMoneyToBig();
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
