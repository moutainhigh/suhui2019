package org.suhui.modules.industry.controller;

import java.util.List;
import org.suhui.common.api.vo.Result;
import org.suhui.common.aspect.annotation.AutoLog;
import lombok.extern.slf4j.Slf4j;

import org.suhui.modules.industry.entity.IndustryEntity;
import org.suhui.modules.industry.service.IIndustryEntityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 行业查询接口
 * @Author: jeecg-boot
 * @Date:   2019-07-02
 * @Version: V1.0
 */
@Slf4j
@Api(tags="行业查询接口")
@RestController
@RequestMapping("/industry/industryEntity")
public class IndustryEntityController {
	@Autowired
	private IIndustryEntityService industryEntityService;
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "行业查询接口-通过id查询")
	@ApiOperation(value="行业查询接口-通过id查询", notes="行业查询接口-通过id查询")
	@PostMapping(value = "/queryById")
	public Result<List<IndustryEntity>> queryById(@RequestParam(name="typeId",required=true) String typeId,@RequestParam(name="id",required=false) String id) {
		Result<List<IndustryEntity>> result = new Result<>();
		List<IndustryEntity> industryEntity = industryEntityService.selectById(typeId,id);
		if(industryEntity==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(industryEntity);
			result.setSuccess(true);
		}
		return result;
	}
}