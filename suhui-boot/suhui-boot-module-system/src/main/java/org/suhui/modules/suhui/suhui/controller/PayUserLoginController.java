package org.suhui.modules.suhui.suhui.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.suhui.common.api.vo.Result;
import org.suhui.common.system.query.QueryGenerator;
import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.suhui.suhui.entity.PayUserLogin;
import org.suhui.modules.suhui.suhui.service.IPayUserLoginService;
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
 * @Description: app用户登陆
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Slf4j
@Api(tags="app用户登陆")
@RestController
@RequestMapping("/suhui/payUserLogin")
public class PayUserLoginController {
	@Autowired
	private IPayUserLoginService payUserLoginService;
	
	/**
	  * 分页列表查询
	 * @param payUserLogin
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "app用户登陆-分页列表查询")
	@ApiOperation(value="app用户登陆-分页列表查询", notes="app用户登陆-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PayUserLogin>> queryPageList(PayUserLogin payUserLogin,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<PayUserLogin>> result = new Result<IPage<PayUserLogin>>();
		QueryWrapper<PayUserLogin> queryWrapper = QueryGenerator.initQueryWrapper(payUserLogin, req.getParameterMap());
		Page<PayUserLogin> page = new Page<PayUserLogin>(pageNo, pageSize);
		IPage<PayUserLogin> pageList = payUserLoginService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param payUserLogin
	 * @return
	 */
	@AutoLog(value = "app用户登陆-添加")
	@ApiOperation(value="app用户登陆-添加", notes="app用户登陆-添加")
	@PostMapping(value = "/add")
	public Result<PayUserLogin> add(@RequestBody PayUserLogin payUserLogin) {
		Result<PayUserLogin> result = new Result<PayUserLogin>();
		try {
			payUserLoginService.save(payUserLogin);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param payUserLogin
	 * @return
	 */
	@AutoLog(value = "app用户登陆-编辑")
	@ApiOperation(value="app用户登陆-编辑", notes="app用户登陆-编辑")
	@PutMapping(value = "/edit")
	public Result<PayUserLogin> edit(@RequestBody PayUserLogin payUserLogin) {
		Result<PayUserLogin> result = new Result<PayUserLogin>();
		PayUserLogin payUserLoginEntity = payUserLoginService.getById(payUserLogin.getId());
		if(payUserLoginEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = payUserLoginService.updateById(payUserLogin);
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
	@AutoLog(value = "app用户登陆-通过id删除")
	@ApiOperation(value="app用户登陆-通过id删除", notes="app用户登陆-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<PayUserLogin> delete(@RequestParam(name="id",required=true) String id) {
		Result<PayUserLogin> result = new Result<PayUserLogin>();
		PayUserLogin payUserLogin = payUserLoginService.getById(id);
		if(payUserLogin==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = payUserLoginService.removeById(id);
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
	@AutoLog(value = "app用户登陆-批量删除")
	@ApiOperation(value="app用户登陆-批量删除", notes="app用户登陆-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<PayUserLogin> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<PayUserLogin> result = new Result<PayUserLogin>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.payUserLoginService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "app用户登陆-通过id查询")
	@ApiOperation(value="app用户登陆-通过id查询", notes="app用户登陆-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PayUserLogin> queryById(@RequestParam(name="id",required=true) String id) {
		Result<PayUserLogin> result = new Result<PayUserLogin>();
		PayUserLogin payUserLogin = payUserLoginService.getById(id);
		if(payUserLogin==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(payUserLogin);
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
      QueryWrapper<PayUserLogin> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              PayUserLogin payUserLogin = JSON.parseObject(deString, PayUserLogin.class);
              queryWrapper = QueryGenerator.initQueryWrapper(payUserLogin, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<PayUserLogin> pageList = payUserLoginService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "app用户登陆列表");
      mv.addObject(NormalExcelConstants.CLASS, PayUserLogin.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("app用户登陆列表数据", "导出人:Jeecg", "导出信息"));
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
              List<PayUserLogin> listPayUserLogins = ExcelImportUtil.importExcel(file.getInputStream(), PayUserLogin.class, params);
              for (PayUserLogin payUserLoginExcel : listPayUserLogins) {
                  payUserLoginService.save(payUserLoginExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listPayUserLogins.size());
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
