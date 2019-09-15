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
import org.suhui.modules.suhui.suhui.entity.PayCurrencyType;
import org.suhui.modules.suhui.suhui.entity.PayUserAccountType;
import org.suhui.modules.suhui.suhui.service.IPayCurrencyTypeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/api/login/payCurrencyType")
@Api(tags="货币种类")
@Slf4j
public class AppLoginPayCurrencyTypeController {


    @Autowired
    private IPayCurrencyTypeService iPayCurrencyTypeService ;


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
//        String currencycode =  UUIDGenerator.generate() ;  // 货币编号
        String currencycode = params.get("currencycode")+"" ; //货币编号
        String currencyname = params.get("currencyname")+"" ; //货币名字
        String currencysymbol = params.get("currencysymbol")+"" ; //货币符号
        String currencyunit = params.get("currencyunit")+"" ; //货币单位

        String status = params.get("status")+"" ; //是否启用(0-停用 1-启用)
        String remark = params.get("remark")+"" ;
        PayCurrencyType payCurrencyType = new PayCurrencyType() ; // 当前账号类型
        payCurrencyType.setCurrencyCode(currencycode) ;
        payCurrencyType.setCurrencyName(currencyname) ;
        payCurrencyType.setCurrencySymbol(currencysymbol) ;
        payCurrencyType.setCurrencyUnit(currencyunit) ;
        payCurrencyType.setStatus(Integer.parseInt(status)) ;
        payCurrencyType.setRemark(remark) ;

        try{
            /**  保存 支付账号信息  和身份信息 */
            iPayCurrencyTypeService.save(payCurrencyType) ;
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

        String currencycode = params.get("currencycode")+"" ; //货币编号

        String currencyname = params.get("currencyname")+"" ; //货币名字
        String currencysymbol = params.get("currencysymbol")+"" ; //货币符号
        String currencyunit = params.get("currencyunit")+"" ; //货币单位

        String status = params.get("status")+"" ; //是否启用(0-停用 1-启用)
        String remark = params.get("remark")+"" ;
        PayCurrencyType payCurrencyType = new PayCurrencyType() ; // 当前账号类型
        payCurrencyType.setCurrencyName(currencyname) ;
        payCurrencyType.setCurrencySymbol(currencysymbol) ;
        payCurrencyType.setCurrencyUnit(currencyunit) ;
        payCurrencyType.setStatus(Integer.parseInt(status)) ;
        payCurrencyType.setRemark(remark) ;
        payCurrencyType.setId(Integer.parseInt(id)) ;
        payCurrencyType.setCurrencyCode(currencycode) ;

        try{
            /**  保存 支付账号信息  和身份信息 */
            iPayCurrencyTypeService.updateById(payCurrencyType) ;

        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("操作失败  Operation failed");
            return result ;
        }

        result.setResult(obj);
        result.success("修改成功 Modification is successful");
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
    @RequestMapping(value = "/getCurrencyTypeList", method = RequestMethod.POST)
    public Result<JSONObject> getCurrencyTypeList(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
//        PayCurrencyType payCurrencyType = new PayCurrencyType() ;
//        QueryWrapper<PayCurrencyType> queryWrapper = null;
//        queryWrapper = QueryGenerator.initQueryWrapper(payCurrencyType, request.getParameterMap());
        try{
//            List<PayCurrencyType> list = iPayCurrencyTypeService.list(queryWrapper) ;
            List<PayCurrencyType> list = iPayCurrencyTypeService.list() ;
            obj.put("list" ,list) ;

        }catch (Exception e){
            e.printStackTrace();
            result.error("error");
            return result ;
        }

        result.setResult(obj);
        result.success("get currency type list success");
        result.setCode(CommonConstant.SC_OK_200);
        return result ;
    }

}
