package org.suhui.modules.position.controller;

import java.util.List;

import io.swagger.annotations.ApiOperation;
import org.suhui.common.api.vo.Result;
import lombok.extern.slf4j.Slf4j;

import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.modules.position.entity.PositionEntity;
import org.suhui.modules.position.service.IPositionEntityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

 /**
 * @Description: 职位查询接口
 * @Author: jeecg-boot
 * @Date:   2019-07-02
 * @Version: V1.0
 */
@Slf4j
@Api(tags="职位查询接口")
@RestController
@RequestMapping("/position/positionEntity")
public class PositionEntityController {
	@Autowired
	private IPositionEntityService positionEntityService;

	 /**
	  * 通过id查询
	  * @param typeId
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "职位查询接口-通过id查询")
	 @ApiOperation(value="职位查询接口-通过id查询", notes="职位查询接口-通过id查询")
	 @PostMapping(value = "/queryById")
	 public Result<List<PositionEntity>> queryById(@RequestParam(name = "typeId",required = true) String typeId,@RequestParam(name = "id",required = false) String id) {
		 Result<List<PositionEntity>> result = new Result<>();
		 List<PositionEntity> positionEntity = positionEntityService.selectById(typeId,id);
		 if(positionEntity==null) {
			 result.error500("未找到对应实体");
		 }else {
			 result.setResult(positionEntity);
			 result.setSuccess(true);
		 }
		 return result;
	 }
}