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
import org.suhui.modules.suhui.suhui.service.IPayAccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/api/login/payCharge")
@Api(tags="账户操作")
@Slf4j
public class AppLoginPayChargeController {

    @Autowired
    private IPayAccountService iPayAccountService ;
    /**
     *  账号充值
     * @param params
     * @return
     */
    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    @Transactional
    public Result<JSONObject> recharge(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        String userno = params.get("userno")+"" ; //渠道类型 1-支付宝 2-微信 3-招行 4-XX银行
        String usertype = params.get("usertype")+"" ; //渠道商户号
        Map map = new HashMap() ;
        map.put("userno" , userno) ;
        map.put("usertype" , usertype) ;
        Map payaccount = iPayAccountService.getPayAccountByUserNo(map) ;
//        是否允许充值  0 表示允许充值 1 表示不允许
        String is_allow_recharge = payaccount.get("is_allow_recharge")+"" ;
        if(is_allow_recharge.equals("0")){

        }else{

        }

        try{

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


}
