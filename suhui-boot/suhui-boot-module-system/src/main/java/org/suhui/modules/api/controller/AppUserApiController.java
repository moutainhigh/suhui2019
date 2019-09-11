package org.suhui.modules.api.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.suhui.common.api.vo.Result;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.suhui.common.constant.CommonConstant;
import org.suhui.common.system.api.ISysBaseAPI;
import org.suhui.common.system.util.JwtUtil;
import org.suhui.common.util.PasswordUtil;
import org.suhui.common.util.RedisUtil;
import org.suhui.common.util.UUIDGenerator;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.suhui.suhui.entity.*;
import org.suhui.modules.suhui.suhui.service.*;
import org.suhui.modules.system.entity.SysDepart;
import org.suhui.modules.system.entity.SysUser;

@RestController
@RequestMapping("/api/user")
@Api(tags="用户登录")
@Slf4j
public class AppUserApiController {

    @Autowired
    private IPayUserLoginService iPayUserLoginService ;

    @Autowired
    private IPayUserInfoService iPayUserInfoService ;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result<JSONObject> register(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {

        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        String phone = params.get("phone")+"" ;
       // String username = params.get("username")+"" ;
        String pwd = params.get("pwd")+"" ;
        String email = params.get("email")+"" ;
        String smsCodeParam =  params.get("smsCode")+"" ; // 短信验证码

        String areaCode = params.get("areaCode") +"" ; // 区域代码
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
            result.error500("验证码过期");
            return result ;
        }
        String smsCode =  request.getSession().getAttribute("smsCode_"+codeVaue)+"" ;

        if(smsCodeParam.equals(smsCode)){

        }else{
            result.error500("验证码不正确");
            return result ;
        }

        String userno = UUIDGenerator.generate() ;
        // 盐值
        String salt = oConvertUtils.randomGen(8);
        // 密码加密
        String passwordEncode = PasswordUtil.encrypt(phone, pwd+"", salt);
        PayUserLogin payUserLogin = new PayUserLogin() ;
        payUserLogin.setLoginName(phone);
        payUserLogin.setPassword(passwordEncode);
        payUserLogin.setSalt(salt);
        // 设置有效状态  状态 0-默认 1-有效 2-无效
        payUserLogin.setStatus(0);
        payUserLogin.setUserNo(userno); // 通过生成的uuid
        iPayUserLoginService.save(payUserLogin) ;

        PayUserInfo payUserInfo = new PayUserInfo() ;
        payUserInfo.setUserNo(userno);
        payUserInfo.setPhoneNo(phone);

        iPayUserInfoService.save(payUserInfo) ;
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
        String areacode = params.get("areacode")+"" ;
//  校验注册的用户名 是否是手机号
        PayUserLogin payUserLogindb = iPayUserLoginService.getUserByPhone(phone,areacode) ;


        if(payUserLogindb==null) {
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


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation("登录接口")
    public Result<JSONObject> login(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params) {
        Result<JSONObject> result = new Result<JSONObject>();
        String phone = params.get("phone")+ "";
        String pwd = params.get("pwd")+ "";
        String areacode =  params.get("areacode")+ "";

        PayUserLogin payUserLogin = iPayUserLoginService.getUserByPhone(phone,areacode) ;

//        SysUser sysUser = sysUserService.getUserByName(phone);
//        if(sysUser==null){
//            sysUser = sysUserService.getByPhone(username);
//        }
//
//        if(sysUser==null){
//            sysUser = sysUserService.getByEmail(username);
//        }

        if(payUserLogin==null) {
            result.error500("该用户不存在");
            log.error("登录失败，用户名:"+phone+"不存在！", CommonConstant.LOG_TYPE_1, null);
            return result;
        }else {
            phone = payUserLogin.getLoginName() ;//  用户名 存的就是电话
            //密码验证
            String userpassword = PasswordUtil.encrypt(phone, pwd, payUserLogin.getSalt());
            String syspassword = payUserLogin.getPassword();
            if(!syspassword.equals(userpassword)) {
                result.error500("用户名或密码错误");
                return result;
            }
            //生成token
            String token = JwtUtil.sign(phone, syspassword);
            redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
            //设置超时时间
            redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME/1000);
            JSONObject obj = new JSONObject();

            obj.put("token", token);
            obj.put("payUserLogin", payUserLogin);
            result.setResult(obj);
            result.success("登录成功");
            sysBaseAPI.addLog("用户名: "+phone+",登录成功！", CommonConstant.LOG_TYPE_1, null);
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
        String areacode =  params.get("areacode")+"" ;

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
        PayUserLogin payUserLogin = iPayUserLoginService.getUserByPhone(phone,areacode) ;

        if(payUserLogin==null) {
            result.setResult(obj);
            result.success("has no user");
            result.setCode(0);
        }else{

            String salt = payUserLogin.getSalt() ;
            String passwordEncode = PasswordUtil.encrypt(payUserLogin.getLoginName(), pwd+"", salt);
            payUserLogin.setPassword(passwordEncode) ;
            iPayUserLoginService.updateById(payUserLogin) ;
            result.setResult(obj);
            result.success("update password success!");
            result.setCode(CommonConstant.SC_OK_200);
        }

        return result ;
    }

}
