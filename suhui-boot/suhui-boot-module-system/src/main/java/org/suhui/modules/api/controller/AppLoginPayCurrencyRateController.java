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
import org.suhui.modules.suhui.suhui.entity.PayCurrencyRate;
import org.suhui.modules.suhui.suhui.entity.PayCurrencyType;
import org.suhui.modules.suhui.suhui.entity.PayUserAccountType;
import org.suhui.modules.suhui.suhui.service.IPayCurrencyRateService;
import org.suhui.modules.suhui.suhui.service.IPayUserAccountTypeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;

/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/api/login/payCurrencyRate")
@Api(tags="货币种类")
@Slf4j
public class AppLoginPayCurrencyRateController {

    @Autowired
    private IPayCurrencyRateService iPayCurrencyRateService ;

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
        String rate_code =  UUIDGenerator.generate() ;  // 换汇编号，兑换以此为准
        DecimalFormat df = new DecimalFormat("#0.00") ;

        String rate_name = params.get("rate_name")+"" ; //换汇名称
        String rate_now = params.get("rate_now")+"" ; //当前汇率，用10000表示1   用分表示

        String status = params.get("status")+"" ; //是否启用(0-停用 1-启用)
        String source_currency_code = params.get("source_currency_code")+"" ; //源货币编号
        String target_currency_code = params.get("target_currency_code")+"" ; //目标货币编号
        String remark = params.get("remark")+"" ;
        String rate_nowStr = df.format(Double.parseDouble(rate_now)*10000)  ;
        int  rate_nowInt = (int)Double.parseDouble(rate_nowStr) ;

        PayCurrencyRate payCurrencyRate = new PayCurrencyRate() ; // 当前账号类型
        payCurrencyRate.setRateCode(rate_code) ;
        payCurrencyRate.setRateName(rate_name) ;
        payCurrencyRate.setRateNow(rate_nowInt ) ;
        payCurrencyRate.setStatus(Integer.parseInt(status)) ;

        payCurrencyRate.setSourceCurrencyCode(source_currency_code) ;
        payCurrencyRate.setTargetCurrencyCode(target_currency_code) ;
        payCurrencyRate.setCreateTime(new Date()) ;
        payCurrencyRate.setRateTime(new Date()) ;

        payCurrencyRate.setRemark(remark) ;

        try{
            /**  保存 支付账号信息  和身份信息 */
            iPayCurrencyRateService.save(payCurrencyRate) ;
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("操作失败");
            return result ;
        }

        result.setResult(obj);
        result.success("add success");
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
        DecimalFormat df = new DecimalFormat("#0.00") ;
        JSONObject obj = new JSONObject();
        String id = params.get("id")+"" ;
        String rate_name = params.get("rate_name")+"" ; //换汇名称
        String rate_now = params.get("rate_now")+"" ; //当前汇率，用10000表示1   用分表示
        String rate_nowStr = df.format(Double.parseDouble(rate_now)*10000)  ;
        int  rate_nowInt = (int)Double.parseDouble(rate_nowStr) ;
        String status = params.get("status")+"" ; //是否启用(0-停用 1-启用)
        String source_currency_code = params.get("source_currency_code")+"" ; //源货币编号
        String target_currency_code = params.get("target_currency_code")+"" ; //目标货币编号
        String remark = params.get("remark")+"" ;


        PayCurrencyRate payCurrencyRate = new PayCurrencyRate() ; // 当前账号类型
        payCurrencyRate.setId(Integer.parseInt(id)) ;
        payCurrencyRate.setRateName(rate_name) ;
        payCurrencyRate.setRateNow(rate_nowInt) ;
        payCurrencyRate.setStatus(Integer.parseInt(status)) ;

        payCurrencyRate.setSourceCurrencyCode(source_currency_code) ;
        payCurrencyRate.setTargetCurrencyCode(target_currency_code) ;
        payCurrencyRate.setCreateTime(new Date()) ;
        payCurrencyRate.setRateTime(new Date()) ;

        payCurrencyRate.setRemark(remark) ;

        try{
            /**  保存 支付账号信息  和身份信息 */
            iPayCurrencyRateService.updateById(payCurrencyRate) ;

        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("update success");
            return result ;
        }

        result.setResult(obj);
        result.success("修改成功");
        result.setCode(CommonConstant.SC_OK_200);
        return result ;
    }

}
