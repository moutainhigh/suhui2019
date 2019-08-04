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
import org.suhui.common.constant.CommonConstant;
import org.suhui.common.util.PasswordUtil;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.suhui.suhui.service.IPayUserInfoService;
import org.suhui.modules.suhui.suhui.entity.* ;
import org.suhui.modules.suhui.suhui.service.IPayUserLoginService;
import org.suhui.modules.system.entity.SysUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;



/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/api/login/user")
@Api(tags="用户登录")
@Slf4j
public class AppLoginUserApiController {

    @Autowired
    private IPayUserLoginService iPayUserLoginService ;

    @Autowired
    private IPayUserInfoService iPayUserInfoService ;

    /**
     * 身份证名和身份证号码校验
     * @param params
     * @return
     */
    @RequestMapping(value = "/check-role", method = RequestMethod.POST)
    public Result<JSONObject> checkRole(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {

        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        String id = params.get("id")+"" ;
        String status = params.get("status")+"" ;

        PayUserInfo payUserInfo = iPayUserInfoService.getById(id) ;

        if(payUserInfo==null) {
            result.setResult(obj);
            result.success("has no user");
            result.setCode(0);
        }else{
            // 用户身份认证 0-未实名认证 1-已经实名认证
            payUserInfo.setCardCheck(Integer.parseInt(status));
            iPayUserInfoService.updateById(payUserInfo) ;
            result.setResult(obj);
            result.success("update check role success!");
            result.setCode(CommonConstant.SC_OK_200);
        }

        return result ;
    }



    /**
     * 修改用户电话   登陆表中的login_name也会跟着修改
     * @param params
     * @return
     */
    @RequestMapping(value = "/change-phone", method = RequestMethod.POST)
    public Result<JSONObject> changePhone(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {

        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        String phone = params.get("phone")+"" ;
        String id = params.get("id")+"" ;

        PayUserLogin payUserLogin = iPayUserLoginService.getById(id) ;
        payUserLogin.setLoginName(phone);
        iPayUserLoginService.updateById(payUserLogin) ;

        String userno = payUserLogin.getUserNo() ;
        Integer usertype = payUserLogin.getUserType() ;

        PayUserInfo payUserInfo = new PayUserInfo() ;
        payUserInfo.setUserNo(userno);
        payUserInfo.setUserType(usertype);

        PayUserInfo payUserInfoDb = iPayUserInfoService.getUserByObj(payUserInfo) ;
        payUserInfoDb.setPhoneNo(phone);
        iPayUserInfoService.updateById(payUserInfoDb) ;

        result.success("update phone success!");
        return result ;
    }


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

        PayUserLogin payUserLogin = iPayUserLoginService.getById(id) ;

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
        PayUserLogin payUserLogin = iPayUserLoginService.getById(id) ;
        if(payUserLogin == null){
            result.setResult(obj);
            result.success("has no user");
            result.setCode(0);
            return result ;
        }
        String userno = payUserLogin.getUserNo() ;
        Integer usertype = payUserLogin.getUserType() ;

        String paypwd = params.get("paypwd")+"" ;

        PayUserInfo payUserInfo = new PayUserInfo() ;
        payUserInfo.setUserNo(userno);
        payUserInfo.setUserType(usertype);
        PayUserInfo payUserInfoDb = iPayUserInfoService.getUserByObj(payUserInfo) ;


        if(payUserInfoDb==null) {
            result.setResult(obj);
            result.success("has no user");
            result.setCode(0);
        }else{

            String salt = payUserInfoDb.getSalt() ;
            if(salt == null || salt.equals("")){
                salt = oConvertUtils.randomGen(8);
            }

            String payPasswordEncode = PasswordUtil.encrypt(payUserInfoDb.getPhoneNo(), paypwd+"", salt);
            payUserInfoDb.setPayPassword(payPasswordEncode) ;
            payUserInfoDb.setSalt(salt);
            iPayUserInfoService.updateById(payUserInfoDb) ;

            result.setResult(obj);
            result.success("update pay password success!");
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
        PayUserLogin payUserLogin = iPayUserLoginService.getById(id) ;
        if(payUserLogin == null){
            result.setResult(obj);
            result.success("has no user");
            result.setCode(0);
            return result ;
        }
        String userno = payUserLogin.getUserNo() ;
        Integer usertype = payUserLogin.getUserType() ;

        String paypwd = params.get("paypwd")+"" ;
        String oldpaypwd =  params.get("oldpaypwd")+"" ; // old pay password

        PayUserInfo payUserInfo = new PayUserInfo() ;
        payUserInfo.setUserNo(userno);
        payUserInfo.setUserType(usertype);
        PayUserInfo payUserInfoDb = iPayUserInfoService.getUserByObj(payUserInfo) ;


        String salt = payUserInfoDb.getSalt() ;
        String paypassword = payUserInfoDb.getPayPassword() ;
        String oldpaypwdEncode = PasswordUtil.encrypt(payUserInfoDb.getPhoneNo(), oldpaypwd+"", salt);

        if(!oldpaypwdEncode.equals(paypassword)){
            result.error500("old pay password is not right , please check it!");
            return result ;
        }

        if(payUserInfoDb==null) {
            result.setResult(obj);
            result.success("has no user");
            result.setCode(0);
        }else{
            String payPasswordEncode = PasswordUtil.encrypt(payUserInfoDb.getPhoneNo(), paypwd+"", salt);
            payUserInfoDb.setPayPassword(payPasswordEncode) ;

            iPayUserInfoService.updateById(payUserInfoDb) ;

            result.setResult(obj);
            result.success("update pay password success!");
            result.setCode(CommonConstant.SC_OK_200);

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
        String userName = params.get("userName")+"" ; //用户真实姓名
        String cardType = params.get("cardType")+"" ;//证件类型 1-身份证 2-军官证 3-护照
        String cardNo = params.get("cardNo")+"" ; //证件号码
        String phoneNo = params.get("phoneNo")+"" ;
        String email = params.get("email")+"" ;
        String sex = params.get("sex")+"" ;
        String birthday = params.get("birthday")+"" ;

        PayUserLogin payUserLogin = iPayUserLoginService.getById(id) ;
        if(payUserLogin == null){
            result.setResult(obj);
            result.success("has no user");
            result.setCode(0);
            return result ;
        }
        String userno = payUserLogin.getUserNo() ;
        Integer usertype = payUserLogin.getUserType() ;

        String paypwd = params.get("paypwd")+"" ;
        String oldpaypwd =  params.get("oldpaypwd")+"" ; // old pay password

        PayUserInfo payUserInfo = new PayUserInfo() ;
        payUserInfo.setUserNo(userno);
        payUserInfo.setUserType(usertype);
        PayUserInfo payUserInfoDb = iPayUserInfoService.getUserByObj(payUserInfo) ;

        if(payUserInfoDb==null) {
            result.setResult(obj);
            result.success("has no user");
            result.setCode(0);
        }else{
            payUserInfoDb.setUserName(userName); ; //real name
            payUserInfoDb.setCardType(Integer.parseInt(cardType));
            payUserInfoDb.setCardNo(cardNo);
            payUserInfoDb.setPhoneNo(phoneNo);
            payUserInfoDb.setEmail(email);
            payUserInfoDb.setSex(Integer.parseInt(sex)) ;
            payUserInfoDb.setBirthday(birthday);

            iPayUserInfoService.updateById(payUserInfoDb) ;
            result.setResult(obj);
            result.success("update pay password success!");
            result.setCode(CommonConstant.SC_OK_200);
        }

        return result ;
    }

}
