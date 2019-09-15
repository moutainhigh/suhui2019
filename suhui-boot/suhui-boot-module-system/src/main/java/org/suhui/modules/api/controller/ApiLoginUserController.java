package org.suhui.modules.api.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.suhui.common.api.vo.Result;
import org.suhui.common.constant.CommonConstant;
import org.suhui.common.system.api.ISysBaseAPI;
import org.suhui.common.system.util.JwtUtil;
import org.suhui.common.util.PasswordUtil;
import org.suhui.common.util.RedisUtil;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.system.entity.SysDepart;
import org.suhui.modules.system.entity.SysUser;
import org.suhui.modules.system.service.ISysDepartService;
import org.suhui.modules.system.service.ISysLogService;
import org.suhui.modules.system.service.ISysUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/old/api/login/user")
@Api(tags="用户登录")
@Slf4j
public class ApiLoginUserController {
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	@Autowired
	private ISysLogService logService;
	@Autowired
    private RedisUtil redisUtil;
	@Autowired
    private ISysDepartService sysDepartService;

	/**
	 * 找回密码-重设密码
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/reset-new-password", method = RequestMethod.POST)
	public Result<JSONObject> resetNewPassword(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params ) {

		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();
		String id = params.get("id")+"" ;
		String pwd = params.get("pwd")+"" ;

		SysUser sysUser = sysUserService.getById(id) ;

		if(sysUser==null) {
			result.setResult(obj);
			result.success("has no user");
			result.setCode(0);
		}else{

			String salt = sysUser.getSalt() ;
			String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), pwd+"", salt);
			sysUser.setPassword(passwordEncode) ;

			sysUserService.updateById(sysUser) ;

			result.setResult(obj);
			result.success("update password success!");
			result.setCode(CommonConstant.SC_OK_200);
		}

		return result ;
	}


	/**
	 * 修改支付密码
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/set-new-paypassword", method = RequestMethod.POST)
	public Result<JSONObject> setNewPaypassword(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params ) {

		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();
		String id = params.get("id")+"" ;
		String paypwd = params.get("paypwd")+"" ;
		String oldpaypwd =  params.get("oldpaypwd")+"" ; // old pay password

		SysUser sysUser = sysUserService.getById(id) ;
		String salt = sysUser.getSalt() ;
		String paypassword = sysUser.getPayPassword() ;

		String oldpaypwdEncode = PasswordUtil.encrypt(sysUser.getUsername(), oldpaypwd+"", salt);

		if(!oldpaypwdEncode.equals(paypassword)){
			result.error500("old pay password is not right , please check it!");
			return result ;
		}

		if(sysUser==null) {
			result.setResult(obj);
			result.success("has no user");
			result.setCode(0);
		}else{

			String payPasswordEncode = PasswordUtil.encrypt(sysUser.getUsername(), paypwd+"", salt);
			sysUser.setPayPassword(payPasswordEncode) ;

			sysUserService.updateById(sysUser) ;

			result.setResult(obj);
			result.success("update pay password success!");
			result.setCode(CommonConstant.SC_OK_200);
		}

		return result ;
	}


	/**
	 * 设置支付密码
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/set_paypassword", method = RequestMethod.POST)
	public Result<JSONObject> setPaypassword(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params ) {

		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();
		String id = params.get("id")+"" ;
		String paypwd = params.get("paypwd")+"" ;

		SysUser sysUser = sysUserService.getById(id) ;

		if(sysUser==null) {
			result.setResult(obj);
			result.success("has no user");
			result.setCode(0);
		}else{

			String salt = sysUser.getSalt() ;
			String payPasswordEncode = PasswordUtil.encrypt(sysUser.getUsername(), paypwd+"", salt);
			sysUser.setPayPassword(payPasswordEncode) ;

			sysUserService.updateById(sysUser) ;

			result.setResult(obj);
			result.success("update pay password success!");
			result.setCode(CommonConstant.SC_OK_200);
		}

		return result ;
	}


	/**
	 * 确认支付密码 接口
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/confirm_paypassword", method = RequestMethod.POST)
	public Result<JSONObject> confirmPaypassword(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params ) {

		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();
		String id = params.get("id")+"" ;
		String paypwd = params.get("paypwd")+"" ;
		SysUser sysUser = sysUserService.getById(id) ;
		if(sysUser==null) {
			result.setResult(obj);
			result.success("has no user");
			result.setCode(0);
		}else{

			String salt = sysUser.getSalt() ;
			String payPasswordEncode = PasswordUtil.encrypt(sysUser.getUsername(), paypwd+"", salt);

			if(payPasswordEncode.equals(sysUser.getPayPassword())){
				result.success("pay password is right");
				result.setCode(CommonConstant.SC_OK_200);
			}else{
				result.success("pay password is not right");
				result.setCode(0);
			}
		}

		return result ;
	}

	/**
	 * 修改资料
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Result<JSONObject> update(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params ) {

		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();
		String id = params.get("id")+"" ;
		String realname = params.get("realname")+"" ;
		String sex = params.get("sex")+"" ;

		SysUser sysUser = sysUserService.getById(id) ;

		if(sysUser==null) {
			result.setResult(obj);
			result.success("has no user");
			result.setCode(0);
		}else{
			sysUser.setRealname(realname) ; //real name

			sysUser.setSex(Integer.parseInt(sex)) ;
			sysUserService.updateById(sysUser) ;
			result.setResult(obj);
			result.success("update pay password success!");
			result.setCode(CommonConstant.SC_OK_200);
		}

		return result ;
	}

	/**
	 * 查看个人资料
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/get-info", method = RequestMethod.POST)
	public Result<JSONObject> getInfo(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params ) {

		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();
		String id = params.get("id")+"" ;

		SysUser sysUser = sysUserService.getById(id) ;

		if(sysUser==null) {
			result.setResult(obj);
			result.success("has no user");
			result.setCode(0);
		}else{

			result.setResult(obj);
			result.success("update pay password success!");
			result.setCode(CommonConstant.SC_OK_200);
		}

		return result ;
	}



	/**
	 * 身份证名和身份证号码校验
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/check-role", method = RequestMethod.POST)
	public Result<JSONObject> checkRole(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params ) {

		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();
		String id = params.get("id")+"" ;
		String state = params.get("state")+"" ;

		SysUser sysUser = sysUserService.getById(id) ;

		if(sysUser==null) {
			result.setResult(obj);
			result.success("has no user");
			result.setCode(0);
		}else{
			// 审核状态(1：通过  2：不通过 ）
			sysUser.setCheckStatus(Integer.parseInt(state)) ;

			sysUserService.updateById(sysUser) ;
			result.setResult(obj);
			result.success("update check role success!");
			result.setCode(CommonConstant.SC_OK_200);
		}

		return result ;
	}

}
