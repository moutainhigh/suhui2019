package org.suhui.modules.api.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.suhui.common.api.vo.Result;
import org.suhui.common.constant.CommonConstant;
import org.suhui.common.system.api.ISysBaseAPI;
import org.suhui.common.system.util.JwtUtil;
import org.suhui.common.system.vo.LoginUser;
import org.suhui.common.util.PasswordUtil;
import org.suhui.common.util.RedisUtil;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.shiro.vo.DefContants;
import org.suhui.modules.system.entity.SysDepart;
import org.suhui.modules.system.entity.SysUser;
import org.suhui.modules.system.service.ISysDepartService;
import org.suhui.modules.system.service.ISysLogService;
import org.suhui.modules.system.service.ISysUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.*;

/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/old/api/user")
@Api(tags="用户登录")
@Slf4j
public class ApiUserController {
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
	 * 用户注册
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public Result<JSONObject> register(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params ) {
		//用户退出逻辑
		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();
		String phone = params.get("phone")+"" ;
		String username = params.get("username")+"" ;
		String pwd = params.get("pwd")+"" ;
		String email = params.get("email")+"" ;
		String smsCodeParam =  params.get("smsCode")+"" ;

		String areaCode = params.get("areaCode") +"" ;
		if(areaCode == null || areaCode.equals("")||areaCode.equals("null")){
			result.error500("please choose area code for your phone !");
			return result ;
		}

		String codeVaue = "" ;
		if(!phone.equals("null")&&!phone.equals("")){
			codeVaue = phone ;
		}else if(!email.equals("null")&&!email.equals("")){
			codeVaue = email ;
		}

		if(request.getSession(false) ==null){
			result.error500("verification Code is out of time");
			return result ;
		}
		String smsCode =  request.getSession().getAttribute("smsCode_"+codeVaue)+"" ;

		if(smsCodeParam.equals(smsCode)){

		}else{
			result.error500("verification Code is not right");
			return result ;
		}

		String role ="ee8626f80f7c2619917b6236f3a7f02b" ;
		SysUser sysUser = new SysUser() ;
		sysUser.setPhone(phone) ;
		sysUser.setUsername(username) ;
		sysUser.setEmail(email) ;

		sysUser.setAreaCode(areaCode) ;

		sysUser.setCreateTime(new Date());//设置创建时间
		String salt = oConvertUtils.randomGen(8);
		sysUser.setSalt(salt);

		String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), pwd+"", salt);
		sysUser.setPassword(passwordEncode);
		sysUser.setStatus(1);
		sysUser.setDelFlag("0");
		sysUser.setAvatar("user/20190119/logo-2_1547868176839.png") ;

		sysUserService.addUserWithRole(sysUser,role);

		result.setResult(obj);
		result.success("resign success");
		result.setCode(CommonConstant.SC_OK_200);
		return result ;
	}


	/**
	 * 注册时手机号唯一性校验
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/check-mobile", method = RequestMethod.POST)
	public Result<JSONObject> checkMobile(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params ) {
		//用户退出逻辑
		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();
		String phone = params.get("phone")+"" ;

		SysUser sysUser = sysUserService.getUserByName(phone);
		if(sysUser==null){
			sysUser = sysUserService.getByPhone(phone);
		}

		if(sysUser==null) {
			result.setResult(obj);
			result.success("phone is right");
			result.setCode(CommonConstant.SC_OK_200);
		}else{
			result.setResult(obj);
			result.success("phone is used by some one");
			result.setCode(0);
		}

		return result ;
	}



	/**
	 * 注册时邮箱唯一性校验
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/check-email", method = RequestMethod.POST)
	public Result<JSONObject> checkEmail(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params ) {

		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();
		String email = params.get("email")+"" ;

		SysUser sysUser = sysUserService.getByEmail(email);

		if(sysUser==null) {
			result.setResult(obj);
			result.success("email is right");
			result.setCode(CommonConstant.SC_OK_200);
		}else{
			result.setResult(obj);
			result.success("email is used by some one");
			result.setCode(0);
		}

		return result ;
	}



	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ApiOperation("登录接口")
	public Result<JSONObject> login(@RequestBody  Map<String, Object> params) {
		Result<JSONObject> result = new Result<JSONObject>();
		String username = params.get("username")+ "";
		String password = params.get("password")+ "";

		SysUser sysUser = sysUserService.getUserByName(username);
		if(sysUser==null){
			sysUser = sysUserService.getByPhone(username);
		}

		if(sysUser==null){
			sysUser = sysUserService.getByEmail(username);
		}

		if(sysUser==null) {
			result.error500("user not exist");
			sysBaseAPI.addLog("登录失败，用户名:"+username+"不存在！", CommonConstant.LOG_TYPE_1, null);
			return result;
		}else {
			username = sysUser.getUsername() ;
			//密码验证
			String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
			String syspassword = sysUser.getPassword();
			if(!syspassword.equals(userpassword)) {
				result.error500("user or password is wrong");
				return result;
			}
			//生成token
			String token = JwtUtil.sign(username, syspassword);
			redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
			//设置超时时间
			redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME/1000);

			//获取用户部门信息
			JSONObject obj = new JSONObject();
			List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
			obj.put("departs",departs);
			if(departs==null || departs.size()==0) {
				obj.put("multi_depart",0);
			}else if(departs.size()==1){
				sysUserService.updateUserDepart(username, departs.get(0).getOrgCode());
				obj.put("multi_depart",1);
			}else {
				obj.put("multi_depart",2);
			}
			obj.put("token", token);
			obj.put("userInfo", sysUser);
			result.setResult(obj);
			result.success("success");
			sysBaseAPI.addLog("用户名: "+username+",登录成功！", CommonConstant.LOG_TYPE_1, null);
		}
		return result;
	}



	/**
	 * 找回密码-重设密码
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/reset-password", method = RequestMethod.POST)
	public Result<JSONObject> resetPassword(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params ) {

		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();
		String phone = params.get("phone")+"" ;
		String pwd = params.get("pwd")+"" ;  // 密码
		String smsCodeParam =  params.get("smsCode")+"" ;

		if(request.getSession(false) ==null){
			result.error500("verification Code is out of time");
			return result ;
		}
		String smsCode =  request.getSession().getAttribute("smsCode_"+phone)+"" ;

		if(smsCodeParam.equals(smsCode)){

		}else{
			result.error500("verification Code is not right");
			return result ;
		}

		SysUser sysUser = sysUserService.getByPhone(phone);

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
	 * 找回密码-重设密码
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/deleteByPhone", method = RequestMethod.POST)
	public Result<JSONObject> deleteByPhone(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params ) {

		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();
		String phone = params.get("phone") + "";

		SysUser sysUser = sysUserService.getByPhone(phone);



		if (sysUser == null) {

			result.success("phone is not used!");
			result.setCode(CommonConstant.SC_OK_200);
		} else {
			sysUserService.removeById(sysUser.getId()) ;

			result.success("delete success!");
			result.setCode(0);
		}

		return result ;
	}
}
