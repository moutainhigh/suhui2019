package org.suhui.modules.api.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.suhui.common.api.vo.Result;
import org.suhui.common.constant.CommonConstant;
import org.suhui.common.util.PasswordUtil;
import org.suhui.common.util.UUIDGenerator;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.suhui.suhui.service.IPayAccountService;
import org.suhui.modules.suhui.suhui.service.IPayIdentityInfoService;
import org.suhui.modules.suhui.suhui.entity.PayIdentityInfo ;
import org.suhui.modules.suhui.suhui.entity.PayAccount ;
import org.suhui.modules.system.entity.SysUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/api/login/payAccount")
@Api(tags="账户信息")
@Slf4j
public class AppLoginPayAccountController {


    @Autowired
    private IPayAccountService iPayAccountService ;

    @Autowired
    private IPayIdentityInfoService iPayIdentityInfoService ;
    /**
     * 账户新增
     * @param params
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @Transactional
    public Result<JSONObject> add(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        String userno = params.get("userno")+"" ;
        String usertype = params.get("usertype")+"" ;
        String username = params.get("username")+"" ;

        String status = params.get("status")+"" ;
        String remark = params.get("remark")+"" ;
//        String identityno = params.get("identityno")+"" ;
        String identityno =  UUIDGenerator.generate() ;  // 身份编码
        String identitytype = params.get("identitytype")+"" ;

        //account_no  通过自动生成
        String accountno  = UUIDGenerator.generate() ; //  账户编码
        String accounttypecode = params.get("accounttypecode")+"" ; //账户类型编码
        String accountname = params.get("accountname")+"" ;//账户名称

        String isallowrecharge = params.get("isallowrecharge")+"" ; //是否允许充值
        String isallowwithdraw = params.get("isallowwithdraw")+"" ;//是否允许提现
        String isallowoverdraft = params.get("isallowoverdraft")+"" ; //是否允许透支
        String isallowtransferin = params.get("isallowtransferin")+"" ; //是否允许转账转入
        String isallowtransferout = params.get("isallowtransferout")+"" ; //是否允许转账转入

        String isfrozen = params.get("isfrozen")+"" ; //是否冻结，表示冻结账户


        // 用户身份信息表  添加账号的时候  添加用户身份信息相关数据库  主要绑定登陆账号 跟 资金账户的关系。
        PayIdentityInfo payIdentityInfo = new PayIdentityInfo() ;
        payIdentityInfo.setIdentityNo(identityno) ;
        payIdentityInfo.setIdentityType(Integer.parseInt(identitytype)) ;
        payIdentityInfo.setUserNo(userno) ;
        payIdentityInfo.setUserType(Integer.parseInt(usertype)) ;
        payIdentityInfo.setUserName(username) ;
        payIdentityInfo.setStatus(Integer.parseInt(status)) ;
        payIdentityInfo.setRemark(remark) ;

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
        payAccount.setRemark(remark) ;

        try{
            /**  保存 支付账号信息  和身份信息 */
            iPayAccountService.save(payAccount) ;
            iPayIdentityInfoService.save(payIdentityInfo) ;
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("操作失败");
            return result ;
        }

        result.setResult(obj);
        result.success("添加成功");
        result.setCode(CommonConstant.SC_OK_200);
        return result ;
    }


    /**
     * 账户新增
     * @param params
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @Transactional
    public Result<JSONObject> update(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        String id = params.get("id")+"" ;

        String status = params.get("status")+"" ;
        String remark = params.get("remark")+"" ;

        String identitytype = params.get("identitytype")+"" ;
        String accounttypecode = params.get("accounttypecode")+"" ; //账户类型编码
        String accountname = params.get("accountname")+"" ;//账户名称

        String isallowrecharge = params.get("isallowrecharge")+"" ; //是否允许充值
        String isallowwithdraw = params.get("isallowwithdraw")+"" ;//是否允许提现
        String isallowoverdraft = params.get("isallowoverdraft")+"" ; //是否允许透支
        String isallowtransferin = params.get("isallowtransferin")+"" ; //是否允许转账转入
        String isallowtransferout = params.get("isallowtransferout")+"" ; //是否允许转账转出

        String isfrozen = params.get("isfrozen")+"" ; //是否冻结，表示冻结账户



        PayAccount payAccount = new PayAccount() ;
        payAccount.setId(Integer.parseInt(id)) ;
        payAccount.setIdentityType(Integer.parseInt(identitytype)) ;
        payAccount.setAccountName(accountname) ;

        payAccount.setIsAllowRecharge(Integer.parseInt(isallowrecharge)) ;
        payAccount.setIsAllowWithdraw(Integer.parseInt(isallowwithdraw)) ;
        payAccount.setIsAllowOverdraft(Integer.parseInt(isallowoverdraft)) ;
        payAccount.setIsAllowTransferIn(Integer.parseInt(isallowtransferin)) ;
        payAccount.setIsAllowTransferOut(Integer.parseInt(isallowtransferout)) ;
        payAccount.setIsFrozen(Integer.parseInt(isfrozen)) ;
        payAccount.setStatus(Integer.parseInt(status)) ;
        payAccount.setRemark(remark) ;

        try{
            /**  保存 支付账号信息  和身份信息 */
            iPayAccountService.updateById(payAccount) ;

        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("操作失败");
            return result ;
        }

        result.setResult(obj);
        result.success("修改成功");
        result.setCode(CommonConstant.SC_OK_200);
        return result ;
    }

}
