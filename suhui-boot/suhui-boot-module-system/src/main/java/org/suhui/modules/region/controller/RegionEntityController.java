package org.suhui.modules.region.controller;

import java.util.List;

import org.suhui.common.api.vo.Result;
import org.suhui.common.aspect.annotation.AutoLog;
import lombok.extern.slf4j.Slf4j;

import org.suhui.modules.region.entity.RegionEntity;
import org.suhui.modules.region.service.IRegionEntityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description: 省市区查询接口
 * @Author: jeecg-boot
 * @Date: 2019-07-02
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "省市区查询接口")
@RestController
@RequestMapping("/region/regionEntity")
public class RegionEntityController {
    @Autowired
    private IRegionEntityService regionEntityService;

    /**
     * 通过id查询
     *
     * @param typeId * @param id
     * @return
     */
    @AutoLog(value = "省市区查询接口-通过id查询")
    @ApiOperation(value = "省市区查询接口-通过id查询", notes = "省市区查询接口-通过id查询")
    @PostMapping(value = "/queryById")
    public Result<List<RegionEntity>> queryById(@RequestParam(name = "typeId", required = true) String typeId, @RequestParam(name = "id", required = false) String id) {
        Result<List<RegionEntity>> result = new Result<>();
        List<RegionEntity> regionEntity = regionEntityService.selectById(typeId, id);
        if (regionEntity == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(regionEntity);
            result.setSuccess(true);
        }
        return result;
    }
}