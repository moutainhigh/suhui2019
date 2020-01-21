package org.suhui.modules.order.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.suhui.common.api.vo.Result;
import org.suhui.common.system.query.QueryGenerator;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.order.entity.OrderAssurerAccount;
import org.suhui.modules.order.entity.OrderAssurer;
import org.suhui.modules.order.entity.OrderMain;
import org.suhui.modules.order.vo.OrderAssurerPage;
import org.suhui.modules.order.service.IOrderAssurerService;
import org.suhui.modules.order.service.IOrderAssurerAccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import org.suhui.modules.utils.BaseUtil;

/**
 * @Description: 承兑商
 * @Author: jeecg-boot
 * @Date:   2019-12-29
 * @Version: V1.0
 */
@RestController
@RequestMapping("/order/orderAssurer")
@Slf4j
public class OrderAssurerController {
	@Autowired
	private IOrderAssurerService orderAssurerService;
	@Autowired
	private IOrderAssurerAccountService orderAssurerAccountService;


	/**
	 * 通过userNo查询承兑商数据
	 */
	@PostMapping(value = "/queryAssurerByUserNo")
	public Result<OrderAssurer> queryAssurerByUserNo( @RequestBody OrderAssurer data) {
		Result<OrderAssurer> result = new Result<OrderAssurer>();
		OrderAssurer orderAssurer = orderAssurerService.getAssurerByUserNo(data.getUserNo());
		if(orderAssurer==null) {
			result.error500("该承兑商不存在");
		}else {
			result.setResult(orderAssurer);
			result.setSuccess(true);
			result.setCode(200);
		}
		return result;
	}

	/**
	 * 更新承兑商数据
	 */
	@PostMapping(value = "/updateAssurer")
	public Result<OrderAssurer> updateAssurer( @RequestBody OrderAssurer data) {
		Result<OrderAssurer> result = new Result<OrderAssurer>();
		OrderAssurer orderAssurer = orderAssurerService.updateAssurer(data);
		if(orderAssurer==null) {
			result.error500("该承兑商不存在");
		}else {
			result.setResult(orderAssurer);
			result.setSuccess(true);
			result.setCode(200);
		}
		return result;
	}

	@PostMapping(value = "/auditPass")
	public Result<Object> auditPass(@RequestBody JSONObject jsonObject) {
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
	  * 分页列表查询
	 * @param orderAssurer
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<OrderAssurer>> queryPageList(OrderAssurer orderAssurer,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<OrderAssurer>> result = new Result<IPage<OrderAssurer>>();
		QueryWrapper<OrderAssurer> queryWrapper = QueryGenerator.initQueryWrapper(orderAssurer, req.getParameterMap());
		Page<OrderAssurer> page = new Page<OrderAssurer>(pageNo, pageSize);
		IPage<OrderAssurer> pageList = orderAssurerService.page(page, queryWrapper);
		List<OrderAssurer> orderAssurers = pageList.getRecords();
		if (BaseUtil.Base_HasValue(orderAssurers)) {
			for(int i=0;i<orderAssurers.size();i++){
				orderAssurers.get(i).changeMoneyToBig();
			}
		}
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param orderAssurerPage
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<OrderAssurer> add(@RequestBody OrderAssurerPage orderAssurerPage) {
		Result<OrderAssurer> result = new Result<OrderAssurer>();
		try {
			OrderAssurer orderAssurer = new OrderAssurer();
			BeanUtils.copyProperties(orderAssurerPage, orderAssurer);
			
			orderAssurerService.saveMain(orderAssurer, orderAssurerPage.getOrderAssurerAccountList());
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param orderAssurerPage
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<OrderAssurer> edit(@RequestBody OrderAssurerPage orderAssurerPage) {
		Result<OrderAssurer> result = new Result<OrderAssurer>();
		OrderAssurer orderAssurer = new OrderAssurer();
		BeanUtils.copyProperties(orderAssurerPage, orderAssurer);
		OrderAssurer orderAssurerEntity = orderAssurerService.getById(orderAssurer.getId());
		if(orderAssurerEntity==null) {
			result.error500("未找到对应实体");
		}else {
			orderAssurer.changeMoneyToPoints();
			boolean ok = orderAssurerService.updateById(orderAssurer);
			orderAssurerService.updateMain(orderAssurer, orderAssurerPage.getOrderAssurerAccountList());
			result.success("修改成功!");
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<OrderAssurer> delete(@RequestParam(name="id",required=true) String id) {
		Result<OrderAssurer> result = new Result<OrderAssurer>();
		OrderAssurer orderAssurer = orderAssurerService.getById(id);
		if(orderAssurer==null) {
			result.error500("未找到对应实体");
		}else {
			orderAssurerService.delMain(id);
			result.success("删除成功!");
		}
		
		return result;
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<OrderAssurer> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<OrderAssurer> result = new Result<OrderAssurer>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.orderAssurerService.delBatchMain(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<OrderAssurer> queryById(@RequestParam(name="id",required=true) String id) {
		Result<OrderAssurer> result = new Result<OrderAssurer>();
		OrderAssurer orderAssurer = orderAssurerService.getById(id);
		if(orderAssurer==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(orderAssurer);
			result.setSuccess(true);
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryOrderAssurerAccountByMainId")
	public Result<List<OrderAssurerAccount>> queryOrderAssurerAccountListByMainId(@RequestParam(name="id",required=true) String id) {
		Result<List<OrderAssurerAccount>> result = new Result<List<OrderAssurerAccount>>();
		List<OrderAssurerAccount> orderAssurerAccountList = orderAssurerAccountService.selectByMainId(id);
		if(BaseUtil.Base_HasValue(orderAssurerAccountList)){
			for(int i=0;i<orderAssurerAccountList.size();i++){
				orderAssurerAccountList.get(i).changeMoneyToBig();
			}
		}
		result.setResult(orderAssurerAccountList);
		result.setSuccess(true);
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
      QueryWrapper<OrderAssurer> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              OrderAssurer orderAssurer = JSON.parseObject(deString, OrderAssurer.class);
              queryWrapper = QueryGenerator.initQueryWrapper(orderAssurer, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<OrderAssurerPage> pageList = new ArrayList<OrderAssurerPage>();
      List<OrderAssurer> orderAssurerList = orderAssurerService.list(queryWrapper);
      for (OrderAssurer orderAssurer : orderAssurerList) {
          OrderAssurerPage vo = new OrderAssurerPage();
          BeanUtils.copyProperties(orderAssurer, vo);
          List<OrderAssurerAccount> orderAssurerAccountList = orderAssurerAccountService.selectByMainId(orderAssurer.getId());
          vo.setOrderAssurerAccountList(orderAssurerAccountList);
          pageList.add(vo);
      }
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "去列表");
      mv.addObject(NormalExcelConstants.CLASS, OrderAssurerPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("去列表数据", "导出人:Jeecg", "导出信息"));
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
              List<OrderAssurerPage> list = ExcelImportUtil.importExcel(file.getInputStream(), OrderAssurerPage.class, params);
              for (OrderAssurerPage page : list) {
                  OrderAssurer po = new OrderAssurer();
                  BeanUtils.copyProperties(page, po);
                  orderAssurerService.saveMain(po, page.getOrderAssurerAccountList());
              }
              return Result.ok("文件导入成功！数据行数:" + list.size());
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
