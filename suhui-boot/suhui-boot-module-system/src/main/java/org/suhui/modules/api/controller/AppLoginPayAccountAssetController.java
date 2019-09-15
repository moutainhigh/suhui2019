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
import org.suhui.modules.suhui.suhui.entity.PayAccountAsset;
import org.suhui.modules.suhui.suhui.service.IPayAccountAssetService;
import org.suhui.modules.suhui.suhui.service.IPayIdentityInfoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/api/login/payAccountAsset")
@Api(tags="账户资产")
@Slf4j
public class AppLoginPayAccountAssetController {


    @Autowired
    private IPayAccountAssetService iPayAccountAssetService ;

    @Autowired
    private IPayIdentityInfoService iPayIdentityInfoService ;
    /**
     * 资金账户
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

        Map map = new HashMap() ;
        map.put("user_no" ,userno);
        map.put("user_type" ,usertype);
        Map<String, Object> identityMap = iPayIdentityInfoService.getIdentityInfoByUserNo(map) ;

        String identityno = identityMap.get("identity_no")+"";
        String identitytype = identityMap.get("identity_type")+"" ;

//        String identityno = params.get("identityno")+"" ;


        String accountno = params.get("accountno")+"" ;
        String accounttypecode = params.get("accounttypecode")+"" ;

        String availableamount = params.get("availableamount")+"" ;
        String frozenamount = params.get("frozenamount")+"" ;
        String currencycode = params.get("currencycode")+"" ;
        String remark = params.get("remark")+"" ;

        PayAccountAsset payAccountAsset = new PayAccountAsset() ;
        payAccountAsset.setAccountNo(accountno) ;
        payAccountAsset.setIdentityNo(identityno) ;
        payAccountAsset.setAccountTypeCode(Integer.parseInt(accounttypecode)) ;
        payAccountAsset.setAvailableAmount(Integer.parseInt(availableamount)) ;

        payAccountAsset.setFrozenAmount(Integer.parseInt(frozenamount)) ;
        payAccountAsset.setCurrencyCode(Integer.parseInt(currencycode)) ;
        payAccountAsset.setRemark(remark) ;
        try{
            // 保存 账号资产信息
            iPayAccountAssetService.save(payAccountAsset) ;
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
     * 账户资产修改
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

        String accounttypecode = params.get("accounttypecode")+"" ;

        String availableamount = params.get("availableamount")+"" ;
        String frozenamount = params.get("frozenamount")+"" ;
        String currencycode = params.get("currencycode")+"" ;
        String remark = params.get("remark")+"" ;

        PayAccountAsset payAccountAsset = new PayAccountAsset() ;
        payAccountAsset.setId(Integer.parseInt(id)) ;

        payAccountAsset.setAccountTypeCode(Integer.parseInt(accounttypecode)) ;
        payAccountAsset.setAvailableAmount(Integer.parseInt(availableamount)) ;

        payAccountAsset.setFrozenAmount(Integer.parseInt(frozenamount)) ;
        payAccountAsset.setCurrencyCode(Integer.parseInt(currencycode)) ;
        payAccountAsset.setRemark(remark) ;
        try{
            // 保存 账号资产信息
            iPayAccountAssetService.updateById(payAccountAsset) ;
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("操作失败 Operation failed");
            return result ;
        }

        result.setResult(obj);
        result.success("修改成功 Update succeeded");
        result.setCode(CommonConstant.SC_OK_200);
        return result ;
    }




}
