package org.suhui.modules.resume.controller;

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
import org.suhui.modules.resume.entity.ResumeWorkrecord;
import org.suhui.modules.resume.service.IResumeWorkrecordService;
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
 * @Description: 志愿者经历
 * @Author: jeecg-boot
 * @Date:   2019-07-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags="志愿者经历")
@RestController
@RequestMapping("/resume/resumeWorkrecord")
public class ResumeWorkrecordController {
	@Autowired
	private IResumeWorkrecordService resumeWorkrecordService;
	
	/**
	  * 分页列表查询
	 * @param resumeWorkrecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "志愿者经历-分页列表查询")
	@ApiOperation(value="志愿者经历-分页列表查询", notes="志愿者经历-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ResumeWorkrecord>> queryPageList(ResumeWorkrecord resumeWorkrecord,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<ResumeWorkrecord>> result = new Result<IPage<ResumeWorkrecord>>();
		QueryWrapper<ResumeWorkrecord> queryWrapper = QueryGenerator.initQueryWrapper(resumeWorkrecord, req.getParameterMap());
		Page<ResumeWorkrecord> page = new Page<ResumeWorkrecord>(pageNo, pageSize);
		IPage<ResumeWorkrecord> pageList = resumeWorkrecordService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param resumeWorkrecord
	 * @return
	 */
	@AutoLog(value = "志愿者经历-添加")
	@ApiOperation(value="志愿者经历-添加", notes="志愿者经历-添加")
	@PostMapping(value = "/add")
	public Result<ResumeWorkrecord> add(@RequestBody ResumeWorkrecord resumeWorkrecord) {
		Result<ResumeWorkrecord> result = new Result<ResumeWorkrecord>();
		try {
			resumeWorkrecordService.save(resumeWorkrecord);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param resumeWorkrecord
	 * @return
	 */
	@AutoLog(value = "志愿者经历-编辑")
	@ApiOperation(value="志愿者经历-编辑", notes="志愿者经历-编辑")
	@PutMapping(value = "/edit")
	public Result<ResumeWorkrecord> edit(@RequestBody ResumeWorkrecord resumeWorkrecord) {
		Result<ResumeWorkrecord> result = new Result<ResumeWorkrecord>();
		ResumeWorkrecord resumeWorkrecordEntity = resumeWorkrecordService.getById(resumeWorkrecord.getId());
		if(resumeWorkrecordEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = resumeWorkrecordService.updateById(resumeWorkrecord);
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
	@AutoLog(value = "志愿者经历-通过id删除")
	@ApiOperation(value="志愿者经历-通过id删除", notes="志愿者经历-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<ResumeWorkrecord> delete(@RequestParam(name="id",required=true) String id) {
		Result<ResumeWorkrecord> result = new Result<ResumeWorkrecord>();
		ResumeWorkrecord resumeWorkrecord = resumeWorkrecordService.getById(id);
		if(resumeWorkrecord==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = resumeWorkrecordService.removeById(id);
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
	@AutoLog(value = "志愿者经历-批量删除")
	@ApiOperation(value="志愿者经历-批量删除", notes="志愿者经历-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<ResumeWorkrecord> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<ResumeWorkrecord> result = new Result<ResumeWorkrecord>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.resumeWorkrecordService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "志愿者经历-通过id查询")
	@ApiOperation(value="志愿者经历-通过id查询", notes="志愿者经历-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ResumeWorkrecord> queryById(@RequestParam(name="id",required=true) String id) {
		Result<ResumeWorkrecord> result = new Result<ResumeWorkrecord>();
		ResumeWorkrecord resumeWorkrecord = resumeWorkrecordService.getById(id);
		if(resumeWorkrecord==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(resumeWorkrecord);
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
      QueryWrapper<ResumeWorkrecord> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              ResumeWorkrecord resumeWorkrecord = JSON.parseObject(deString, ResumeWorkrecord.class);
              queryWrapper = QueryGenerator.initQueryWrapper(resumeWorkrecord, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<ResumeWorkrecord> pageList = resumeWorkrecordService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "志愿者经历列表");
      mv.addObject(NormalExcelConstants.CLASS, ResumeWorkrecord.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("志愿者经历列表数据", "导出人:Jeecg", "导出信息"));
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
              List<ResumeWorkrecord> listResumeWorkrecords = ExcelImportUtil.importExcel(file.getInputStream(), ResumeWorkrecord.class, params);
              for (ResumeWorkrecord resumeWorkrecordExcel : listResumeWorkrecords) {
                  resumeWorkrecordService.save(resumeWorkrecordExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listResumeWorkrecords.size());
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
