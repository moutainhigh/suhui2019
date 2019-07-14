package org.suhui.modules.system.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.suhui.common.api.vo.Result;
import org.suhui.common.constant.CommonConstant;
import org.suhui.common.system.api.ISysBaseAPI;
import org.suhui.common.system.util.JwtUtil;
import org.suhui.common.system.vo.LoginUser;
import org.suhui.common.util.PasswordUtil;
import org.suhui.common.util.RedisUtil;
import org.suhui.modules.shiro.vo.DefContants;
import org.suhui.modules.system.entity.SysDepart;
import org.suhui.modules.system.entity.SysUser;
import org.suhui.modules.system.model.SysLoginModel;
import org.suhui.modules.system.service.ISysDepartService;
import org.suhui.modules.system.service.ISysLogService;
import org.suhui.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/sys")
@Api(tags="用户登录")
@Slf4j
public class LoginController {
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

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ApiOperation("登录接口")
	public Result<JSONObject> login(@RequestBody  Map<String, Object> params) {
		Result<JSONObject> result = new Result<JSONObject>();
		String username = params.get("username")+ "";
		String password = params.get("password")+ "";
//		String username = sysLoginModel.getUsername();
//		String password = sysLoginModel.getPassword();
		SysUser sysUser = sysUserService.getUserByName(username);
		if(sysUser==null){
			sysUser = sysUserService.getByPhone(username);
		}

		if(sysUser==null) {
			result.error500("该用户不存在");
			sysBaseAPI.addLog("登录失败，用户名:"+username+"不存在！", CommonConstant.LOG_TYPE_1, null);
			return result;
		}else {
			username = sysUser.getUsername() ;
			//密码验证
			String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
			String syspassword = sysUser.getPassword();
			if(!syspassword.equals(userpassword)) {
				result.error500("用户名或密码错误");
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
			result.success("登录成功");
			sysBaseAPI.addLog("用户名: "+username+",登录成功！", CommonConstant.LOG_TYPE_1, null);
		}
		return result;
	}

	
	/**
	 * 退出登录
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/logout")
	public Result<Object> logout(HttpServletRequest request,HttpServletResponse response) {
		//用户退出逻辑
		Subject subject = SecurityUtils.getSubject();
		LoginUser sysUser = (LoginUser)subject.getPrincipal();
		sysBaseAPI.addLog("用户名: "+sysUser.getRealname()+",退出成功！", CommonConstant.LOG_TYPE_1, null);
		log.info(" 用户名:  "+sysUser.getRealname()+",退出成功！ ");
	    subject.logout();

	    String token = request.getHeader(DefContants.X_ACCESS_TOKEN);
	    //清空用户Token缓存
	    redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
	    //清空用户权限缓存：权限Perms和角色集合
	    redisUtil.del(CommonConstant.LOGIN_USER_CACHERULES_ROLE + sysUser.getUsername());
	    redisUtil.del(CommonConstant.LOGIN_USER_CACHERULES_PERMISSION + sysUser.getUsername());
		return Result.ok("退出登录成功！");
	}

	/**
	 * 获取验证码
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/sms" , method = RequestMethod.POST)
	public Result<JSONObject> sms(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params) {
		//用户退出逻辑
		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();

		Random random = new Random();
		int x = random.nextInt(899999);
		x = x+100000;
		System.out.println(x+"");

		String phone = params.get("phone")+"" ;
		HttpSession session = request.getSession();
		session.setAttribute("smsCode_"+phone ,x);
		session.setMaxInactiveInterval(30);

		obj.put("smsCode", x+"");
		result.setResult(obj);
		result.success("发送验证码成功");
		result.setCode(CommonConstant.SC_OK_200);
		return result ;
	}


	public static void sendSms(String url) {
		System.out.println(url + ">>>>>>>>>>>>>>" );

		CloseableHttpClient httpClient = HttpClients.createDefault();

		try {
			URIBuilder uriBuilder = new URIBuilder(url);

			HttpGet httpGet = new HttpGet(uriBuilder.build());

			CloseableHttpResponse response = httpClient.execute(httpGet);

			String retrunStatus= EntityUtils.toString(response.getEntity(), "UTF-8");

			System.out.println(retrunStatus);
			int intStatus = Integer.parseInt(retrunStatus) ;
			if (intStatus>=0)
			{
				System.out.println("发送成功！！");
			}
			else if(intStatus==-1)
			{
				System.out.println("帐号未注册！");
			}
			else if(intStatus==-2)
			{
				System.out.println("其他错误！");
			}
			else if(intStatus==-3)
			{
				System.out.println("帐号或密码错误！");
			}
			else if(intStatus==-5)
			{
				System.out.println("企业号帐户余额不足，请先充值再提交短信息！");
			}
			else if(intStatus==-6)
			{
				System.out.println("定时发送时间不是有效时间格式！");
			}
			else if(intStatus==-7)
			{
				System.out.println("提交信息末尾未加签名，请添加中文的企业签名【 】！");
			}

			else if(intStatus==-8)
			{
				System.out.println("发送内容需在1到300个字之间");
			}
			else if(intStatus==-9)
			{
				System.out.println("发送号码为空");

			}
			else if(intStatus==-10)
			{
				System.out.println("定时时间不能小于当前系统时间！");
			}
			else if(intStatus==-101)
			{
				System.out.println("调用接口速度太快");
			}

		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws ParseException {

		Random random = new Random();
		int result = random.nextInt(899999);
		result = result +100000;
		System.out.println(result+"");
		String phone = "15550034527" ;
		String url = null;
		String yzm = "您的验证码"+result+"，该验证码5分钟内有效，请勿泄漏于他人！" ;
		try {
			url = "https://sdk2.028lk.com/sdk2/BatchSend2.aspx?CorpID=JNJS005972&Pwd=zm0513@&Mobile="+phone+"&Content="+ URLEncoder.encode(yzm, "GB2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//注册
		LoginController.sendSms(url);
	}

	/**
	 * 用户注册
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public Result<JSONObject> register(HttpServletRequest request, HttpServletResponse response,@RequestBody SysLoginModel sysLoginModel ) {
		//用户退出逻辑
		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();

		String smsCode =  request.getSession().getAttribute("smsCode")+"" ;
		System.out.println(smsCode+">>>>>>>>>>>>>>>>>>>>");



		result.setResult(obj);
		result.success("注册成功");
		result.setCode(CommonConstant.SC_OK_200);
		return result ;
	}


    /**
     *通过电话登陆
     * @param sysLoginModel
     * @return
     */
    @RequestMapping(value = "/loginByPhone", method = RequestMethod.POST)
    @ApiOperation("登录接口")
    public Result<JSONObject> loginByPhone(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params) {
        Result<JSONObject> result = new Result<JSONObject>();
        String phone = params.get("phone")+"" ;
        String smsCodeParam =  params.get("smsCode")+"" ;

        if(request.getSession(false) ==null){
			result.error500("验证码过期");
			return result ;
		}
        String smsCode =  request.getSession().getAttribute("smsCode_"+phone)+"" ;

        if(smsCodeParam.equals(smsCode)){

		}else{
			result.error500("验证码不正确");
			return result ;
		}
        SysUser sysUser = sysUserService.getByPhone(phone);
        if(sysUser==null) {
            result.error500("该用户不存在");
            sysBaseAPI.addLog("登录失败，用户名:"+sysUser.getUsername()+"不存在！", CommonConstant.LOG_TYPE_1, null);
            return result;
        }else {

            //生成token
            String token = JwtUtil.sign(sysUser.getUsername(), sysUser.getPassword());
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
                sysUserService.updateUserDepart(sysUser.getUsername(), departs.get(0).getOrgCode());
                obj.put("multi_depart",1);
            }else {
                obj.put("multi_depart",2);
            }
            obj.put("token", token);
            obj.put("userInfo", sysUser);
            result.setResult(obj);
            result.success("登录成功");
            sysBaseAPI.addLog("用户名: "+sysUser.getUsername()+",登录成功！", CommonConstant.LOG_TYPE_1, null);
        }
		request.getSession().removeAttribute("smsCode_"+phone);
        return result;
    }

	/**
	 * 获取访问量
	 * @return
	 */
	@GetMapping("loginfo")
	public Result<JSONObject> loginfo() {
		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();
		//update-begin--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数
		// 获取一天的开始和结束时间
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date dayStart = calendar.getTime();
        calendar.add(calendar.DATE, 1);
        Date dayEnd = calendar.getTime();
		// 获取系统访问记录
		Long totalVisitCount = logService.findTotalVisitCount();
		obj.put("totalVisitCount", totalVisitCount);
		Long todayVisitCount = logService.findTodayVisitCount(dayStart,dayEnd);
		obj.put("todayVisitCount", todayVisitCount);
		Long todayIp = logService.findTodayIp(dayStart,dayEnd);
		//update-end--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数
		obj.put("todayIp", todayIp);
		result.setResult(obj);
		result.success("登录成功");
		return result;
	}
	
	/**
	 * 登陆成功选择用户当前部门
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/selectDepart", method = RequestMethod.PUT)
	public Result<?> selectDepart(@RequestBody SysUser user) {
		String username = user.getUsername();
		String orgCode= user.getOrgCode();
		this.sysUserService.updateUserDepart(username, orgCode);
		return Result.ok();
	}


}
