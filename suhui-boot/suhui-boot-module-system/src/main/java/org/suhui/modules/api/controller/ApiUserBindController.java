package org.suhui.modules.api.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.suhui.common.api.vo.Result;
import org.suhui.common.aspect.annotation.AutoLog;
import org.suhui.common.constant.CommonConstant;
import org.suhui.modules.bind.entity.SysUserBind;
import org.suhui.modules.bind.service.ISysUserBindService;
import org.suhui.modules.system.entity.SysUser;
import org.suhui.modules.system.service.ISysUserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/userbind")
@Api(tags="用户绑定")
@Slf4j
public class ApiUserBindController {

    @Autowired
    private ISysUserBindService sysUserBindService;

    @Autowired
    private ISysUserService sysUserService;

    /**
     *   添加
     * @param params
     * @return
     */
    @AutoLog(value = "绑定第三方账号")
    @ApiOperation(value="绑定第三方账号-添加", notes="绑定第三方账号-添加")
    @RequestMapping(value = "",method = RequestMethod.POST)
    public Result<JSONObject> add(HttpServletRequest request, @RequestBody Map<String, Object> params ){
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        String userId = params.get("userId")+"" ;
        String targetId = params.get("targetId")+"";
        int targetType=(int)params.get("targetType");
        //check
        SysUser sysUser=sysUserService.getById(userId);
        if(sysUser==null) {
            result.setResult(obj);
            result.success("has no user");
            result.setCode(0);
            return result;
        }
        //save
        SysUserBind entity=new SysUserBind();
        entity.setUserId(userId);
        entity.setTargetId(targetId);
        entity.setTargetType(targetType);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        sysUserBindService.save(entity);
        result.setResult(obj);
        result.success("bind success");
        result.setCode(CommonConstant.SC_OK_200);
        return result ;
    }

    /**
     *   修改
     * @param params
     * @return
     */
    @AutoLog(value = "修改用户绑定")
    @ApiOperation(value="绑定第三方账号-添加", notes="绑定第三方账号-添加")
    @RequestMapping(value = "{id}",method = RequestMethod.PUT)
    public Result<JSONObject> update(HttpServletRequest request,@PathVariable String id, @RequestBody Map<String, Object> params ){
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        String userId = params.get("userId")+"" ;
        String targetId = params.get("targetId")+"";
        int targetType=(int)params.get("targetType");
        //check
        SysUserBind entity=sysUserBindService.getById(id);
        if(entity==null){
            result.setResult(obj);
            result.success("has no userbind");
            result.setCode(0);
            return result;
        }
        SysUser sysUser=sysUserService.getById(userId);
        if(sysUser==null) {
            result.setResult(obj);
            result.success("has no user");
            result.setCode(0);
            return result;
        }
        //save
        entity.setUserId(userId);
        entity.setTargetId(targetId);
        entity.setTargetType(targetType);
        entity.setUpdateTime(new Date());
        sysUserBindService.updateById(entity);
        result.setResult(obj);
        result.success("bind success");
        result.setCode(CommonConstant.SC_OK_200);
        return result ;
    }

    /**
     *   删除
     * @return
     */
    @AutoLog(value = "删除用户绑定")
    @ApiOperation(value="绑定第三方账号-添加", notes="绑定第三方账号-添加")
    @RequestMapping(value = "{id}",method = RequestMethod.DELETE)
    public Result<JSONObject> delete(HttpServletRequest request,@PathVariable String id){
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        SysUserBind sysUserBind = sysUserBindService.getById(id);
        if(sysUserBind==null){
            result.setResult(obj);
            result.success("bind not found");
            result.setCode(0);
        }else{
            sysUserBindService.removeById(id);
            result.setResult(obj);
            result.success("delete success");
            result.setCode(CommonConstant.SC_OK_200);
        }
        return result ;
    }

    /**
     * 获取详情
     */
    @AutoLog(value = "获取绑定详情")
    @ApiOperation(value="获取绑定详情", notes="获取绑定详情")
    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    public Result<SysUserBind> detail(HttpServletRequest request,@PathVariable String id){
        Result<SysUserBind> result = new Result<SysUserBind>();
        SysUserBind sysUserBind = sysUserBindService.getById(id);
        if(sysUserBind==null){
            result.setResult(sysUserBind);
            result.success("bind not found");
            result.setCode(0);
        }else{
            result.setResult(sysUserBind);
            result.setCode(CommonConstant.SC_OK_200);
        }
        return result ;
    }







}
