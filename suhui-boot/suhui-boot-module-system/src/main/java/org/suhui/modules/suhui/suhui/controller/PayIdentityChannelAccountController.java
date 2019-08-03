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
import org.suhui.modules.suhui.suhui.entity.PayIdentityChannelAccount;
import org.suhui.modules.suhui.suhui.service.IPayIdentityChannelAccountService;
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
 * @Description: 实体账号表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Slf4j
@Api(tags="实体账号表")
@RestController
@RequestMapping("/suhui/payIdentityChannelAccount")
public class PayIdentityChannelAccountController {
	@Autowired
	private IPayIdentityChannelAccountService payIdentityChannelAccountService;
	
	/**
	  * 分页列表查询
	 * @param payIdentityChannelAccount
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "实体账号表-分页列表查询")
	@ApiOperation(value="实体账号表-分页列表查询", notes="实体账号表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PayIdentityChannelAccount>> queryPageList(PayIdentityChannelAccount payIdentityChannelAccount,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<PayIdentityChannelAccount>> result = new Result<IPage<PayIdentityChannelAccount>>();
		QueryWrapper<PayIdentityChannelAccount> queryWrapper = QueryGenerator.initQueryWrapper(payIdentityChannelAccount, req.getParameterMap());
		Page<PayIdentityChannelAccount> page = new Page<PayIdentityChannelAccount>(pageNo, pageSize);
		IPage<PayIdentityChannelAccount> pageList = payIdentityChannelAccountService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param payIdentityChannelAccount
	 * @return
	 */
	@AutoLog(value = "实体账号表-添加")
	@ApiOperation(value="实体账号表-添加", notes="实体账号表-添加")
	@PostMapping(value = "/add")
	public Result<PayIdentityChannelAccount> add(@RequestBody PayIdentityChannelAccount payIdentityChannelAccount) {
		Result<PayIdentityChannelAccount> result = new Result<PayIdentityChannelAccount>();
		try {
			payIdentityChannelAccountService.save(payIdentityChannelAccount);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param payIdentityChannelAccount
	 * @return
	 */
	@AutoLog(value = "实体账号表-编辑")
	@ApiOperation(value="实体账号表-编辑", notes="实体账号表-编辑")
	@PutMapping(value = "/edit")
	public Result<PayIdentityChannelAccount> edit(@RequestBody PayIdentityChannelAccount payIdentityChannelAccount) {
		Result<PayIdentityChannelAccount> result = new Result<PayIdentityChannelAccount>();
		PayIdentityChannelAccount payIdentityChannelAccountEntity = payIdentityChannelAccountService.getById(payIdentityChannelAccount.getId());
		if(payIdentityChannelAccountEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = payIdentityChannelAccountService.updateById(payIdentityChannelAccount);
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
	@AutoLog(value = "实体账号表-通过id删除")
	@ApiOperation(value="实体账号表-通过id删除", notes="实体账号表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<PayIdentityChannelAccount> delete(@RequestParam(name="id",required=true) String id) {
		Result<PayIdentityChannelAccount> result = new Result<PayIdentityChannelAccount>();
		PayIdentityChannelAccount payIdentityChannelAccount = payIdentityChannelAccountService.getById(id);
		if(payIdentityChannelAccount==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = payIdentityChannelAccountService.removeById(id);
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
	@AutoLog(value = "实体账号表-批量删除")
	@ApiOperation(value="实体账号表-批量删除", notes="实体账号表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<PayIdentityChannelAccount> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<PayIdentityChannelAccount> result = new Result<PayIdentityChannelAccount>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.payIdentityChannelAccountService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "实体账号表-通过id查询")
	@ApiOperation(value="实体账号表-通过id查询", notes="实体账号表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PayIdentityChannelAccount> queryById(@RequestParam(name="id",required=true) String id) {
		Result<PayIdentityChannelAccount> result = new Result<PayIdentityChannelAccount>();
		PayIdentityChannelAccount payIdentityChannelAccount = payIdentityChannelAccountService.getById(id);
		if(payIdentityChannelAccount==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(payIdentityChannelAccount);
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
      QueryWrapper<PayIdentityChannelAccount> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              PayIdentityChannelAccount payIdentityChannelAccount = JSON.parseObject(deString, PayIdentityChannelAccount.class);
              queryWrapper = QueryGenerator.initQueryWrapper(payIdentityChannelAccount, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<PayIdentityChannelAccount> pageList = payIdentityChannelAccountService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "实体账号表列表");
      mv.addObject(NormalExcelConstants.CLASS, PayIdentityChannelAccount.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("实体账号表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<PayIdentityChannelAccount> listPayIdentityChannelAccounts = ExcelImportUtil.importExcel(file.getInputStream(), PayIdentityChannelAccount.class, params);
              for (PayIdentityChannelAccount payIdentityChannelAccountExcel : listPayIdentityChannelAccounts) {
                  payIdentityChannelAccountService.save(payIdentityChannelAccountExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listPayIdentityChannelAccounts.size());
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
