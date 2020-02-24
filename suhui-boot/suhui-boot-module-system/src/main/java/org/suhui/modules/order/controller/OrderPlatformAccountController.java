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
import org.suhui.common.api.vo.Result;
import org.suhui.common.system.query.QueryGenerator;
import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.order.entity.OrderPlatformAccount;
import org.suhui.modules.order.service.IOrderPlatformAccountService;
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
 * @Description: 平台账户管理
 * @Author: jeecg-boot
 * @Date:   2020-02-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags="平台账户管理")
@RestController
@RequestMapping("/order/platformAccount")
public class OrderPlatformAccountController {
	@Autowired
	private IOrderPlatformAccountService orderPlatformAccountService;


	 /**
	  * 改变账户状态
	  */
	 @PostMapping(value = "/changeState")
	 public Result<Object> changeState(@RequestBody JSONObject jsonObject) {
		 Result<Object> result = new Result<Object>();
		 try {
			 result = orderPlatformAccountService.changeAccountState(jsonObject.getString("accountIds"),jsonObject.getString("type"));
			 return result;
		 } catch (Exception e) {
			 result.error500("操作失败");
		 }
		 return result;
	 }

	/**
	  * 分页列表查询
	 * @param orderPlatformAccount
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "平台账户管理-分页列表查询")
	@ApiOperation(value="平台账户管理-分页列表查询", notes="平台账户管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<OrderPlatformAccount>> queryPageList(OrderPlatformAccount orderPlatformAccount,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<OrderPlatformAccount>> result = new Result<IPage<OrderPlatformAccount>>();
		QueryWrapper<OrderPlatformAccount> queryWrapper = QueryGenerator.initQueryWrapper(orderPlatformAccount, req.getParameterMap());
		Page<OrderPlatformAccount> page = new Page<OrderPlatformAccount>(pageNo, pageSize);
		IPage<OrderPlatformAccount> pageList = orderPlatformAccountService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param orderPlatformAccount
	 * @return
	 */
	@AutoLog(value = "平台账户管理-添加")
	@ApiOperation(value="平台账户管理-添加", notes="平台账户管理-添加")
	@PostMapping(value = "/add")
	public Result<OrderPlatformAccount> add(@RequestBody OrderPlatformAccount orderPlatformAccount) {
		Result<OrderPlatformAccount> result = new Result<OrderPlatformAccount>();
		try {
			orderPlatformAccountService.save(orderPlatformAccount);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param orderPlatformAccount
	 * @return
	 */
	@AutoLog(value = "平台账户管理-编辑")
	@ApiOperation(value="平台账户管理-编辑", notes="平台账户管理-编辑")
	@PutMapping(value = "/edit")
	public Result<OrderPlatformAccount> edit(@RequestBody OrderPlatformAccount orderPlatformAccount) {
		Result<OrderPlatformAccount> result = new Result<OrderPlatformAccount>();
		OrderPlatformAccount orderPlatformAccountEntity = orderPlatformAccountService.getById(orderPlatformAccount.getId());
		if(orderPlatformAccountEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = orderPlatformAccountService.updateById(orderPlatformAccount);
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
	@AutoLog(value = "平台账户管理-通过id删除")
	@ApiOperation(value="平台账户管理-通过id删除", notes="平台账户管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<OrderPlatformAccount> delete(@RequestParam(name="id",required=true) String id) {
		Result<OrderPlatformAccount> result = new Result<OrderPlatformAccount>();
		OrderPlatformAccount orderPlatformAccount = orderPlatformAccountService.getById(id);
		if(orderPlatformAccount==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = orderPlatformAccountService.removeById(id);
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
	@AutoLog(value = "平台账户管理-批量删除")
	@ApiOperation(value="平台账户管理-批量删除", notes="平台账户管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<OrderPlatformAccount> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<OrderPlatformAccount> result = new Result<OrderPlatformAccount>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.orderPlatformAccountService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "平台账户管理-通过id查询")
	@ApiOperation(value="平台账户管理-通过id查询", notes="平台账户管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<OrderPlatformAccount> queryById(@RequestParam(name="id",required=true) String id) {
		Result<OrderPlatformAccount> result = new Result<OrderPlatformAccount>();
		OrderPlatformAccount orderPlatformAccount = orderPlatformAccountService.getById(id);
		if(orderPlatformAccount==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(orderPlatformAccount);
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
      QueryWrapper<OrderPlatformAccount> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              OrderPlatformAccount orderPlatformAccount = JSON.parseObject(deString, OrderPlatformAccount.class);
              queryWrapper = QueryGenerator.initQueryWrapper(orderPlatformAccount, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<OrderPlatformAccount> pageList = orderPlatformAccountService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "平台账户管理列表");
      mv.addObject(NormalExcelConstants.CLASS, OrderPlatformAccount.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("平台账户管理列表数据", "导出人:Jeecg", "导出信息"));
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
              List<OrderPlatformAccount> listOrderPlatformAccounts = ExcelImportUtil.importExcel(file.getInputStream(), OrderPlatformAccount.class, params);
              for (OrderPlatformAccount orderPlatformAccountExcel : listOrderPlatformAccounts) {
                  orderPlatformAccountService.save(orderPlatformAccountExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listOrderPlatformAccounts.size());
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
