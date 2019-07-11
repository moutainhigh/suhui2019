package org.suhui.modules.follow.controller;

import java.util.List;
import org.suhui.common.api.vo.Result;
import org.suhui.common.aspect.annotation.AutoLog;
import lombok.extern.slf4j.Slf4j;

import org.suhui.modules.follow.entity.FollowPositionEntity;
import org.suhui.modules.follow.service.IFollowPositionEntityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 关注职位
 * @Author: jeecg-boot
 * @Date:   2019-07-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags="关注职位")
@RestController
@RequestMapping("/follow/followPositionEntity")
public class FollowPositionEntityController {
	@Autowired
	private IFollowPositionEntityService followPositionEntityService;
	
	/**
	  *   添加关注
	 * @param followPositionEntity
	 * @return
	 */
	@AutoLog(value = "关注职位-添加")
	@ApiOperation(value="关注职位-添加", notes="关注职位-添加")
	@PostMapping(value = "/add")
	public Result<FollowPositionEntity> add(@RequestBody FollowPositionEntity followPositionEntity) {
		Result<FollowPositionEntity> result = new Result<>();
		try {
			boolean ok = followPositionEntityService.save(followPositionEntity);
			if(ok) {
				result.success("添加成功！");
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *   通过id删除关注
	 * @param paramEntity
	 * @return
	 */
	@AutoLog(value = "关注职位-通过id删除")
	@ApiOperation(value="关注职位-通过id删除", notes="关注职位-通过id删除")
	@PostMapping(value = "/delete")
	public Result<FollowPositionEntity> delete(@RequestBody FollowPositionEntity paramEntity) {
		Result<FollowPositionEntity> result = new Result<FollowPositionEntity>();
		List<FollowPositionEntity> followPositionList = followPositionEntityService.find(paramEntity);
		if(followPositionList==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = followPositionEntityService.delete(paramEntity);
			if(ok) {
				result.success("删除成功!");
			}
		}
		return result;
	}

	/**
	  * 通过id查询
	 * @param paramEntity
	 * @return
	 */
	@AutoLog(value = "关注职位-通过id查询")
	@ApiOperation(value="关注职位-通过id查询", notes="关注职位-通过id查询")
	@PostMapping(value = "/queryById")
	public Result<List<FollowPositionEntity>> queryById(@RequestBody FollowPositionEntity paramEntity) {
		Result<List<FollowPositionEntity>> result = new Result<>();
		List<FollowPositionEntity> followPositionEntity = followPositionEntityService.find(paramEntity);
		if(followPositionEntity==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(followPositionEntity);
			result.setSuccess(true);
		}
		return result;
	}
}