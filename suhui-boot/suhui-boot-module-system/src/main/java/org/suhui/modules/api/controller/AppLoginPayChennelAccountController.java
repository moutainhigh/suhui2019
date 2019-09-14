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
import org.suhui.common.util.UUIDGenerator;
import org.suhui.modules.suhui.suhui.entity.PayChannelAccount;
import org.suhui.modules.suhui.suhui.service.IPayChannelAccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/api/login/payChannelAccount")
@Api(tags="账户信息")
@Slf4j
public class AppLoginPayChennelAccountController {



    @Autowired
    private IPayChannelAccountService iPayChannelAccountService ;
    /**
     *  账号绑定
     * @param params
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @Transactional
    public Result<JSONObject> add(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        String channeltype = params.get("channeltype")+"" ; //渠道类型 1-支付宝 2-微信 3-招行 4-XX银行
        String channelaccount = params.get("channelaccount")+"" ; //渠道商户号
        String channelloginaccount = params.get("channelloginaccount")+"" ; //支付渠道登录账号
        String channelconfig = params.get("channelconfig")+"" ; //渠道账户配置信息json
        String status = params.get("status")+"" ;//是否启用(0-停用 1-启用)
        String remark = params.get("remark")+"" ; //备注
        String channelaccountno =  UUIDGenerator.generate() ;  // 渠道账号编号
        PayChannelAccount payChannelAccount  = new PayChannelAccount() ;

        payChannelAccount.setChannelAccountNo(channelaccountno) ;
        payChannelAccount.setChannelType(Integer.parseInt(channeltype)) ;
        payChannelAccount.setChannelAccount(channelaccount) ;
        payChannelAccount.setChannelLoginAccount(channelloginaccount) ;
        payChannelAccount.setChannelConfig(channelconfig) ;
        payChannelAccount.setStatus(Integer.parseInt(status)) ;
        payChannelAccount.setRemark(remark) ;

        try{
            /**  保存 支付账号信息  和身份信息 */
            iPayChannelAccountService.save(payChannelAccount) ;
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("操作失败  Operation failed");
            return result ;
        }

        result.setResult(obj);
        result.success("添加成功 Add is successful");
        result.setCode(CommonConstant.SC_OK_200);
        return result ;
    }


    /**
     * 账号绑定 信息 修改
     * @param params
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @Transactional
    public Result<JSONObject> update(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        String channeltype = params.get("channeltype")+"" ; //渠道类型 1-支付宝 2-微信 3-招行 4-XX银行
        String channelaccount = params.get("channelaccount")+"" ; //渠道商户号
        String channelloginaccount = params.get("channelloginaccount")+"" ; //支付渠道登录账号
        String channelconfig = params.get("channelconfig")+"" ; //渠道账户配置信息json
        String status = params.get("status")+"" ;//是否启用(0-停用 1-启用)
        String remark = params.get("remark")+"" ; //备注
        String id = params.get("id")+"" ;

        PayChannelAccount payChannelAccount  = new PayChannelAccount() ;

        payChannelAccount.setChannelType(Integer.parseInt(channeltype)) ;
        payChannelAccount.setChannelAccount(channelaccount) ;
        payChannelAccount.setChannelLoginAccount(channelloginaccount) ;
        payChannelAccount.setChannelConfig(channelconfig) ;
        payChannelAccount.setStatus(Integer.parseInt(status)) ;
        payChannelAccount.setRemark(remark) ;
        payChannelAccount.setId(Integer.parseInt(id)) ;

        try{
            /**  保存 支付账号信息  和身份信息 */
            iPayChannelAccountService.updateById(payChannelAccount) ;
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("操作失败  Operation failed");
            return result ;
        }

        result.setResult(obj);
        result.success("修改成功 Update is successful");
        result.setCode(CommonConstant.SC_OK_200);
        return result ;
    }

}
