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
import org.suhui.modules.suhui.suhui.entity.PayAccountAsset;
import org.suhui.modules.suhui.suhui.service.IPayAccountAssetService;
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
@RequestMapping("/api/login/exchange")
@Api(tags="换汇")
@Slf4j
public class AppLoginPayExchangeController {

    @Autowired
    private IPayAccountService iPayAccountService ;

    @Autowired
    private IPayAccountAssetService iPayAccountAssetService ;
    /**
     *  换汇冻结
     * @param params
     * @return
     */
    @RequestMapping(value = "/frozen", method = RequestMethod.POST)
    @Transactional
    public Result<JSONObject> frozen(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();

        try{

            String userno = params.get("userno")+"" ; //用户id
            String usertype = params.get("usertype")+"" ; //用户类型 0-默认 1-个人 2-企业',
            String account_type_code = params.get("account_type_code")+"" ; //账户类型
            String moneyamount = params.get("moneyamount") +""; // 获取
            long moneyamount_long = Long.parseLong(moneyamount) ;
            Map map = new HashMap() ;
            map.put("userno" , userno) ;
            map.put("usertype" , usertype) ;
            map.put("account_type_code" , account_type_code) ;
            Map payaccount =  iPayAccountService.getPayAccountByUserNo(map) ;

            String is_allow_transfer_out = payaccount.get("is_allow_transfer_out")+"" ;
            if(is_allow_transfer_out.equals("0")){ // 是否允许转汇
                // 操作账户金额
                String account_no = payaccount.get("account_no") +"";
                String identity_no = payaccount.get("identity_no")+"" ;
                String identity_type = payaccount.get("identity_type")+"" ;
                Map mapAsset = new HashMap() ;
                mapAsset.put("account_no" , account_no) ;
                mapAsset.put("account_type_code" , account_type_code) ;
                Map<String,Object> mapAssetDb = iPayAccountService.getPayAccountAssetByUserNo(mapAsset) ;
                long available_amount_before = Long.parseLong(mapAssetDb.get("available_amount")+"")  ; // 可用金额
                if(moneyamount_long >available_amount_before ){
                    result.error("insufficient funds in your account");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return result ;
                }else{

                    // 设置 冻结金额
                    long frozen_amount_before =  Long.parseLong(mapAssetDb.get("frozen_amount")+"") ;// 冻结金额
                    long available_amount = available_amount_before;
                    long frozen_amount = frozen_amount_before+ moneyamount_long ;
                    PayAccountAsset payAccountAsset = new PayAccountAsset() ;
                    payAccountAsset.setId(Integer.parseInt(mapAssetDb.get("id")+"")) ;
                    payAccountAsset.setFrozenAmount(frozen_amount) ; // 冻结金额
                    payAccountAsset.setAvailableAmount(available_amount) ; // 设置可用金额
                    iPayAccountAssetService.updateById(payAccountAsset) ;
                }

            }else{
                result.error("not allow to transfer out");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result ;
            }

        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("操作失败");
            return result ;
        }

        result.setResult(obj);
        result.success("换汇冻结");
        result.setCode(CommonConstant.SC_OK_200);
        return result ;
    }




}
