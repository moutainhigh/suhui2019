package org.suhui.modules.exchange.controller;

import java.math.BigDecimal;
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
import org.suhui.modules.exchange.entity.BasicExchangeRate;
import org.suhui.modules.exchange.entity.UserExchangeRecord;
import org.suhui.modules.exchange.service.IBasicExchangeRateService;
import org.suhui.modules.exchange.service.IUserExchangeRecordService;
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
import org.suhui.modules.system.entity.SysUser;
import org.suhui.modules.system.service.ISysUserService;

/**
 * @Description: 用户的换汇记录表
 * @Author: jeecg-boot
 * @Date:   2019-07-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags="用户的换汇记录表")
@RestController
@RequestMapping("/api/exchange/record")
public class UserExchangeRecordController {
	@Autowired
	private IUserExchangeRecordService userExchangeRecordService;
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private IBasicExchangeRateService basicExchangeRateService;

	
	/**
	  * 分页列表查询
	 * @param userExchangeRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "用户的换汇记录表-分页列表查询")
	@ApiOperation(value="用户的换汇记录表-分页列表查询", notes="用户的换汇记录表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<UserExchangeRecord>> queryPageList(UserExchangeRecord userExchangeRecord,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<UserExchangeRecord>> result = new Result<IPage<UserExchangeRecord>>();
		QueryWrapper<UserExchangeRecord> queryWrapper = QueryGenerator.initQueryWrapper(userExchangeRecord, req.getParameterMap());
		Page<UserExchangeRecord> page = new Page<UserExchangeRecord>(pageNo, pageSize);
		IPage<UserExchangeRecord> pageList = userExchangeRecordService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param userExchangeRecord
	 * @return
	 */
	@AutoLog(value = "用户的换汇记录表-添加")
	@ApiOperation(value="用户的换汇记录表-添加", notes="用户的换汇记录表-添加")
	@PostMapping(value = "/do")
	public Result<UserExchangeRecord> add(@RequestBody UserExchangeRecord userExchangeRecord) {
		Result<UserExchangeRecord> result = new Result<UserExchangeRecord>();
		try {
			//check
			SysUser sysUser=sysUserService.getById(userExchangeRecord.getUserId());
			if(sysUser==null){
				result.success("user not found");
				return result;
			}
			BasicExchangeRate rate=basicExchangeRateService.getOne(new QueryWrapper<BasicExchangeRate>().
					eq("source_currency",userExchangeRecord.getSourceCurrency()).eq("target_currency",userExchangeRecord.getTargetCurrency()));
			if(rate==null){
				result.success("rate not founf");
				return result;
			}
			BigDecimal amount=userExchangeRecord.getAmount();
			if(rate.getSourceCurrency()==0){
				sysUser.setRmbAmount(sysUser.getRmbAmount().subtract(amount.multiply(rate.getLiveRate())));
			}else if(rate.getSourceCurrency()==1){
				sysUser.setRmbAmount(sysUser.getUsAmount().subtract(amount.multiply(rate.getLiveRate())));
			}else{
				sysUser.setRmbAmount(sysUser.getPhAmount().subtract(amount.multiply(rate.getLiveRate())));
			}
			if(rate.getTargetCurrency()==0){
				sysUser.setRmbAmount(sysUser.getRmbAmount().add(amount.multiply(rate.getLiveRate())));
			}else if(rate.getTargetCurrency()==1){
				sysUser.setRmbAmount(sysUser.getUsAmount().add(amount.multiply(rate.getLiveRate())));
			}else{
				sysUser.setRmbAmount(sysUser.getPhAmount().add(amount.multiply(rate.getLiveRate())));
			}
			userExchangeRecord.setCurrentRate(rate.getLiveRate());
			userExchangeRecord.setRmbAmount(sysUser.getRmbAmount());
			userExchangeRecord.setUsAmount(sysUser.getUsAmount());
			userExchangeRecord.setPhAmount(sysUser.getPhAmount());
			userExchangeRecordService.saveRecord(userExchangeRecord,sysUser);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}



	/**
	  *  编辑
	 * @param userExchangeRecord
	 * @return
	 */
	@AutoLog(value = "用户的换汇记录表-编辑")
	@ApiOperation(value="用户的换汇记录表-编辑", notes="用户的换汇记录表-编辑")
	@PutMapping(value = "/edit")
	public Result<UserExchangeRecord> edit(@RequestBody UserExchangeRecord userExchangeRecord) {
		Result<UserExchangeRecord> result = new Result<UserExchangeRecord>();
		UserExchangeRecord userExchangeRecordEntity = userExchangeRecordService.getById(userExchangeRecord.getId());
		if(userExchangeRecordEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userExchangeRecordService.updateById(userExchangeRecord);
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
	@AutoLog(value = "用户的换汇记录表-通过id删除")
	@ApiOperation(value="用户的换汇记录表-通过id删除", notes="用户的换汇记录表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<UserExchangeRecord> delete(@RequestParam(name="id",required=true) String id) {
		Result<UserExchangeRecord> result = new Result<UserExchangeRecord>();
		UserExchangeRecord userExchangeRecord = userExchangeRecordService.getById(id);
		if(userExchangeRecord==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userExchangeRecordService.removeById(id);
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
	@AutoLog(value = "用户的换汇记录表-批量删除")
	@ApiOperation(value="用户的换汇记录表-批量删除", notes="用户的换汇记录表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<UserExchangeRecord> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<UserExchangeRecord> result = new Result<UserExchangeRecord>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.userExchangeRecordService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户的换汇记录表-通过id查询")
	@ApiOperation(value="用户的换汇记录表-通过id查询", notes="用户的换汇记录表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<UserExchangeRecord> queryById(@RequestParam(name="id",required=true) String id) {
		Result<UserExchangeRecord> result = new Result<UserExchangeRecord>();
		UserExchangeRecord userExchangeRecord = userExchangeRecordService.getById(id);
		if(userExchangeRecord==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(userExchangeRecord);
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
      QueryWrapper<UserExchangeRecord> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              UserExchangeRecord userExchangeRecord = JSON.parseObject(deString, UserExchangeRecord.class);
              queryWrapper = QueryGenerator.initQueryWrapper(userExchangeRecord, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<UserExchangeRecord> pageList = userExchangeRecordService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "用户的换汇记录表列表");
      mv.addObject(NormalExcelConstants.CLASS, UserExchangeRecord.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户的换汇记录表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<UserExchangeRecord> listUserExchangeRecords = ExcelImportUtil.importExcel(file.getInputStream(), UserExchangeRecord.class, params);
              for (UserExchangeRecord userExchangeRecordExcel : listUserExchangeRecords) {
                  userExchangeRecordService.save(userExchangeRecordExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listUserExchangeRecords.size());
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
