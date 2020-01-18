package org.suhui.modules.order.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.suhui.common.api.vo.Result;
import org.suhui.common.constant.CommonConstant;
import org.suhui.common.system.api.ISysBaseAPI;
import org.suhui.common.system.util.JwtUtil;
import org.suhui.common.util.PasswordUtil;
import org.suhui.common.util.RedisUtil;
import org.suhui.common.util.UUIDGenerator;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.order.entity.OrderAssurer;
import org.suhui.modules.order.service.IOrderAssurerService;
import org.suhui.modules.suhui.suhui.entity.*;
import org.suhui.modules.suhui.suhui.service.*;
import org.suhui.modules.system.entity.SysDepart;
import org.suhui.modules.system.entity.SysUser;
import org.suhui.modules.system.service.ISysDepartService;
import org.suhui.modules.system.service.ISysLogService;
import org.suhui.modules.system.service.ISysUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/assurer/user")
@Api(tags = "用户登录")
@Slf4j
public class AssurerUserController {


    @Autowired
    private IPayUserLoginService iPayUserLoginService;

    @Autowired
    private IPayUserInfoService iPayUserInfoService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Autowired
    private IPayAccountService iPayAccountService;

    @Autowired
    private IPayIdentityInfoService iPayIdentityInfoService;

    @Autowired
    private IPayAccountAssetService iPayAccountAssetService;

    @Autowired
    private IOrderAssurerService iOrderAssurerService;

