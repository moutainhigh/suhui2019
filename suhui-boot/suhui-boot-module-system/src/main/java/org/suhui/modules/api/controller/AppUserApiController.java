package org.suhui.modules.api.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.suhui.common.api.vo.Result;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.suhui.common.constant.CommonConstant;
import org.suhui.common.util.PasswordUtil;
import org.suhui.common.util.UUIDGenerator;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.suhui.suhui.entity.*;
import org.suhui.modules.suhui.suhui.service.*;

@RestController
@RequestMapping("/api/user")
@Api(tags="用户登录")
@Slf4j
public class AppUserApiController {

    @Autowired
    private IPayUserLoginService iPayUserLoginService ;

    @Autowired
    private IPayUserInfoService iPayUserInfoService ;

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







}
