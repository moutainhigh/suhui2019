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
import org.suhui.modules.suhui.suhui.entity.PayIdentityChannelAccount;
import org.suhui.modules.suhui.suhui.service.IPayIdentityChannelAccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/api/login/payIdentityChannelAccount")
@Api(tags="账户信息")
@Slf4j
public class AppLoginPayIdentityChennelAccountController {



    @Autowired
    private IPayIdentityChannelAccountService iPayIdentityChannelAccountService ;
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
        String identityno = params.get("identityno")+"" ;
        String identitytype = params.get("identitytype")+"" ;
        String tradetype = params.get("tradetype")+"" ;
        String channeltype = params.get("channeltype")+"" ;
//        渠道账户名称(如用户在支付宝的真实姓名、银行卡真实姓名)
        String channelaccountname = params.get("channelaccountname")+"" ;
//        渠道账号如支付宝账户、银行卡账户
        String channelaccountno = params.get("channelaccountno")+"" ;
        String keymsg = params.get("keymsg")+"" ; //关键信息json(如信用卡安全码、日期信息)
        String status = params.get("status")+"" ;//状态 1-绑定中 99-已解绑 100-已绑定

        String remark = params.get("remark")+"" ; //备注
        //String bindtime = params.get("bindtime")+"" ;//绑定时间
       // String unbindtime = params.get("unbindtime")+"" ; //解绑时间

        PayIdentityChannelAccount payIdentityChannelAccount  = new PayIdentityChannelAccount() ;

        payIdentityChannelAccount.setIdentityNo(identityno) ;
        payIdentityChannelAccount.setIdentityType(Integer.parseInt(identitytype)) ;
        payIdentityChannelAccount.setTradeType(Integer.parseInt(tradetype)) ;
        payIdentityChannelAccount.setChannelType(Integer.parseInt(channeltype)) ;
        payIdentityChannelAccount.setChannelAccountName(channelaccountname) ;
        payIdentityChannelAccount.setChannelAccountNo(channelaccountno) ;
        payIdentityChannelAccount.setKeyMsg(keymsg) ;
        payIdentityChannelAccount.setStatus(Integer.parseInt(status) );
        payIdentityChannelAccount.setRemark(remark) ;
        if(status == null){

        }else if(status.equals("1") || status.equals("100")){
            payIdentityChannelAccount.setBindTime(new Date()) ;
        }else if(status.equals("99")){
            payIdentityChannelAccount.setUnbindTime(new Date()) ;
        }

        try{
            /**  保存 支付账号信息  和身份信息 */
            iPayIdentityChannelAccountService.save(payIdentityChannelAccount) ;
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
        String identityno = params.get("identityno")+"" ;
        String identitytype = params.get("identitytype")+"" ;
        String tradetype = params.get("tradetype")+"" ;

        String channeltype = params.get("channeltype")+"" ;
//        渠道账户名称(如用户在支付宝的真实姓名、银行卡真实姓名)
        String channelaccountname = params.get("channelaccountname")+"" ;

//        渠道账号如支付宝账户、银行卡账户
        String channelaccountno = params.get("channelaccountno")+"" ;

        String keymsg = params.get("keymsg")+"" ; //关键信息json(如信用卡安全码、日期信息)
        String status = params.get("status")+"" ;//状态 1-绑定中 99-已解绑 100-已绑定

        String remark = params.get("remark")+"" ; //备注
       // String bindtime = params.get("bindtime")+"" ;//绑定时间
       // String unbindtime = params.get("unbindtime")+"" ; //解绑时间
        String id = params.get("id")+"" ;

        PayIdentityChannelAccount payIdentityChannelAccount  = new PayIdentityChannelAccount() ;

        payIdentityChannelAccount.setIdentityNo(identityno) ;
        payIdentityChannelAccount.setIdentityType(Integer.parseInt(identitytype)) ;
        payIdentityChannelAccount.setTradeType(Integer.parseInt(tradetype)) ;
        payIdentityChannelAccount.setChannelType(Integer.parseInt(channeltype)) ;
        payIdentityChannelAccount.setChannelAccountName(channelaccountname) ;
        payIdentityChannelAccount.setChannelAccountNo(channelaccountno) ;
        payIdentityChannelAccount.setKeyMsg(keymsg) ;
        payIdentityChannelAccount.setStatus(Integer.parseInt(status) );
        payIdentityChannelAccount.setRemark(remark) ;
        payIdentityChannelAccount.setId(Integer.parseInt(id)) ;
        if(status == null){

        }else if(status.equals("1") || status.equals("100")){
            payIdentityChannelAccount.setBindTime(new Date()) ;
        }else if(status.equals("99")){
            payIdentityChannelAccount.setUnbindTime(new Date()) ;
        }

        try{
            /**  保存 支付账号信息  和身份信息 */
            iPayIdentityChannelAccountService.updateById(payIdentityChannelAccount) ;
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