    @RequestMapping(value = "/registerAsAcceptor", method = RequestMethod.POST)
    @Transactional
    public Result<JSONObject> registerAsAcceptor(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params) {

        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        String phone = params.get("phone") + "";
        String username = params.get("username") + "";
        String pwd = params.get("pwd") + "";
        String email = params.get("email") + "";
        String smsCodeParam = params.get("smscode") + ""; // 短信验证码
        String areacode = params.get("areacode") + ""; // 区域代码
        if (areacode == null || areacode.equals("") || areacode.equals("null")) {
            result.error500("please choose area code for your phone !");
            result.setCode(414);
            return result;
        }

        PayUserLogin payUserLoginCheck = iPayUserLoginService.getUserByPhoneAndUserType(phone, areacode, 3);
        if (payUserLoginCheck != null) {
            result.error500("the phone is exit !");
            result.setCode(514);
            return result;
        }


        String codeVaue = "";
        if (!phone.equals("null") && !phone.equals("")) {
            codeVaue = phone;
        } else if (!email.equals("null") && !email.equals("")) {
            codeVaue = email;
        }

        if (request.getSession(false) == null) {
            result.error500("sms code is out of time");
            result.setCode(521);
            return result;
        }
        String smsCode = request.getSession().getAttribute("smsCode_" + codeVaue) + "";

        if (smsCodeParam.equals(smsCode)) {

        } else {
            result.error500("sms code is not right");
            result.setCode(522);
            return result;
        }

        String userno = UUIDGenerator.generate();
        // 盐值
        String salt = oConvertUtils.randomGen(8);
        // 密码加密
        String passwordEncode = PasswordUtil.encrypt(phone, pwd + "", salt);
        PayUserLogin payUserLogin = new PayUserLogin();
        payUserLogin.setLoginName(phone);
        payUserLogin.setPassword(passwordEncode);
        payUserLogin.setSalt(salt);
        // 设置有效状态  状态 0-默认 1-有效 2-无效
        payUserLogin.setStatus(0);
        payUserLogin.setUserNo(userno); // 通过生成的uuid
        payUserLogin.setAreacode(areacode);
        payUserLogin.setUserType(3);
        iPayUserLoginService.save(payUserLogin);

        PayUserInfo payUserInfo = new PayUserInfo();
        payUserInfo.setUserNo(userno);
        payUserInfo.setPhoneNo(phone);
        payUserInfo.setUserType(3);
        iPayUserInfoService.save(payUserInfo);

        OrderAssurer orderAssurer = new OrderAssurer();
        orderAssurer.setUserNo(userno);
        orderAssurer.setAssurerName(phone);
        orderAssurer.setAssurerPhone(phone);
        iOrderAssurerService.save(orderAssurer);

        List list = new ArrayList();
        Map map1 = new HashMap();
        map1.put("accounttypecode", "0101");//人民币账户
        map1.put("accountname", "CNY");//人民币账户
        map1.put("currencycode", "1");
        list.add(map1);
        Map map2 = new HashMap();
        map2.put("accounttypecode", "0201");//美元账户
        map2.put("accountname", "USD");//美元账户
        map2.put("currencycode", "2");
        list.add(map2);
        Map map3 = new HashMap();
        map3.put("accounttypecode", "0301");//非礼宾账户
        map3.put("accountname", "PHP");//非礼宾账户
        map3.put("currencycode", "3");
        list.add(map3);

        Map map4 = new HashMap();
        map4.put("accounttypecode", "0401");//韩元账户
        map4.put("accountname", "KRW");//韩元币账户
        map4.put("currencycode", "4");
        list.add(map4);
        try {

            for (int i = 0; i < list.size(); i++) {
                Map mapparam = (Map) list.get(i);
                String usertype = "3"; // 承兑商
                String status = "0"; // 默认是正常状态 0
                String identityno = UUIDGenerator.generate();  // 身份编码
                String identitytype = "0"; // 身份类型 默认0
                String accountno = UUIDGenerator.generate(); //  账户编码
                String accounttypecode = mapparam.get("accounttypecode") + ""; //账户类型编码
                String accountname = mapparam.get("accountname") + "";//账户payCharge名称
                String isallowrecharge = "0"; //是否允许充值
                String isallowwithdraw = "0";//是否允许提现
                String isallowoverdraft = "1"; //是否允许透支  1 表示否
                String isallowtransferin = "0"; //是否允许转账转入
                String isallowtransferout = "0"; //是否允许转账转入
                String isfrozen = "0"; //是否冻结，表示冻结账户
                PayIdentityInfo payIdentityInfo = new PayIdentityInfo();
                payIdentityInfo.setIdentityNo(identityno);
                payIdentityInfo.setIdentityType(Integer.parseInt(identitytype));
                payIdentityInfo.setUserNo(userno);
                payIdentityInfo.setUserType(Integer.parseInt(usertype));
                payIdentityInfo.setUserName(username);
                payIdentityInfo.setStatus(Integer.parseInt(status));

                PayAccount payAccount = new PayAccount();
                payAccount.setIdentityNo(identityno);
                payAccount.setIdentityType(Integer.parseInt(identitytype));
                payAccount.setAccountName(accountname);
                payAccount.setAccountNo(accountno);

                payAccount.setIsAllowRecharge(Integer.parseInt(isallowrecharge));
                payAccount.setIsAllowWithdraw(Integer.parseInt(isallowwithdraw));
                payAccount.setIsAllowOverdraft(Integer.parseInt(isallowoverdraft));
                payAccount.setIsAllowTransferIn(Integer.parseInt(isallowtransferin));
                payAccount.setIsAllowTransferOut(Integer.parseInt(isallowtransferout));
                payAccount.setIsFrozen(Integer.parseInt(isfrozen));
                payAccount.setStatus(Integer.parseInt(status));
                payAccount.setAccountTypeCode(Integer.parseInt(accounttypecode));

                Map map = new HashMap();
                map.put("user_no", userno);
                map.put("user_type", usertype);
                Map<String, Object> identityMap = iPayIdentityInfoService.getIdentityInfoByUserNo(map);
                String identityInfo = "";
                if (identityMap == null) {
                    iPayIdentityInfoService.save(payIdentityInfo);
                    identityInfo = identityno;
                } else {
                    payAccount.setIdentityNo(identityMap.get("identity_no") + "");
                    identityInfo = identityMap.get("identity_no") + "";
                }
                /**  保存 支付账号信息  和身份信息 */
                iPayAccountService.save(payAccount);

                String availableamount = "0";
                String frozenamount = "0";
                String currencycode = mapparam.get("currencycode") + ""; //货币单位

                PayAccountAsset payAccountAsset = new PayAccountAsset();
                payAccountAsset.setAccountNo(accountno);
                payAccountAsset.setIdentityNo(identityInfo + "");
                payAccountAsset.setAccountTypeCode(Integer.parseInt(accounttypecode));
                payAccountAsset.setAvailableAmount(Integer.parseInt(availableamount));

                payAccountAsset.setFrozenAmount(Integer.parseInt(frozenamount));
                payAccountAsset.setCurrencyCode(Integer.parseInt(currencycode));

                iPayAccountAssetService.save(payAccountAsset);

            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("操作失败  Operation failed");
            return result;
        }

        result.setResult(obj);
        result.success("添加成功  Operation succeeded");
        result.setCode(CommonConstant.SC_OK_200);
        return result;
    }

    @RequestMapping(value = "/loginAsAcceptor", method = RequestMethod.POST)
    @ApiOperation("登录接口")
    public Result<JSONObject> loginAsAcceptor(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params) {
        Result<JSONObject> result = new Result<JSONObject>();
        String phone = params.get("phone") + "";
        String pwd = params.get("pwd") + "";
        String areacode = params.get("areacode") + "";
        PayUserLogin payUserLogin = iPayUserLoginService.getUserByPhoneAndUserType(phone, areacode, 3);
        if (payUserLogin == null) {
            result.error500("该承兑商用户不存在");
            result.setCode(510);
            log.error("登录失败，用户名:" + phone + "不存在！", CommonConstant.LOG_TYPE_1, null);
            return result;
        } else {
            OrderAssurer orderAssurer = iOrderAssurerService.getAssurerByUserNo(payUserLogin.getUserNo());
            if(orderAssurer == null){
                result.error500("该承兑商信息不存在");
                result.setCode(510);
                log.error("登录失败，该承兑商信息不存在", CommonConstant.LOG_TYPE_1, null);
                return result;
            }
            phone = payUserLogin.getLoginName();//  用户名 存的就是电话
            //密码验证
            String userpassword = PasswordUtil.encrypt(phone, pwd, payUserLogin.getSalt());
            String syspassword = payUserLogin.getPassword();
            if (!syspassword.equals(userpassword)) {
                result.error500("用户名或密码错误");
                result.setCode(540);
                return result;
            }
            //生成token
            String token = JwtUtil.sign(phone, syspassword);
            redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
            //设置超时时间
            redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);
            JSONObject obj = new JSONObject();
            obj.put("token", token);
            obj.put("username", payUserLogin.getLoginName());
            obj.put("realname", orderAssurer.getAssurerName());
            obj.put("avatar", "");
            obj.put("payUserLogin", payUserLogin);
            obj.put("assurerInfo", orderAssurer);
            result.setResult(obj);
            result.success("登录成功");
            result.setCode(200);
            sysBaseAPI.addLog("用户名: " + phone + ",登录成功！", CommonConstant.LOG_TYPE_1, null);
        }
        return result;
    }



}
