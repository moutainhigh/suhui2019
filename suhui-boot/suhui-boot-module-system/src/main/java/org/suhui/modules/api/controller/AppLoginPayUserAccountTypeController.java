package org.suhui.modules.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.suhui.common.system.query.QueryGenerator;
import org.suhui.common.util.UUIDGenerator;
import org.suhui.modules.pay.entity.UserChargeRecord;
import org.suhui.modules.suhui.suhui.entity.PayCurrencyType;
import org.suhui.modules.suhui.suhui.entity.PayUserAccountType;
import org.suhui.modules.suhui.suhui.service.IPayAccountAssetService;
import org.suhui.modules.suhui.suhui.service.IPayUserAccountTypeService;
import org.suhui.modules.suhui.suhui.service.IPayUserLoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/api/login/payUserAccountType")
@Api(tags="货币种类")
@Slf4j
public class AppLoginPayUserAccountTypeController {

    @Autowired
    private IPayUserAccountTypeService iPayUserAccountTypeService ;

    /**
     * 货币类型新增
     * @param params
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @Transactional
    public Result<JSONObject> add(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        String currencycode =  UUIDGenerator.generate() ;  // 货币编号

        String user_type = params.get("user_type")+"" ; //用户类型 0-默认 1-个人 2-企业
        String account_type_code = params.get("account_type_code")+"" ; //账户类型编码  人民币本金账户
        String account_type_name = params.get("account_type_name")+"" ; //账户类型名称

        String status = params.get("status")+"" ; //状态 0-默认 1-有效 2-无效
        String remark = params.get("remark")+"" ;

        PayUserAccountType payUserAccountType = new PayUserAccountType() ; // 当前账号类型
        payUserAccountType.setUserType(Integer.parseInt(user_type)) ;
        payUserAccountType.setAccountTypeCode(Integer.parseInt(account_type_code)) ;
        payUserAccountType.setAccountTypeName(account_type_name) ;
        payUserAccountType.setStatus(Integer.parseInt(status)) ;
        payUserAccountType.setRemark(remark) ;

        try{
            /**  保存 支付账号信息  和身份信息 */
            iPayUserAccountTypeService.save(payUserAccountType) ;
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
     * 货币类型修改
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
        String user_type = params.get("user_type")+"" ; //用户类型 0-默认 1-个人 2-企业
        String account_type_code = params.get("account_type_code")+"" ; //账户类型编码
        String account_type_name = params.get("account_type_name")+"" ; //账户类型名称

        String status = params.get("status")+"" ; //状态 0-默认 1-有效 2-无效
        String remark = params.get("remark")+"" ;
        PayUserAccountType payUserAccountType = new PayUserAccountType() ; // 当前账号类型
        payUserAccountType.setUserType(Integer.parseInt(user_type)) ;
        payUserAccountType.setAccountTypeCode(Integer.parseInt(account_type_code)) ;
        payUserAccountType.setAccountTypeName(account_type_name) ;
        payUserAccountType.setStatus(Integer.parseInt(status)) ;
        payUserAccountType.setRemark(remark) ;
        payUserAccountType.setId(Integer.parseInt(id)) ;

        try{
            /**  保存 支付账号信息  和身份信息 */
            iPayUserAccountTypeService.updateById(payUserAccountType) ;

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

    /**
     * 获取账户类型
     * @param request
     * @param response
     * @param params
     * @return
     */
    @RequestMapping(value = "/getAccountTypeList", method = RequestMethod.POST)
    public Result<JSONObject> getAccountTypeList(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        String user_type = params.get("user_type") +"" ;
        PayUserAccountType payUserAccountType = new PayUserAccountType() ;
        payUserAccountType.setUserType(Integer.parseInt(user_type)) ;
        QueryWrapper<PayUserAccountType> queryWrapper = null;
        queryWrapper = QueryGenerator.initQueryWrapper(payUserAccountType, request.getParameterMap());
        try{
            List<PayUserAccountType> list = iPayUserAccountTypeService.list(queryWrapper) ;
            obj.put("list" ,list) ;

        }catch (Exception e){
            e.printStackTrace();
            result.error("error");
            return result ;
        }

        result.setResult(obj);
        result.success("get account type list success");
        result.setCode(CommonConstant.SC_OK_200);
        return result ;
    }





}
