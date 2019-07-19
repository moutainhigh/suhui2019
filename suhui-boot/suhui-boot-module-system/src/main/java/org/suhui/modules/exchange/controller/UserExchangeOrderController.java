package org.suhui.modules.exchange.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import org.suhui.common.api.vo.Result;
import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.common.system.query.QueryGenerator;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.exchange.entity.UserExchangeOrder;
import org.suhui.modules.exchange.service.IUserExchangeOrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

 /**
 * @Description: 用户换汇预约记录表
 * @Author: jeecg-boot
 * @Date:   2019-07-17
 * @Version: V1.0
 */
@Slf4j
@Api(tags="用户换汇预约记录表")
@RestController
@RequestMapping("/api/exchange/order")
public class UserExchangeOrderController {
	@Autowired
	private IUserExchangeOrderService userExchangeOrderService;
	
	/**
	  * 分页列表查询
	 * @param userExchangeOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "用户换汇预约记录表-分页列表查询")
	@ApiOperation(value="用户换汇预约记录表-分页列表查询", notes="用户换汇预约记录表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<UserExchangeOrder>> queryPageList(UserExchangeOrder userExchangeOrder,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<UserExchangeOrder>> result = new Result<IPage<UserExchangeOrder>>();
		QueryWrapper<UserExchangeOrder> queryWrapper = QueryGenerator.initQueryWrapper(userExchangeOrder, req.getParameterMap());
		Page<UserExchangeOrder> page = new Page<UserExchangeOrder>(pageNo, pageSize);
		IPage<UserExchangeOrder> pageList = userExchangeOrderService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param userExchangeOrder
	 * @return
	 */
	@AutoLog(value = "用户换汇预约记录表-添加")
	@ApiOperation(value="用户换汇预约记录表-添加", notes="用户换汇预约记录表-添加")
	@PostMapping(value = "")
	public Result<UserExchangeOrder> add(@RequestBody UserExchangeOrder userExchangeOrder) {
		Result<UserExchangeOrder> result = new Result<UserExchangeOrder>();
		try {
			userExchangeOrderService.save(userExchangeOrder);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param userExchangeOrder
	 * @return
	 */
	@AutoLog(value = "用户换汇预约记录表-编辑")
	@ApiOperation(value="用户换汇预约记录表-编辑", notes="用户换汇预约记录表-编辑")
	@PutMapping(value = "/edit")
	public Result<UserExchangeOrder> edit(@RequestBody UserExchangeOrder userExchangeOrder) {
		Result<UserExchangeOrder> result = new Result<UserExchangeOrder>();
		UserExchangeOrder userExchangeOrderEntity = userExchangeOrderService.getById(userExchangeOrder.getId());
		if(userExchangeOrderEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userExchangeOrderService.updateById(userExchangeOrder);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户换汇预约记录表-通过id删除")
	@ApiOperation(value="用户换汇预约记录表-通过id删除", notes="用户换汇预约记录表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<UserExchangeOrder> delete(@RequestParam(name="id",required=true) String id) {
		Result<UserExchangeOrder> result = new Result<UserExchangeOrder>();
		UserExchangeOrder userExchangeOrder = userExchangeOrderService.getById(id);
		if(userExchangeOrder==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userExchangeOrderService.removeById(id);
			if(ok) {
				result.success("删除成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "用户换汇预约记录表-批量删除")
	@ApiOperation(value="用户换汇预约记录表-批量删除", notes="用户换汇预约记录表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<UserExchangeOrder> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<UserExchangeOrder> result = new Result<UserExchangeOrder>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.userExchangeOrderService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户换汇预约记录表-通过id查询")
	@ApiOperation(value="用户换汇预约记录表-通过id查询", notes="用户换汇预约记录表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<UserExchangeOrder> queryById(@RequestParam(name="id",required=true) String id) {
		Result<UserExchangeOrder> result = new Result<UserExchangeOrder>();
		UserExchangeOrder userExchangeOrder = userExchangeOrderService.getById(id);
		if(userExchangeOrder==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(userExchangeOrder);
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
      QueryWrapper<UserExchangeOrder> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              UserExchangeOrder userExchangeOrder = JSON.parseObject(deString, UserExchangeOrder.class);
              queryWrapper = QueryGenerator.initQueryWrapper(userExchangeOrder, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<UserExchangeOrder> pageList = userExchangeOrderService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "用户换汇预约记录表列表");
      mv.addObject(NormalExcelConstants.CLASS, UserExchangeOrder.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户换汇预约记录表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<UserExchangeOrder> listUserExchangeOrders = ExcelImportUtil.importExcel(file.getInputStream(), UserExchangeOrder.class, params);
              for (UserExchangeOrder userExchangeOrderExcel : listUserExchangeOrders) {
                  userExchangeOrderService.save(userExchangeOrderExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listUserExchangeOrders.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
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
