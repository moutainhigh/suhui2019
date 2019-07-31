package org.suhui.modules.suhui.suhui.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.suhui.modules.suhui.suhui.entity.PayChannelAccount;
import org.suhui.modules.suhui.suhui.service.IPayChannelAccountService;
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
 * @Description: 支付渠道账户表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Slf4j
@Api(tags="支付渠道账户表")
@RestController
@RequestMapping("/suhui/payChannelAccount")
public class PayChannelAccountController {
	@Autowired
	private IPayChannelAccountService payChannelAccountService;
	
	/**
	  * 分页列表查询
	 * @param payChannelAccount
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "支付渠道账户表-分页列表查询")
	@ApiOperation(value="支付渠道账户表-分页列表查询", notes="支付渠道账户表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PayChannelAccount>> queryPageList(PayChannelAccount payChannelAccount,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<PayChannelAccount>> result = new Result<IPage<PayChannelAccount>>();
		QueryWrapper<PayChannelAccount> queryWrapper = QueryGenerator.initQueryWrapper(payChannelAccount, req.getParameterMap());
		Page<PayChannelAccount> page = new Page<PayChannelAccount>(pageNo, pageSize);
		IPage<PayChannelAccount> pageList = payChannelAccountService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param payChannelAccount
	 * @return
	 */
	@AutoLog(value = "支付渠道账户表-添加")
	@ApiOperation(value="支付渠道账户表-添加", notes="支付渠道账户表-添加")
	@PostMapping(value = "/add")
	public Result<PayChannelAccount> add(@RequestBody PayChannelAccount payChannelAccount) {
		Result<PayChannelAccount> result = new Result<PayChannelAccount>();
		try {
			payChannelAccountService.save(payChannelAccount);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param payChannelAccount
	 * @return
	 */
	@AutoLog(value = "支付渠道账户表-编辑")
	@ApiOperation(value="支付渠道账户表-编辑", notes="支付渠道账户表-编辑")
	@PutMapping(value = "/edit")
	public Result<PayChannelAccount> edit(@RequestBody PayChannelAccount payChannelAccount) {
		Result<PayChannelAccount> result = new Result<PayChannelAccount>();
		PayChannelAccount payChannelAccountEntity = payChannelAccountService.getById(payChannelAccount.getId());
		if(payChannelAccountEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = payChannelAccountService.updateById(payChannelAccount);
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
	@AutoLog(value = "支付渠道账户表-通过id删除")
	@ApiOperation(value="支付渠道账户表-通过id删除", notes="支付渠道账户表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<PayChannelAccount> delete(@RequestParam(name="id",required=true) String id) {
		Result<PayChannelAccount> result = new Result<PayChannelAccount>();
		PayChannelAccount payChannelAccount = payChannelAccountService.getById(id);
		if(payChannelAccount==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = payChannelAccountService.removeById(id);
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
	@AutoLog(value = "支付渠道账户表-批量删除")
	@ApiOperation(value="支付渠道账户表-批量删除", notes="支付渠道账户表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<PayChannelAccount> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<PayChannelAccount> result = new Result<PayChannelAccount>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.payChannelAccountService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "支付渠道账户表-通过id查询")
	@ApiOperation(value="支付渠道账户表-通过id查询", notes="支付渠道账户表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PayChannelAccount> queryById(@RequestParam(name="id",required=true) String id) {
		Result<PayChannelAccount> result = new Result<PayChannelAccount>();
		PayChannelAccount payChannelAccount = payChannelAccountService.getById(id);
		if(payChannelAccount==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(payChannelAccount);
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
      QueryWrapper<PayChannelAccount> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              PayChannelAccount payChannelAccount = JSON.parseObject(deString, PayChannelAccount.class);
              queryWrapper = QueryGenerator.initQueryWrapper(payChannelAccount, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<PayChannelAccount> pageList = payChannelAccountService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "支付渠道账户表列表");
      mv.addObject(NormalExcelConstants.CLASS, PayChannelAccount.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("支付渠道账户表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<PayChannelAccount> listPayChannelAccounts = ExcelImportUtil.importExcel(file.getInputStream(), PayChannelAccount.class, params);
              for (PayChannelAccount payChannelAccountExcel : listPayChannelAccounts) {
                  payChannelAccountService.save(payChannelAccountExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listPayChannelAccounts.size());
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
