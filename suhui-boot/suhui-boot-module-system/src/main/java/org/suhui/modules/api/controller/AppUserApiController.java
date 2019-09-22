package org.suhui.modules.api.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.suhui.common.api.vo.Result;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

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

    @Autowired
    private IPayAccountService iPayAccountService ;

    @Autowired
    private IPayIdentityInfoService iPayIdentityInfoService ;

    @Autowired
    private IPayAccountAssetService iPayAccountAssetService ;

//    @RequestMapping(value = "/register", method = RequestMethod.POST)
//    @Transactional
//    public Result<JSONObject> register(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {
//
//        Result<JSONObject> result = new Result<JSONObject>();
//        JSONObject obj = new JSONObject();
//        String phone = params.get("phone")+"" ;
//       // String username = params.get("username")+"" ;
//        String pwd = params.get("pwd")+"" ;
//        String email = params.get("email")+"" ;
//        String smsCodeParam =  params.get("smscode")+"" ; // 短信验证码
//
//        String areacode = params.get("areacode") +"" ; // 区域代码
//        if(areacode == null || areacode.equals("")||areacode.equals("null")){
//            result.error500("please choose area code for your phone !");
//            return result ;
//        }
//
//        String codeVaue = "" ;
//        if(!phone.equals("null")&&!phone.equals("")){
//            codeVaue = phone ;
//        }else if(!email.equals("null")&&!email.equals("")){
//            codeVaue = email ;
//        }
//
//        if(request.getSession(false) ==null){
//            result.error500("sms code is out of time");
//            return result ;
//        }
//        String smsCode =  request.getSession().getAttribute("smsCode_"+codeVaue)+"" ;
//
//        if(smsCodeParam.equals(smsCode)){
//
//        }else{
//            result.error500("sms code is not right");
//            return result ;
//        }
//
//        String userno = UUIDGenerator.generate() ;
//        // 盐值
//        String salt = oConvertUtils.randomGen(8);
//        // 密码加密
//        String passwordEncode = PasswordUtil.encrypt(phone, pwd+"", salt);
//        PayUserLogin payUserLogin = new PayUserLogin() ;
//        payUserLogin.setLoginName(phone);
//        payUserLogin.setPassword(passwordEncode);
//        payUserLogin.setSalt(salt);
//        // 设置有效状态  状态 0-默认 1-有效 2-无效
//        payUserLogin.setStatus(0);
//        payUserLogin.setUserNo(userno); // 通过生成的uuid
//        payUserLogin.setAreacode(areacode);
//        iPayUserLoginService.save(payUserLogin) ;
//
//        PayUserInfo payUserInfo = new PayUserInfo() ;
//        payUserInfo.setUserNo(userno);
//        payUserInfo.setPhoneNo(phone);
//
//        iPayUserInfoService.save(payUserInfo) ;
//        return result ;
//    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @Transactional
    public Result<JSONObject> register(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {

        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        String phone = params.get("phone")+"" ;
        String username = params.get("username")+"" ;
        String pwd = params.get("pwd")+"" ;
        String email = params.get("email")+"" ;
        String smsCodeParam =  params.get("smscode")+"" ; // 短信验证码



        String areacode = params.get("areacode") +"" ; // 区域代码
        if(areacode == null || areacode.equals("")||areacode.equals("null")){
            result.error500("please choose area code for your phone !");
            return result ;
        }

        PayUserLogin payUserLoginCheck= iPayUserLoginService.getUserByPhone(phone ,areacode) ;
        if(payUserLoginCheck != null ){
            result.error500("the phone is exit !");
            return result ;
        }


        String codeVaue = "" ;
        if(!phone.equals("null")&&!phone.equals("")){
            codeVaue = phone ;
        }else if(!email.equals("null")&&!email.equals("")){
            codeVaue = email ;
        }

        if(request.getSession(false) ==null){
            result.error500("sms code is out of time");
            return result ;
        }
        String smsCode =  request.getSession().getAttribute("smsCode_"+codeVaue)+"" ;

        if(smsCodeParam.equals(smsCode)){

        }else{
            result.error500("sms code is not right");
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
        payUserLogin.setAreacode(areacode);
        iPayUserLoginService.save(payUserLogin) ;

        PayUserInfo payUserInfo = new PayUserInfo() ;
        payUserInfo.setUserNo(userno);
        payUserInfo.setPhoneNo(phone);

        iPayUserInfoService.save(payUserInfo) ;

        List list = new ArrayList() ;
        Map map1 = new HashMap() ;
        map1.put("accounttypecode" ,"0101") ;//人民币账户
        map1.put("accountname" ,"CNY") ;//人民币账户
        map1.put("currencycode","1") ;
        list.add(map1) ;
        Map map2 = new HashMap() ;
        map2.put("accounttypecode" ,"0201") ;//人民币账户
        map2.put("accountname" ,"USD") ;//人民币账户
        map2.put("currencycode","2") ;
        list.add(map2) ;
        Map map3 = new HashMap() ;
        map3.put("accounttypecode" ,"0301") ;//人民币账户
        map3.put("accountname" ,"PHP") ;//人民币账户
        map3.put("currencycode" ,"3") ;
        list.add(map3) ;
        try{
            for(int i = 0 ; i < list.size() ; i++){

                Map mapparam = (Map)list.get(i) ;

                String usertype = "0" ; // 当前usertype 全部都设置成0

                String status = "0" ; // 默认是正常状态 0
                String identityno =  UUIDGenerator.generate() ;  // 身份编码
                String identitytype = "0" ; // 身份类型 默认0

                String accountno  = UUIDGenerator.generate() ; //  账户编码
                String accounttypecode = mapparam.get("accounttypecode")+"" ; //账户类型编码
                String accountname = mapparam.get("accountname")+"" ;//账户payCharge名称

                String isallowrecharge =  "0" ; //是否允许充值
                String isallowwithdraw =  "0" ;//是否允许提现
                String isallowoverdraft = "1" ; //是否允许透支  1 表示否
                String isallowtransferin =  "0" ; //是否允许转账转入
                String isallowtransferout = "0" ; //是否允许转账转入

                String isfrozen =  "0" ; //是否冻结，表示冻结账户

                PayIdentityInfo payIdentityInfo = new PayIdentityInfo() ;
                payIdentityInfo.setIdentityNo(identityno) ;
                payIdentityInfo.setIdentityType(Integer.parseInt(identitytype)) ;
                payIdentityInfo.setUserNo(userno) ;
                payIdentityInfo.setUserType(Integer.parseInt(usertype)) ;
                payIdentityInfo.setUserName(username) ;
                payIdentityInfo.setStatus(Integer.parseInt(status)) ;


                PayAccount payAccount = new PayAccount() ;
                payAccount.setIdentityNo(identityno) ;
                payAccount.setIdentityType(Integer.parseInt(identitytype)) ;
                payAccount.setAccountName(accountname) ;
                payAccount.setAccountNo(accountno) ;

                payAccount.setIsAllowRecharge(Integer.parseInt(isallowrecharge)) ;
                payAccount.setIsAllowWithdraw(Integer.parseInt(isallowwithdraw)) ;
                payAccount.setIsAllowOverdraft(Integer.parseInt(isallowoverdraft)) ;
                payAccount.setIsAllowTransferIn(Integer.parseInt(isallowtransferin)) ;
                payAccount.setIsAllowTransferOut(Integer.parseInt(isallowtransferout)) ;
                payAccount.setIsFrozen(Integer.parseInt(isfrozen)) ;
                payAccount.setStatus(Integer.parseInt(status)) ;
                payAccount.setAccountTypeCode(Integer.parseInt(accounttypecode)) ;

                Map map = new HashMap() ;
                map.put("user_no" ,userno);
                map.put("user_type" ,usertype);
                Map<String, Object> identityMap = iPayIdentityInfoService.getIdentityInfoByUserNo(map) ;
                if(identityMap == null){
                    iPayIdentityInfoService.save(payIdentityInfo) ;
                }else{
                    payAccount.setIdentityNo(identityMap.get("identity_no")+"") ;
                }
                /**  保存 支付账号信息  和身份信息 */
                iPayAccountService.save(payAccount) ;

                String availableamount = "0" ;
                String frozenamount = "0" ;
                String currencycode = mapparam.get("currencycode")+"" ; //货币单位

                PayAccountAsset payAccountAsset = new PayAccountAsset() ;
                payAccountAsset.setAccountNo(accountno) ;
                payAccountAsset.setIdentityNo(identityno) ;
                payAccountAsset.setAccountTypeCode(Integer.parseInt(accounttypecode)) ;
                payAccountAsset.setAvailableAmount(Integer.parseInt(availableamount)) ;

                payAccountAsset.setFrozenAmount(Integer.parseInt(frozenamount)) ;
                payAccountAsset.setCurrencyCode(Integer.parseInt(currencycode)) ;

                iPayAccountAssetService.save(payAccountAsset) ;

            }
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("操作失败  Operation failed");
            return result ;
        }

        result.setResult(obj);
        result.success("添加成功  Operation succeeded");
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
        String smsCodeParam =  params.get("smscode")+"" ;
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
