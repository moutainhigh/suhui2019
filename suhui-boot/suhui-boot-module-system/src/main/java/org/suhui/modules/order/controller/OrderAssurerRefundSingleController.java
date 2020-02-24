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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.suhui.common.api.vo.Result;
import org.suhui.common.system.query.QueryGenerator;
import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.order.entity.OrderAssurerRefundSingle;
import org.suhui.modules.order.service.IOrderAssurerRefundSingleService;
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
 * @Description: 退款单
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags="退款单")
@RestController
@RequestMapping("/order/orderAssurerRefundSingle")
public class OrderAssurerRefundSingleController {
	@Autowired
	private IOrderAssurerRefundSingleService orderAssurerRefundSingleService;


	 @AutoLog(value = "平台上传付款凭证")
	 @ApiOperation(value = "平台上传付款凭证", notes = "平台上传付款凭证")
	 @PostMapping(value = "/uploadRefundVoucher")
	 public Result<Object> uploadRefundVoucher(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
		 Result<Object> result = new Result<Object>();
		 try {
			 result = orderAssurerRefundSingleService.uploadRefundVoucherConfirm(jsonObject.getString("id"),jsonObject.getString("fileList"));
			 return result;
		 } catch (Exception e) {
			 result.error500("操作失败");
		 }
		 return result;
	 }

	 @PostMapping(value = "/changeState")
	 @Transactional
	 public Result<Object> changeState(@RequestBody JSONObject jsonObject) {
		 Result<Object> result = new Result<Object>();
		 try {
			 result = orderAssurerRefundSingleService.changeSingleState(jsonObject.getString("ids"),jsonObject.getString("state"));
			 return result;
		 } catch (Exception e) {
			 TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			 result.error500("操作失败");
		 }
		 return result;
	 }

	/**
	  * 分页列表查询
	 * @param orderAssurerRefundSingle
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "退款单-分页列表查询")
	@ApiOperation(value="退款单-分页列表查询", notes="退款单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<OrderAssurerRefundSingle>> queryPageList(OrderAssurerRefundSingle orderAssurerRefundSingle,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<OrderAssurerRefundSingle>> result = new Result<IPage<OrderAssurerRefundSingle>>();
		QueryWrapper<OrderAssurerRefundSingle> queryWrapper = QueryGenerator.initQueryWrapper(orderAssurerRefundSingle, req.getParameterMap());
		Page<OrderAssurerRefundSingle> page = new Page<OrderAssurerRefundSingle>(pageNo, pageSize);
		IPage<OrderAssurerRefundSingle> pageList = orderAssurerRefundSingleService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param orderAssurerRefundSingle
	 * @return
	 */
	@AutoLog(value = "退款单-添加")
	@ApiOperation(value="退款单-添加", notes="退款单-添加")
	@PostMapping(value = "/add")
	public Result<OrderAssurerRefundSingle> add(@RequestBody OrderAssurerRefundSingle orderAssurerRefundSingle) {
		Result<OrderAssurerRefundSingle> result = new Result<OrderAssurerRefundSingle>();
		try {
			orderAssurerRefundSingleService.save(orderAssurerRefundSingle);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param orderAssurerRefundSingle
	 * @return
	 */
	@AutoLog(value = "退款单-编辑")
	@ApiOperation(value="退款单-编辑", notes="退款单-编辑")
	@PutMapping(value = "/edit")
	public Result<OrderAssurerRefundSingle> edit(@RequestBody OrderAssurerRefundSingle orderAssurerRefundSingle) {
		Result<OrderAssurerRefundSingle> result = new Result<OrderAssurerRefundSingle>();
		OrderAssurerRefundSingle orderAssurerRefundSingleEntity = orderAssurerRefundSingleService.getById(orderAssurerRefundSingle.getId());
		if(orderAssurerRefundSingleEntity==null) {
			result.error500("未找到对应实体");
		}else {
			if(!orderAssurerRefundSingleEntity.getRefundSingleState().equals("pass")){
				boolean ok = orderAssurerRefundSingleService.updateById(orderAssurerRefundSingle);
				//TODO 返回false说明什么？
				if(ok) {
					result.success("修改成功!");
				}
			}else{
				result.error500("审核通过的退款单不可进行修改");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "退款单-通过id删除")
	@ApiOperation(value="退款单-通过id删除", notes="退款单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<OrderAssurerRefundSingle> delete(@RequestParam(name="id",required=true) String id) {
		Result<OrderAssurerRefundSingle> result = new Result<OrderAssurerRefundSingle>();
		OrderAssurerRefundSingle orderAssurerRefundSingle = orderAssurerRefundSingleService.getById(id);
		if(orderAssurerRefundSingle==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = orderAssurerRefundSingleService.removeById(id);
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
	@AutoLog(value = "退款单-批量删除")
	@ApiOperation(value="退款单-批量删除", notes="退款单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<OrderAssurerRefundSingle> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<OrderAssurerRefundSingle> result = new Result<OrderAssurerRefundSingle>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			String arr[] = ids.split(",");
			for (String id : arr) {
				OrderAssurerRefundSingle orderAssurerRefundSingle = orderAssurerRefundSingleService.getById(id);
				if (!orderAssurerRefundSingle.getRefundSingleState().equals("pass")) {
					orderAssurerRefundSingle.setDelFlag("1");
					orderAssurerRefundSingleService.updateById(orderAssurerRefundSingle);
				}
			}
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "退款单-通过id查询")
	@ApiOperation(value="退款单-通过id查询", notes="退款单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<OrderAssurerRefundSingle> queryById(@RequestParam(name="id",required=true) String id) {
		Result<OrderAssurerRefundSingle> result = new Result<OrderAssurerRefundSingle>();
		OrderAssurerRefundSingle orderAssurerRefundSingle = orderAssurerRefundSingleService.getById(id);
		if(orderAssurerRefundSingle==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(orderAssurerRefundSingle);
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
      QueryWrapper<OrderAssurerRefundSingle> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              OrderAssurerRefundSingle orderAssurerRefundSingle = JSON.parseObject(deString, OrderAssurerRefundSingle.class);
              queryWrapper = QueryGenerator.initQueryWrapper(orderAssurerRefundSingle, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<OrderAssurerRefundSingle> pageList = orderAssurerRefundSingleService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "退款单列表");
      mv.addObject(NormalExcelConstants.CLASS, OrderAssurerRefundSingle.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("退款单列表数据", "导出人:Jeecg", "导出信息"));
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
              List<OrderAssurerRefundSingle> listOrderAssurerRefundSingles = ExcelImportUtil.importExcel(file.getInputStream(), OrderAssurerRefundSingle.class, params);
              for (OrderAssurerRefundSingle orderAssurerRefundSingleExcel : listOrderAssurerRefundSingles) {
                  orderAssurerRefundSingleService.save(orderAssurerRefundSingleExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listOrderAssurerRefundSingles.size());
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
