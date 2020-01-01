package org.suhui.modules.api.controller;

import com.alibaba.fastjson.JSON;
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
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;


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
            result.setCode(517);
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
        result.setCode(200);
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
        String oldpwd = params.get("oldpwd")+"" ;

        PayUserLogin payUserLogin = iPayUserLoginService.getById(id) ;

        if(payUserLogin==null) {
            result.setResult(obj);
            result.success("has no user");
            result.setCode(517);
        }else{

            String salt = payUserLogin.getSalt() ;
            String passworddb = payUserLogin.getPassword() ;
            String oldEncode = PasswordUtil.encrypt(payUserLogin.getLoginName(), oldpwd+"", salt);

            if(!passworddb.equals(oldEncode)){
                result.error500("old  password is not right , please check it!");
                result.setCode(518);
                return result ;
            }

//            String salt = payUserLogin.getSalt() ;
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
            result.setCode(517);
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
            result.setCode(517);
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
        PayUserLogin payUserLogin = iPayUserLoginService.getById(id) ;
        if(payUserLogin == null){
            result.setResult(obj);
            result.success("has no user");
            result.setCode(517);
            return result ;
        }
        String userno = payUserLogin.getUserNo() ;
        Integer usertype = payUserLogin.getUserType() ;

        PayUserInfo payUserInfo = new PayUserInfo() ;
        payUserInfo.setUserNo(userno);
        payUserInfo.setUserType(usertype);
        PayUserInfo payUserInfoDb = iPayUserInfoService.getUserByObj(payUserInfo) ;

        if(payUserInfoDb==null) {
            result.setResult(obj);
            result.success("has no user");
            result.setCode(517);
        }else{

            String salt = payUserInfoDb.getSalt() ;
            if(salt == null || salt.equals("")){
                salt = oConvertUtils.randomGen(8);
            }

            String payPasswordEncode = PasswordUtil.encrypt(payUserInfoDb.getPhoneNo(), paypwd+"", salt);
            if(payUserInfoDb.getPayPassword() == null || payUserInfoDb.getPayPassword().equals("")){
                result.success("please to set your pay password");
                result.setCode(412);
                return result ;
            }

            if(payPasswordEncode.equals(payUserInfoDb.getPayPassword())){
                result.success("pay password is right");
                result.setCode(CommonConstant.SC_OK_200);
            }else{
                result.success("pay password is not right");
                result.setCode(413);
            }
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
            result.setCode(517);
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
            result.setCode(518);
            return result ;
        }

        if(payUserInfoDb==null) {
            result.setResult(obj);
            result.success("has no user");
            result.setCode(517);
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
     * 修改资料  修改账号 信息
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
//        String phoneNo = params.get("phoneNo")+"" ;
        String email = params.get("email")+"" ;
        String sex = params.get("sex")+"" ;
        String birthday = params.get("birthday")+"" ;

        String cardCheck = params.get("cardCheck")+"" ;
        String phoneCheck = params.get("phoneCheck")+"" ;
        String emailCheck = params.get("emailCheck")+"" ;

        String userLevel = params.get("userLevel")+"" ;

        PayUserLogin payUserLogin = iPayUserLoginService.getById(id) ;
        if(payUserLogin == null){
            result.setResult(obj);
            result.success("has no user");
            result.setCode(517);
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
            result.setCode(517);
        }else{
            payUserInfoDb.setUserName(userName); ; //real name
            payUserInfoDb.setCardType(Integer.parseInt(cardType));
            payUserInfoDb.setCardNo(cardNo);
//            payUserInfoDb.setPhoneNo(phoneNo);
            payUserInfoDb.setEmail(email);
            payUserInfoDb.setSex(Integer.parseInt(sex)) ;
            payUserInfoDb.setBirthday(birthday);
            // 设置check
            if(emailCheck == null || emailCheck.equals("")|| emailCheck.equals("null")){

            }else{
                payUserInfoDb.setEmailCheck(Integer.parseInt(emailCheck));
            }

            if(phoneCheck == null || phoneCheck.equals("")|| phoneCheck.equals("null")){

            }else{
                payUserInfoDb.setPhoneCheck(Integer.parseInt(phoneCheck));
            }

            if(cardCheck == null || cardCheck.equals("")|| cardCheck.equals("null")){

            }else{
                payUserInfoDb.setCardCheck(Integer.parseInt(cardCheck));
            }
            // 设置用户等级  会员级别 1-普通会员 2-青铜 3-白银 4-黄金 5-钻石
            if(userLevel == null || userLevel.equals("")|| userLevel.equals("null")){

            }else{
                payUserInfoDb.setUserLevel(Integer.parseInt(userLevel));
            }


            iPayUserInfoService.updateById(payUserInfoDb) ;
            result.setResult(obj);
            result.success("update userinfo success!");
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

        PayUserLogin payUserLogin = iPayUserLoginService.getById(id) ;
        if(payUserLogin == null){
            result.setResult(obj);
            result.success("has no user");
            result.setCode(517);
            return result ;
        }
        String userno = payUserLogin.getUserNo() ;
        Integer usertype = payUserLogin.getUserType() ;

        PayUserInfo payUserInfo = new PayUserInfo() ;
        payUserInfo.setUserNo(userno);
        payUserInfo.setUserType(usertype);
        PayUserInfo payUserInfoDb = iPayUserInfoService.getUserByObj(payUserInfo) ;

        if(payUserInfoDb==null) {
            result.setResult(obj);
            result.success("has no user");
            result.setCode(517);
        }else{
            Map map = new HashMap() ;
            map.put("userName" ,payUserInfoDb.getUserName()) ;
            map.put("cardType" ,payUserInfoDb.getCardType()) ;
            map.put("cardNo" ,payUserInfoDb.getCardNo()) ;
            map.put("phoneNo" ,payUserInfoDb.getPhoneNo()) ;
            map.put("email" ,payUserInfoDb.getEmail()) ;
            map.put("sex" ,payUserInfoDb.getSex()) ;
            map.put("birthday" ,payUserInfoDb.getBirthday()) ;

            obj.put("userinfo" , map) ;
            result.setResult(obj);
            result.success("get userinfo success");
            result.setCode(CommonConstant.SC_OK_200);
        }

        return result ;
    }



    /**
     * 通过电话 区域代码 查询用户详细信息
     * @param params
     * @return
     */
    @RequestMapping(value = "/getUserInfoByPhone", method = RequestMethod.POST)
    public Result<JSONObject> getUserInfoByPhone(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params ) {

        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        String phone = params.get("phone")+"" ;
        String areacode = params.get("areacode")+"" ;

        PayUserLogin payUserLogin = iPayUserLoginService.getUserByPhone(phone , areacode) ;
        if(payUserLogin == null){
            result.setResult(obj);
            result.success("has no user");
            result.setCode(517);
            return result ;
        }
        String userno = payUserLogin.getUserNo() ;
        Integer usertype = payUserLogin.getUserType() ;

        PayUserInfo payUserInfo = new PayUserInfo() ;
        payUserInfo.setUserNo(userno);
        payUserInfo.setUserType(usertype);
        PayUserInfo payUserInfoDb = iPayUserInfoService.getUserByObj(payUserInfo) ;

        if(payUserInfoDb==null) {
            result.setResult(obj);
            result.success("has no user");
            result.setCode(517);
        }else{
            Map map = new HashMap() ;
            map.put("userName" ,payUserInfoDb.getUserName()) ;
            map.put("cardType" ,payUserInfoDb.getCardType()) ;
            map.put("cardNo" ,payUserInfoDb.getCardNo()) ;
            map.put("phoneNo" ,payUserInfoDb.getPhoneNo()) ;
            map.put("email" ,payUserInfoDb.getEmail()) ;
            map.put("sex" ,payUserInfoDb.getSex()) ;
            map.put("birthday" ,payUserInfoDb.getBirthday()) ;

            obj.put("userinfo" , map) ;
            result.setResult(obj);
            result.success("get userinfo success");
            result.setCode(CommonConstant.SC_OK_200);
        }

        return result ;
    }


    /**
     * 通过电话 区域代码 查询用户详细信息
     * @param params
     * @return
     * 
     * 目前返回的是Demo数据。升级时，需要返回该用户所有的券
     * 
     */
    @RequestMapping(value = "/getUserCardListByPhone", method = RequestMethod.POST)
    public Result<JSONObject> getUserCardListByPhone(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params ) {

        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        final String phone = params.get("phone")+"" ;
        final String areacode = params.get("areacode")+"" ;

        PayUserLogin payUserLogin = iPayUserLoginService.getUserByPhone(phone , areacode) ;
        if(payUserLogin == null){
            result.setResult(obj);
            result.success("cannot find this user");
            result.setCode(517);
            return result ;
        }
        final String userno = payUserLogin.getUserNo();
        final Integer usertype = payUserLogin.getUserType();
        
        obj.put("phone", phone);
        obj.put("areacode", areacode);
        obj.put("userno", userno);
        obj.put("usertype", usertype);

        List<String> cardList = new ArrayList<>();
        JSONObject sampleCard = new JSONObject();
        sampleCard.put("cardType", "currencyTicket");
        sampleCard.put("rate", "0.005");
        sampleCard.put("usedOrNot", "0");   // 0 : not used， 1 : used
        sampleCard.put("cardShouldBeUsedBefore", java.time.LocalDateTime.now().toString());   // 

        sampleCard.put("cardNo", "5612314949667318464212");
        cardList.add(sampleCard.toString());
        sampleCard.put("cardNo", "5612314949667318464562");
        cardList.add(sampleCard.toString());
        sampleCard.put("cardNo", "5612314949667318464593");
        cardList.add(sampleCard.toString());
        obj.put("cardList", cardList);

        result.setResult(obj);
        result.success("get getUserCardListByPhone success");
        result.setCode(CommonConstant.SC_OK_200);
        return result;
    }
}
