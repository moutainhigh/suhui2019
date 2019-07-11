package org.suhui.modules.publish.controller;

import java.util.List;
import java.util.Map;

import org.suhui.common.api.vo.Result;
import org.suhui.common.aspect.annotation.AutoLog;
import lombok.extern.slf4j.Slf4j;

import org.suhui.modules.publish.entity.PublishPositionEntity;
import org.suhui.modules.publish.service.IPublishPositionEntityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description: 职位增删改查
 * @Author: jeecg-boot
 * @Date:   2019-07-03
 * @Version: V1.0
 */
@Slf4j
@Api(tags="职位增删改查")
@RestController
@RequestMapping("/publish/publishPositionEntity")
public class PublishPositionEntityController {
	@Autowired
	private IPublishPositionEntityService publishPositionEntityService;

	/**
	 *   添加
	 * @param publishPositionEntity
	 * @return
	 */
	@AutoLog(value = "职位增删改查-添加")
	@ApiOperation(value="职位增删改查-添加", notes="职位增删改查-添加")
	@PostMapping(value = "/add")
	public Result<PublishPositionEntity> add(@RequestBody PublishPositionEntity publishPositionEntity) {
		Result<PublishPositionEntity> result = new Result<PublishPositionEntity>();
		try {
			publishPositionEntityService.save(publishPositionEntity);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 *  编辑
	 * @return
	 */
	@AutoLog(value = "职位增删改查-编辑")
	@ApiOperation(value="职位增删改查-编辑", notes="职位增删改查-编辑")
	@PostMapping(value = "/edit")
	public Result<PublishPositionEntity> edit(@RequestParam Map<String, Object> params) {
		Result<PublishPositionEntity> result = new Result<PublishPositionEntity>();
		List<PublishPositionEntity> publishPositionEntity = publishPositionEntityService.find(params);
		if(publishPositionEntity==null) {
			result.error500("未找到对应实体");
		}else {
			int ok = publishPositionEntityService.update(params);
			if(ok > 0) {
				result.success("修改成功!");
			}
		}
		return result;
	}

	/**
	 * 通过id查询
	 * @return
	 */
	@AutoLog(value = "职位增删改查-通过id查询")
	@ApiOperation(value="职位增删改查-通过id查询", notes="职位增删改查-通过id查询")
	@PostMapping(value = "/find")
	public Result<List<PublishPositionEntity>> queryById(@RequestParam Map<String, Object> params) {
		Result<List<PublishPositionEntity>> result = new Result<>();
		List<PublishPositionEntity> publishPositionEntity = publishPositionEntityService.find(params);
		if(publishPositionEntity==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(publishPositionEntity);
			result.setSuccess(true);
		}
		return result;
	}
}