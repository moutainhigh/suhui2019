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
import org.suhui.modules.suhui.suhui.entity.*;
import org.suhui.modules.suhui.suhui.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    @Autowired
    private IBizFreezeOrderService iBizFreezeOrderService ;

    @Autowired
    private ICashierFreezeOrderDetailService iCashierFreezeOrderDetailService ;

    @Autowired
    private IPayCurrencyRateService iPayCurrencyRateService ;

    @Autowired
    private IBizExchangeOrderService iBizExchangeOrderService ;

    @Autowired
    private IBizAssetChangeRecordService iBizAssetChangeRecordService ;
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
            String status =  params.get("status") +"";

//            String sourcecurrency = params.get("sourcecurrency")+"" ;
//            String targetcurrency = params.get("targetcurrency")+"" ;

            String ratecodeid = params.get("ratecodeid")+"" ;

            long moneyamount_long = Long.parseLong(moneyamount) ;
            Map map = new HashMap() ;
            map.put("userno" , userno) ;
            map.put("usertype" , usertype) ;
            map.put("accounttypecode" , account_type_code) ;
            Map payaccount =  iPayAccountService.getPayAccountByUserNo(map) ;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss") ;
            Random random = new Random();
            int x = random.nextInt(899);
            x = x+100; //生成一个3位的随机数
            int y = random.nextInt(899);
            y = y+100 ;

            String is_allow_transfer_out = payaccount.get("is_allow_transfer_out")+"" ;
            if(is_allow_transfer_out.equals("0")){ // 是否允许转汇
                // 操作账户金额
                String account_no = payaccount.get("account_no") +"";
                String identity_no = payaccount.get("identity_no")+"" ;
                String identity_type = payaccount.get("identity_type")+"" ;
                Map mapAsset = new HashMap() ;
                mapAsset.put("identity_no" , identity_no) ;
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
                    long available_amount = available_amount_before-moneyamount_long;
                    long frozen_amount = frozen_amount_before+ moneyamount_long ;
                    PayAccountAsset payAccountAsset = new PayAccountAsset() ;
                    payAccountAsset.setId(Integer.parseInt(mapAssetDb.get("id")+"")) ;
                    payAccountAsset.setFrozenAmount(frozen_amount) ; // 冻结金额
                    payAccountAsset.setAvailableAmount(available_amount) ; // 设置可用金额
                    iPayAccountAssetService.updateById(payAccountAsset) ;


                    String trade_no = UUIDGenerator.generate() ;//业务交易流水号(各业务保持唯一)
                    String biz_excharge_no =  "EX"+y+ sdf.format(new Date())+'0'+x;

                    BizExchangeOrder bizExchangeOrder = new BizExchangeOrder() ;
                    bizExchangeOrder.setTradeNo(trade_no) ;
                    bizExchangeOrder.setBizExchangeNo(biz_excharge_no) ;
                    bizExchangeOrder.setUserNo(userno) ;
                    bizExchangeOrder.setUserType(Integer.parseInt(usertype)) ;
                    bizExchangeOrder.setAccountType(Integer.parseInt(account_type_code)) ;
                    bizExchangeOrder.setSourceCurrency(Integer.parseInt(moneyamount)) ;

                    PayCurrencyRate payCurrencyRate = iPayCurrencyRateService.getById(ratecodeid) ;
                    Long ratenow = payCurrencyRate.getRateNow() ;
                    long targetcurrency = moneyamount_long*ratenow/1000000000 ;

                    bizExchangeOrder.setTargetCurrency(Integer.parseInt(targetcurrency+"")) ;
                    bizExchangeOrder.setRateCode(payCurrencyRate.getRateCode()) ;
                    bizExchangeOrder.setStatus(1) ;
                    bizExchangeOrder.setExchangeTime(payCurrencyRate.getCreateTime()) ;
                    iBizExchangeOrderService.save(bizExchangeOrder) ;

                    // biz_freeze_order  冻结记录
                    String biz_freeze_no = UUIDGenerator.generate() ;
                    BizFreezeOrder bizFreezeOrder = new BizFreezeOrder() ;
                    bizFreezeOrder.setTradeNo(trade_no) ;
                    bizFreezeOrder.setTotalFreezeAmount(Integer.parseInt(moneyamount_long+"")) ; //  设置充值冻结金额
                    bizFreezeOrder.setBizFreezeNo(biz_freeze_no)  ;
                    bizFreezeOrder.setFreezeType("3") ; //冻结类型  枚举维护，1-提现冻结 2- 充值冻结 3-换汇冻结 4-送汇冻结
                    bizFreezeOrder.setFreezeTime(new Date()) ;
                    bizFreezeOrder.setStatus(1) ;//1：冻结 2：解冻
                    JSONObject objRemark = new JSONObject();
                    objRemark.put("account_no",account_no) ;
                    objRemark.put("accounttypecode" ,account_type_code) ;
                    objRemark.put("chargeMoney" , moneyamount_long) ;
                    objRemark.put("msg","充值冻结") ;
                    bizFreezeOrder.setRemark(objRemark.toString() ) ;
                    bizFreezeOrder.setCreateTime(new Date()) ;

//                冻结记录表
                    iBizFreezeOrderService.save(bizFreezeOrder) ;


//              冻结详情表
                    CashierFreezeOrderDetail cashierFreezeOrderDetail = new CashierFreezeOrderDetail() ;
//                String biz_freeze_no = UUIDGenerator.generate() ; //由支付系统生成的唯一流水号
                    cashierFreezeOrderDetail.setBizFreezeNo(biz_freeze_no) ;
                    cashierFreezeOrderDetail.setUserNo(userno) ;
                    cashierFreezeOrderDetail.setUserType(Integer.parseInt(usertype)) ;
                    //支付账户类型（1：渠道账户    2：本金账户    3：赠额账户    4：授信账户)
                    cashierFreezeOrderDetail.setPayAccountType(Integer.parseInt(account_type_code)) ;
                    cashierFreezeOrderDetail.setPayAccount(account_no) ;
                    cashierFreezeOrderDetail.setFreezeAmount(Integer.parseInt(moneyamount_long+"")) ;// 设置冻结金额
                    cashierFreezeOrderDetail.setSubStatus(Integer.parseInt(status)) ;
                    iCashierFreezeOrderDetailService.save(cashierFreezeOrderDetail) ;

                    String bill_no = UUIDGenerator.generate() ; //记账流水

                    BizAssetChangeRecord bizAssetChangeRecord = new BizAssetChangeRecord() ;//账户资金变更聚合流水表

                    bizAssetChangeRecord.setPayNo(biz_excharge_no) ;
                    bizAssetChangeRecord.setBillNo(bill_no) ;
                    bizAssetChangeRecord.setUserNo(userno) ;
                    bizAssetChangeRecord.setUserType(Integer.parseInt(usertype)) ;
                    bizAssetChangeRecord.setAccountNo(account_no) ;
                    bizAssetChangeRecord.setAccountType( Integer.parseInt(account_type_code)) ;
                    bizAssetChangeRecord.setIdentityNo(identity_no) ;

                    bizAssetChangeRecord.setChangeType(1) ; //变更类型 1-增加 2-减少 4-冻结 5-解冻
                    bizAssetChangeRecord.setChangeAmount(Integer.parseInt(moneyamount)) ;
                    bizAssetChangeRecord.setFrozenAmountBefore(frozen_amount_before) ;
                    bizAssetChangeRecord.setAvailableAmountBefore(available_amount_before) ;
                    bizAssetChangeRecord.setFrozenAmountAfter(frozen_amount) ;
                    bizAssetChangeRecord.setAvailableAmountAfter(available_amount) ;

                    bizAssetChangeRecord.setChangeTime(new Date()) ;//变更时间
                    bizAssetChangeRecord.setBillTime(new Date()) ;
                    bizAssetChangeRecord.setBillType(4) ;//记账类型 1-资产增加 2-转账(废弃) 3-资产减少 4-冻结 5-解冻 6-冻结资产减少
                    bizAssetChangeRecord.setPayBizType("1") ; //业务类型编码 1 充值 2 提现 3 转账这种类似的举例
                    bizAssetChangeRecord.setRemark("") ;// 备注
                    bizAssetChangeRecord.setBillJson("") ;// 记账json

                    iBizAssetChangeRecordService.save(bizAssetChangeRecord) ;

                }

            }else{
                result.error("not allow to transfer out");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result ;
            }

        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("操作失败 Operation failed");
            return result ;
        }

        result.setResult(obj);
        result.success("换汇冻结 frozen when changing currency ");
        result.setCode(CommonConstant.SC_OK_200);
        return result ;
    }


    /**
     *  换汇解冻
     * @param params
     * @return
     */
    @RequestMapping(value = "/unfrozen", method = RequestMethod.POST)
    @Transactional
    public Result<JSONObject> unfrozen(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();

        try{
            String order_id = params.get("order_id") +"" ; // 系统订单号 对应biz_excharge_no

            String status = params.get("status") +"" ;//状态：1-冻结成功 2-换汇中 100-换汇成功 99-换汇失败

            String userno = params.get("userno")+"" ; //用户id
            String usertype = params.get("usertype")+"" ; //用户类型 0-默认 1-个人 2-企业',
            String account_type_code_source = params.get("account_type_code_source")+"" ; //账户类型
            String account_type_code_target = params.get("account_type_code_target")+"" ; // 换汇目标账户
            String moneyamount = params.get("moneyamount") +""; // 获取
            long moneyamount_long = Long.parseLong(moneyamount) ;
            Map map = new HashMap() ;
            map.put("userno" , userno) ;
            map.put("usertype" , usertype) ;
            map.put("accounttypecode" , account_type_code_source) ;
            Map payaccount =  iPayAccountService.getPayAccountByUserNo(map) ;

            String is_allow_transfer_out = payaccount.get("is_allow_transfer_out")+"" ;
            if(is_allow_transfer_out.equals("0")){ // 是否允许转汇

                // 换汇信息
                Map mapexcharge = new HashMap() ;
                mapexcharge.put("biz_excharge_no" , order_id) ;
                // 获取换汇信息
                Map mapexchargeDb = iBizExchangeOrderService.getExchargeOrderByExchargeNo(mapexcharge) ;
                Integer targetmoney = Integer.parseInt( mapexchargeDb.get("target_currency") +"");

                if(status == null || status.equals("")){
                    result.error("请传入解冻状态");
                    result.setCode(411);
                    return result ;
                }else if(status.equals("100")){ //100-换汇成功
                    // 换汇成功 解冻
                    String id = mapexchargeDb.get("id")+"" ;
                    String trade_no = mapexchargeDb.get("trade_no")+"" ; //业务交易流水号(各业务保持唯一)

                    BizExchangeOrder bizRechargeOrder = new BizExchangeOrder() ;
                    bizRechargeOrder.setId(Integer.parseInt(id)) ;
                    bizRechargeOrder.setStatus(Integer.parseInt(status)) ;
                    iBizExchangeOrderService.updateById(bizRechargeOrder) ;

                    Map freezeMap = new HashMap() ;
                    freezeMap.put("trade_no" ,trade_no) ;
                    Map freezeMapdb = iBizFreezeOrderService.getFreezeOrderByTradeNo(freezeMap) ;

                    String freezeid = freezeMapdb.get("id")+"" ;
                    String biz_freeze_no =  freezeMapdb.get("biz_freeze_no")+"" ;
                    String statusFree =   freezeMapdb.get("status")+"" ;

                    if(statusFree == null|| statusFree.equals("")){

                    }else if(statusFree.equals("2")){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        result.error("已经解冻，不能重复解冻");
                        result.setCode(516);
                        return result ;
                    }
                    /**解冻  解冻详情**/
                    BizFreezeOrder bizFrezzeOrder = new BizFreezeOrder() ;
                    bizFrezzeOrder.setId(Integer.parseInt(freezeid)) ;
                    bizFrezzeOrder.setStatus(2) ;
                    iBizFreezeOrderService.updateById(bizFrezzeOrder) ;

                    Map cashierMap = new HashMap() ;
                    cashierMap.put("biz_freeze_no" , biz_freeze_no) ;
                    Map cashierMapdb = iCashierFreezeOrderDetailService.getCashierFreezeOrderByFreezeNo(cashierMap) ;

                    String cashierId = cashierMapdb.get("id") + "" ;

                    CashierFreezeOrderDetail cashierFreezeOrderDetail = new CashierFreezeOrderDetail() ;
                    cashierFreezeOrderDetail.setId(Integer.parseInt(cashierId)) ;
                    cashierFreezeOrderDetail.setSubStatus(2) ; //解冻
                    cashierFreezeOrderDetail.setUnfreezeTime(new Date()) ;
                    iCashierFreezeOrderDetailService.updateById(cashierFreezeOrderDetail) ;
                    /**解冻  解冻详情**/

                    // 操作账户金额
                    String account_no = payaccount.get("account_no") +"";
                    String identity_no = payaccount.get("identity_no")+"" ;
                    String identity_type = payaccount.get("identity_type")+"" ;
                    Map mapAsset = new HashMap() ;
                    mapAsset.put("account_no" , account_no) ;
                    mapAsset.put("account_type_code" , account_type_code_source) ;
                    Map<String,Object> mapAssetDb = iPayAccountService.getPayAccountAssetByUserNo(mapAsset) ;
                    long frozen_amount_before = Long.parseLong(mapAssetDb.get("frozen_amount")+"")  ; // 冻结金额

                    if(moneyamount_long >frozen_amount_before ){
                        result.error("frozen amount is not right");
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return result ;
                    }else{
                        long available_amount_before = Long.parseLong(mapAssetDb.get("available_amount")+"")  ; // 可用金额
                        // 设置 冻结金额
//                    long frozen_amount_before =  Long.parseLong(mapAssetDb.get("frozen_amount")+"") ;// 解冻金额
                        long available_amount = available_amount_before;
                        long frozen_amount = frozen_amount_before- moneyamount_long ;

                        Map assetRecordMap = new HashMap() ;
                        assetRecordMap.put("pay_no",order_id) ;
                        Map assetRecordMapdb =iBizAssetChangeRecordService.getAssetChangeRecordByRechargeNo(assetRecordMap) ;

                        String bill_no = assetRecordMapdb.get("bill_no")+"" ;
                        BizAssetChangeRecord bizAssetChangeRecord = new BizAssetChangeRecord() ;//账户资金变更聚合流水表

                        bizAssetChangeRecord.setPayNo(order_id) ;
                        bizAssetChangeRecord.setBillNo(bill_no) ;
                        bizAssetChangeRecord.setUserNo(userno) ;
                        bizAssetChangeRecord.setUserType(Integer.parseInt(usertype)) ;
                        bizAssetChangeRecord.setAccountNo(account_no) ;
                        bizAssetChangeRecord.setAccountType( Integer.parseInt(account_type_code_source)) ;
                        bizAssetChangeRecord.setIdentityNo(identity_no) ;

                        bizAssetChangeRecord.setChangeType(5) ; //变更类型 1-增加 2-减少 4-冻结 5-解冻
                        bizAssetChangeRecord.setChangeAmount(Integer.parseInt(moneyamount)) ;
                        bizAssetChangeRecord.setFrozenAmountBefore(frozen_amount_before) ;
                        bizAssetChangeRecord.setAvailableAmountBefore(available_amount_before) ;
                        bizAssetChangeRecord.setFrozenAmountAfter(frozen_amount) ;
                        bizAssetChangeRecord.setAvailableAmountAfter(available_amount) ;

                        bizAssetChangeRecord.setChangeTime(new Date()) ;//变更时间
                        bizAssetChangeRecord.setBillTime(new Date()) ;
                        bizAssetChangeRecord.setBillType(5) ;//记账类型 1-资产增加 2-转账(废弃) 3-资产减少 4-冻结 5-解冻 6-冻结资产减少
                        bizAssetChangeRecord.setPayBizType("1") ; //业务类型编码 1 充值 2 提现 3 转账这种类似的举
                        JSONObject objRemark = new JSONObject();
                        objRemark.put("pay_no",order_id) ;
                        objRemark.put("user_no" ,userno) ;
                        objRemark.put("changetype" , 5) ;
                        objRemark.put("msg","换汇解冻") ;
                        bizAssetChangeRecord.setRemark("换汇解冻") ;// 备注
                        bizAssetChangeRecord.setBillJson(objRemark.toString()) ;// 记账json

                        iBizAssetChangeRecordService.save(bizAssetChangeRecord) ;

                        PayAccountAsset payAccountAsset = new PayAccountAsset() ;
                        payAccountAsset.setId(Integer.parseInt(mapAssetDb.get("id")+"")) ;
                        payAccountAsset.setFrozenAmount(frozen_amount) ; // 冻结金额
                        payAccountAsset.setAvailableAmount(available_amount) ; // 设置可用金额
                        iPayAccountAssetService.updateById(payAccountAsset) ;

                        Map mapAssetTarget = new HashMap() ;
                        mapAssetTarget.put("account_no" , account_no) ;
                        mapAssetTarget.put("account_type_code" , account_type_code_target) ;
                        Map<String,Object> mapAssetTargetDb = iPayAccountService.getPayAccountAssetByUserNo(mapAssetTarget) ;

                        long frozen_amount_target_before = Long.parseLong(mapAssetDb.get("frozen_amount")+"")  ; // 冻结金额
                        long available_amount_target_before = Long.parseLong(mapAssetDb.get("available_amount")+"")  ;
                        long available_amount_target = available_amount_target_before + targetmoney;
                        long frozen_amount_target = frozen_amount_target_before ;

                        PayAccountAsset payAccountAssetTarget = new PayAccountAsset() ;
                        payAccountAssetTarget.setId(Integer.parseInt(mapAssetTargetDb.get("id")+"")) ;
                        payAccountAssetTarget.setFrozenAmount(frozen_amount_target) ; // 冻结金额
                        payAccountAssetTarget.setAvailableAmount(available_amount_target) ; // 设置可用金额
                        iPayAccountAssetService.updateById(payAccountAsset) ; // 设置目标冻结

                    }

                }else if(status.equals("99")){ //99-换汇失败
                    // 换汇失败 解冻
                    String id = mapexchargeDb.get("id")+"" ;
                    String trade_no = mapexchargeDb.get("trade_no")+"" ; //业务交易流水号(各业务保持唯一)

                    BizExchangeOrder bizRechargeOrder = new BizExchangeOrder() ;
                    bizRechargeOrder.setId(Integer.parseInt(id)) ;
                    bizRechargeOrder.setStatus(Integer.parseInt(status)) ;
                    iBizExchangeOrderService.updateById(bizRechargeOrder) ;

                    Map freezeMap = new HashMap() ;
                    freezeMap.put("trade_no" ,trade_no) ;
                    Map freezeMapdb = iBizFreezeOrderService.getFreezeOrderByTradeNo(freezeMap) ;

                    String freezeid = freezeMapdb.get("id")+"" ;
                    String biz_freeze_no =  freezeMapdb.get("biz_freeze_no")+"" ;
                    String statusFree =   freezeMapdb.get("status")+"" ;

                    if(statusFree == null|| statusFree.equals("")){

                    }else if(statusFree.equals("2")){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        result.error("已经解冻，不能重复解冻");
                        result.setCode(516);
                        return result ;
                    }
                    /**解冻  解冻详情**/
                    BizFreezeOrder bizFrezzeOrder = new BizFreezeOrder() ;
                    bizFrezzeOrder.setId(Integer.parseInt(freezeid)) ;
                    bizFrezzeOrder.setStatus(2) ;
                    iBizFreezeOrderService.updateById(bizFrezzeOrder) ;

                    Map cashierMap = new HashMap() ;
                    cashierMap.put("biz_freeze_no" , biz_freeze_no) ;
                    Map cashierMapdb = iCashierFreezeOrderDetailService.getCashierFreezeOrderByFreezeNo(cashierMap) ;

                    String cashierId = cashierMapdb.get("id") + "" ;

                    CashierFreezeOrderDetail cashierFreezeOrderDetail = new CashierFreezeOrderDetail() ;
                    cashierFreezeOrderDetail.setId(Integer.parseInt(cashierId)) ;
                    cashierFreezeOrderDetail.setSubStatus(2) ; //解冻
                    cashierFreezeOrderDetail.setUnfreezeTime(new Date()) ;
                    iCashierFreezeOrderDetailService.updateById(cashierFreezeOrderDetail) ;
                    /**解冻  解冻详情**/

                    // 操作账户金额
                    String account_no = payaccount.get("account_no") +"";
                    String identity_no = payaccount.get("identity_no")+"" ;
                    String identity_type = payaccount.get("identity_type")+"" ;
                    Map mapAsset = new HashMap() ;
                    mapAsset.put("account_no" , account_no) ;
                    mapAsset.put("account_type_code" , account_type_code_source) ;
                    Map<String,Object> mapAssetDb = iPayAccountService.getPayAccountAssetByUserNo(mapAsset) ;
                    long frozen_amount_before = Long.parseLong(mapAssetDb.get("frozen_amount")+"")  ; // 冻结金额

                    if(moneyamount_long >frozen_amount_before ){
                        result.error("frozen amount is not right");
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return result ;
                    }else{
                        long available_amount_before = Long.parseLong(mapAssetDb.get("available_amount")+"")  ; // 可用金额
                        // 设置 冻结金额
//                    long frozen_amount_before =  Long.parseLong(mapAssetDb.get("frozen_amount")+"") ;// 解冻金额
                        long available_amount = available_amount_before + moneyamount_long;
                        long frozen_amount = frozen_amount_before- moneyamount_long ;

                        Map assetRecordMap = new HashMap() ;
                        assetRecordMap.put("pay_no",order_id) ;
                        Map assetRecordMapdb =iBizAssetChangeRecordService.getAssetChangeRecordByRechargeNo(assetRecordMap) ;

                        String bill_no = assetRecordMapdb.get("bill_no")+"" ;
                        BizAssetChangeRecord bizAssetChangeRecord = new BizAssetChangeRecord() ;//账户资金变更聚合流水表

                        bizAssetChangeRecord.setPayNo(order_id) ;
                        bizAssetChangeRecord.setBillNo(bill_no) ;
                        bizAssetChangeRecord.setUserNo(userno) ;
                        bizAssetChangeRecord.setUserType(Integer.parseInt(usertype)) ;
                        bizAssetChangeRecord.setAccountNo(account_no) ;
                        bizAssetChangeRecord.setAccountType( Integer.parseInt(account_type_code_source)) ;
                        bizAssetChangeRecord.setIdentityNo(identity_no) ;

                        bizAssetChangeRecord.setChangeType(5) ; //变更类型 1-增加 2-减少 4-冻结 5-解冻
                        bizAssetChangeRecord.setChangeAmount(Integer.parseInt(moneyamount)) ;
                        bizAssetChangeRecord.setFrozenAmountBefore(frozen_amount_before) ;
                        bizAssetChangeRecord.setAvailableAmountBefore(available_amount_before) ;
                        bizAssetChangeRecord.setFrozenAmountAfter(frozen_amount) ;
                        bizAssetChangeRecord.setAvailableAmountAfter(available_amount) ;

                        bizAssetChangeRecord.setChangeTime(new Date()) ;//变更时间
                        bizAssetChangeRecord.setBillTime(new Date()) ;
                        bizAssetChangeRecord.setBillType(5) ;//记账类型 1-资产增加 2-转账(废弃) 3-资产减少 4-冻结 5-解冻 6-冻结资产减少
                        bizAssetChangeRecord.setPayBizType("1") ; //业务类型编码 1 充值 2 提现 3 转账这种类似的举
                        JSONObject objRemark = new JSONObject();
                        objRemark.put("pay_no",order_id) ;
                        objRemark.put("user_no" ,userno) ;
                        objRemark.put("changetype" , 5) ;
                        objRemark.put("msg","换汇解冻") ;
                        bizAssetChangeRecord.setRemark("换汇解冻") ;// 备注
                        bizAssetChangeRecord.setBillJson(objRemark.toString()) ;// 记账json

                        iBizAssetChangeRecordService.save(bizAssetChangeRecord) ;

                        PayAccountAsset payAccountAsset = new PayAccountAsset() ;
                        payAccountAsset.setId(Integer.parseInt(mapAssetDb.get("id")+"")) ;
                        payAccountAsset.setFrozenAmount(frozen_amount) ; // 冻结金额
                        payAccountAsset.setAvailableAmount(available_amount) ; // 设置可用金额
                        iPayAccountAssetService.updateById(payAccountAsset) ;


                    }


                }else{
                    result.error("请传入正确解冻状态");
                    return result ;
                }




            }else{
                result.error("not allow to transfer out"); // 不允许 转出  同样就没有解冻了。
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result ;
            }

        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("操作失败 Operation failed");
            return result ;
        }

        result.setResult(obj);
        result.success("换汇冻结 frozen when changing currency");
        result.setCode(CommonConstant.SC_OK_200);
        return result ;
    }


    /**
     *  换汇冻结
     * @param params
     * @return
     */
    @RequestMapping(value = "/frozenAndUnfrozen", method = RequestMethod.POST)
    @Transactional
    public Result<JSONObject> frozenAndUnfrozen(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();

        try{

            String userno = params.get("userno")+"" ; //用户id
            String usertype = params.get("usertype")+"" ; //用户类型 0-默认 1-个人 2-企业',
//            String account_type_code = params.get("account_type_code")+"" ; //账户类型
            String moneyamount = params.get("moneyamount") +""; // 获取
            String status =  params.get("status") +"";
            String ratecodeid = params.get("ratecodeid")+"" ;

            String order_id = "" ; // 系统订单号 对应biz_excharge_no


            String account_type_code_source = params.get("account_type_code_source")+"" ; //账户类型
            String account_type_code_target = params.get("account_type_code_target")+"" ; // 换汇目标账户


            long moneyamount_long_fr = Long.parseLong(moneyamount) ;
            Map map_fr = new HashMap() ;
            map_fr.put("userno" , userno) ;
            map_fr.put("usertype" , usertype) ;
            map_fr.put("accounttypecode" , account_type_code_source) ;
            Map payaccount_fr =  iPayAccountService.getPayAccountByUserNo(map_fr) ;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss") ;
            Random random = new Random();
            int x = random.nextInt(899);
            x = x+100; //生成一个3位的随机数
            int y = random.nextInt(899);
            y = y+100 ;

            String is_allow_transfer_out = payaccount_fr.get("is_allow_transfer_out")+"" ;
            if(is_allow_transfer_out.equals("0")){ // 是否允许转汇
                // 操作账户金额
                String account_no_fr = payaccount_fr.get("account_no") +"";
                String identity_no_fr = payaccount_fr.get("identity_no")+"" ;
                String identity_type_fr = payaccount_fr.get("identity_type")+"" ;
                Map mapAsset_fr = new HashMap() ;
                mapAsset_fr.put("identity_no" , identity_no_fr) ;
                mapAsset_fr.put("account_type_code" , account_type_code_source) ;
                Map<String,Object> mapAssetDb_fr = iPayAccountService.getPayAccountAssetByUserNo(mapAsset_fr) ;
                long available_amount_before_fr = Long.parseLong(mapAssetDb_fr.get("available_amount")+"")  ; // 可用金额
                if(moneyamount_long_fr >available_amount_before_fr ){
                    result.error("insufficient funds in your account 账户余额不足");
                    result.setCode(430);
                    return result ;
                }else{

                    // 设置 冻结金额
                    long frozen_amount_before_fr =  Long.parseLong(mapAssetDb_fr.get("frozen_amount")+"") ;// 冻结金额
                    long available_amount_fr = available_amount_before_fr-moneyamount_long_fr;

                    if(available_amount_fr < 0){
                        result.success("Insufficient balance in your account. 账户余额不足");
                        result.setCode(430);
                        return result ;
                    }

                    long frozen_amount_fr = frozen_amount_before_fr+ moneyamount_long_fr ;
                    PayAccountAsset payAccountAsset_fr = new PayAccountAsset() ;
                    payAccountAsset_fr.setId(Integer.parseInt(mapAssetDb_fr.get("id")+"")) ;
                    payAccountAsset_fr.setFrozenAmount(frozen_amount_fr) ; // 冻结金额
                    payAccountAsset_fr.setAvailableAmount(available_amount_fr) ; // 设置可用金额
                    iPayAccountAssetService.updateById(payAccountAsset_fr) ;


                    String trade_no_fr = UUIDGenerator.generate() ;//业务交易流水号(各业务保持唯一)
                    String biz_excharge_no =  "EX"+y+ sdf.format(new Date())+'0'+x;
                    order_id = biz_excharge_no ;

                    BizExchangeOrder bizExchangeOrder = new BizExchangeOrder() ;
                    bizExchangeOrder.setTradeNo(trade_no_fr) ;
                    bizExchangeOrder.setBizExchangeNo(biz_excharge_no) ;
                    bizExchangeOrder.setUserNo(userno) ;
                    bizExchangeOrder.setUserType(Integer.parseInt(usertype)) ;
                    bizExchangeOrder.setAccountType(Integer.parseInt(account_type_code_source)) ;
                    bizExchangeOrder.setSourceCurrency(Integer.parseInt(moneyamount)) ;

                    PayCurrencyRate payCurrencyRate = iPayCurrencyRateService.getById(ratecodeid) ;
                    Long ratenow = payCurrencyRate.getRateNow() ;
                    long targetcurrency = moneyamount_long_fr*ratenow/1000000000 ;

                    bizExchangeOrder.setTargetCurrency(Integer.parseInt(targetcurrency+"")) ;
                    bizExchangeOrder.setRateCode(payCurrencyRate.getRateCode()) ;
                    bizExchangeOrder.setStatus(1) ;
                    bizExchangeOrder.setExchangeTime(payCurrencyRate.getCreateTime()) ;
                    iBizExchangeOrderService.save(bizExchangeOrder) ;

                    // biz_freeze_order  冻结记录
                    String biz_freeze_no_fr = UUIDGenerator.generate() ;
                    BizFreezeOrder bizFreezeOrder = new BizFreezeOrder() ;
                    bizFreezeOrder.setTradeNo(trade_no_fr) ;
                    bizFreezeOrder.setTotalFreezeAmount(Integer.parseInt(moneyamount_long_fr+"")) ; //  设置充值冻结金额
                    bizFreezeOrder.setBizFreezeNo(biz_freeze_no_fr)  ;
                    bizFreezeOrder.setFreezeType("3") ; //冻结类型  枚举维护，1-提现冻结 2- 充值冻结 3-换汇冻结 4-送汇冻结
                    bizFreezeOrder.setFreezeTime(new Date()) ;
                    bizFreezeOrder.setStatus(1) ;//1：冻结 2：解冻
                    JSONObject objRemark_fr = new JSONObject();
                    objRemark_fr.put("account_no",account_no_fr) ;
                    objRemark_fr.put("accounttypecode" ,account_type_code_source) ;
                    objRemark_fr.put("chargeMoney" , moneyamount_long_fr) ;
                    objRemark_fr.put("msg","充值冻结") ;
                    bizFreezeOrder.setRemark(objRemark_fr.toString() ) ;
                    bizFreezeOrder.setCreateTime(new Date()) ;

//                冻结记录表
                    iBizFreezeOrderService.save(bizFreezeOrder) ;


//              冻结详情表
                    CashierFreezeOrderDetail cashierFreezeOrderDetail_fr = new CashierFreezeOrderDetail() ;
//                String biz_freeze_no_fr = UUIDGenerator.generate() ; //由支付系统生成的唯一流水号
                    cashierFreezeOrderDetail_fr.setBizFreezeNo(biz_freeze_no_fr) ;
                    cashierFreezeOrderDetail_fr.setUserNo(userno) ;
                    cashierFreezeOrderDetail_fr.setUserType(Integer.parseInt(usertype)) ;
                    //支付账户类型（1：渠道账户    2：本金账户    3：赠额账户    4：授信账户)
                    cashierFreezeOrderDetail_fr.setPayAccountType(Integer.parseInt(account_type_code_source)) ;
                    cashierFreezeOrderDetail_fr.setPayAccount(account_no_fr) ;
                    cashierFreezeOrderDetail_fr.setFreezeAmount(Integer.parseInt(moneyamount_long_fr+"")) ;// 设置冻结金额
                    cashierFreezeOrderDetail_fr.setSubStatus(Integer.parseInt(status)) ;
                    iCashierFreezeOrderDetailService.save(cashierFreezeOrderDetail_fr) ;

                    String bill_no_fr = UUIDGenerator.generate() ; //记账流水

                    BizAssetChangeRecord bizAssetChangeRecord_fr = new BizAssetChangeRecord() ;//账户资金变更聚合流水表

                    bizAssetChangeRecord_fr.setPayNo(biz_excharge_no) ;
                    bizAssetChangeRecord_fr.setBillNo(bill_no_fr) ;
                    bizAssetChangeRecord_fr.setUserNo(userno) ;
                    bizAssetChangeRecord_fr.setUserType(Integer.parseInt(usertype)) ;
                    bizAssetChangeRecord_fr.setAccountNo(account_no_fr) ;
                    bizAssetChangeRecord_fr.setAccountType( Integer.parseInt(account_type_code_source)) ;
                    bizAssetChangeRecord_fr.setIdentityNo(identity_no_fr) ;

                    bizAssetChangeRecord_fr.setChangeType(1) ; //变更类型 1-增加 2-减少 4-冻结 5-解冻
                    bizAssetChangeRecord_fr.setChangeAmount(Integer.parseInt(moneyamount)) ;
                    bizAssetChangeRecord_fr.setFrozenAmountBefore(frozen_amount_before_fr) ;
                    bizAssetChangeRecord_fr.setAvailableAmountBefore(available_amount_before_fr) ;
                    bizAssetChangeRecord_fr.setFrozenAmountAfter(frozen_amount_fr) ;
                    bizAssetChangeRecord_fr.setAvailableAmountAfter(available_amount_fr) ;

                    bizAssetChangeRecord_fr.setChangeTime(new Date()) ;//变更时间
                    bizAssetChangeRecord_fr.setBillTime(new Date()) ;
                    bizAssetChangeRecord_fr.setBillType(4) ;//记账类型 1-资产增加 2-转账(废弃) 3-资产减少 4-冻结 5-解冻 6-冻结资产减少
                    bizAssetChangeRecord_fr.setPayBizType("1") ; //业务类型编码 1 充值 2 提现 3 转账这种类似的举例
                    bizAssetChangeRecord_fr.setRemark("") ;// 备注
                    bizAssetChangeRecord_fr.setBillJson("") ;// 记账json

                    iBizAssetChangeRecordService.save(bizAssetChangeRecord_fr) ;


                    long moneyamount_long = Long.parseLong(moneyamount) ;
                    Map map = new HashMap() ;
                    map.put("userno" , userno) ;
                    map.put("usertype" , usertype) ;
                    map.put("accounttypecode" , account_type_code_source) ;
                    Map payaccount =  iPayAccountService.getPayAccountByUserNo(map) ;


                    if(is_allow_transfer_out.equals("0")){ // 是否允许转汇

                        // 换汇信息
                        Map mapexcharge = new HashMap() ;
                        mapexcharge.put("biz_exchange_no" , order_id) ;
                        // 获取换汇信息
                        Map mapexchargeDb = iBizExchangeOrderService.getExchargeOrderByExchargeNo(mapexcharge) ;
                        Integer targetmoney = Integer.parseInt( mapexchargeDb.get("target_currency") +"");

                        if(status == null || status.equals("")){
                            result.error("请传入解冻状态");
                            result.setCode(411 );
                            return result ;
                        }else if(status.equals("100")){ //100-换汇成功
                            // 换汇成功 解冻
                            String id = mapexchargeDb.get("id")+"" ;
                            String trade_no = mapexchargeDb.get("trade_no")+"" ; //业务交易流水号(各业务保持唯一)

                            BizExchangeOrder bizRechargeOrder = new BizExchangeOrder() ;
                            bizRechargeOrder.setId(Integer.parseInt(id)) ;
                            bizRechargeOrder.setStatus(Integer.parseInt(status)) ;
                            iBizExchangeOrderService.updateById(bizRechargeOrder) ;

                            Map freezeMap = new HashMap() ;
                            freezeMap.put("trade_no" ,trade_no) ;
                            Map freezeMapdb = iBizFreezeOrderService.getFreezeOrderByTradeNo(freezeMap) ;

                            String freezeid = freezeMapdb.get("id")+"" ;
                            String biz_freeze_no =  freezeMapdb.get("biz_freeze_no")+"" ;
                            String statusFree =   freezeMapdb.get("status")+"" ;

                            if(statusFree == null|| statusFree.equals("")){

                            }else if(statusFree.equals("2")){
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                result.error("已经解冻，不能重复解冻");
                                result.setCode(516);
                                return result ;
                            }
                            /**解冻  解冻详情**/
                            BizFreezeOrder bizFrezzeOrder = new BizFreezeOrder() ;
                            bizFrezzeOrder.setId(Integer.parseInt(freezeid)) ;
                            bizFrezzeOrder.setStatus(2) ;
                            iBizFreezeOrderService.updateById(bizFrezzeOrder) ;

                            Map cashierMap = new HashMap() ;
                            cashierMap.put("biz_freeze_no" , biz_freeze_no) ;
                            Map cashierMapdb = iCashierFreezeOrderDetailService.getCashierFreezeOrderByFreezeNo(cashierMap) ;

                            String cashierId = cashierMapdb.get("id") + "" ;

                            CashierFreezeOrderDetail cashierFreezeOrderDetail = new CashierFreezeOrderDetail() ;
                            cashierFreezeOrderDetail.setId(Integer.parseInt(cashierId)) ;
                            cashierFreezeOrderDetail.setSubStatus(2) ; //解冻
                            cashierFreezeOrderDetail.setUnfreezeTime(new Date()) ;
                            iCashierFreezeOrderDetailService.updateById(cashierFreezeOrderDetail) ;
                            /**解冻  解冻详情**/

                            // 操作账户金额
                            String account_no = payaccount.get("account_no") +"";
                            String identity_no = payaccount.get("identity_no")+"" ;
                            String identity_type = payaccount.get("identity_type")+"" ;
                            Map mapAsset = new HashMap() ;
                            mapAsset.put("identity_no" , identity_no) ;
                            mapAsset.put("account_type_code" , account_type_code_source) ;
                            Map<String,Object> mapAssetDb = iPayAccountService.getPayAccountAssetByUserNo(mapAsset) ;
                            long frozen_amount_before = Long.parseLong(mapAssetDb.get("frozen_amount")+"")  ; // 冻结金额

                            if(moneyamount_long >frozen_amount_before ){
                                result.error("frozen amount is not right");
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                return result ;
                            }else{
                                long available_amount_before = Long.parseLong(mapAssetDb.get("available_amount")+"")  ; // 可用金额
                                // 设置 冻结金额
//                    long frozen_amount_before =  Long.parseLong(mapAssetDb.get("frozen_amount")+"") ;// 解冻金额
                                long available_amount = available_amount_before;
                                long frozen_amount = frozen_amount_before- moneyamount_long ;

                                Map assetRecordMap = new HashMap() ;
                                assetRecordMap.put("pay_no",order_id) ;
                                Map assetRecordMapdb =iBizAssetChangeRecordService.getAssetChangeRecordByRechargeNo(assetRecordMap) ;

                                String bill_no = assetRecordMapdb.get("bill_no")+"" ;
                                BizAssetChangeRecord bizAssetChangeRecord = new BizAssetChangeRecord() ;//账户资金变更聚合流水表

                                bizAssetChangeRecord.setPayNo(order_id) ;
                                bizAssetChangeRecord.setBillNo(bill_no) ;
                                bizAssetChangeRecord.setUserNo(userno) ;
                                bizAssetChangeRecord.setUserType(Integer.parseInt(usertype)) ;
                                bizAssetChangeRecord.setAccountNo(account_no) ;
                                bizAssetChangeRecord.setAccountType( Integer.parseInt(account_type_code_source)) ;
                                bizAssetChangeRecord.setIdentityNo(identity_no) ;

                                bizAssetChangeRecord.setChangeType(5) ; //变更类型 1-增加 2-减少 4-冻结 5-解冻
                                bizAssetChangeRecord.setChangeAmount(Integer.parseInt(moneyamount)) ;
                                bizAssetChangeRecord.setFrozenAmountBefore(frozen_amount_before) ;
                                bizAssetChangeRecord.setAvailableAmountBefore(available_amount_before) ;
                                bizAssetChangeRecord.setFrozenAmountAfter(frozen_amount) ;
                                bizAssetChangeRecord.setAvailableAmountAfter(available_amount) ;

                                bizAssetChangeRecord.setChangeTime(new Date()) ;//变更时间
                                bizAssetChangeRecord.setBillTime(new Date()) ;
                                bizAssetChangeRecord.setBillType(5) ;//记账类型 1-资产增加 2-转账(废弃) 3-资产减少 4-冻结 5-解冻 6-冻结资产减少
                                bizAssetChangeRecord.setPayBizType("1") ; //业务类型编码 1 充值 2 提现 3 转账这种类似的举
                                JSONObject objRemark = new JSONObject();
                                objRemark.put("pay_no",order_id) ;
                                objRemark.put("user_no" ,userno) ;
                                objRemark.put("changetype" , 5) ;
                                objRemark.put("msg","换汇解冻") ;
                                bizAssetChangeRecord.setRemark("换汇解冻") ;// 备注
                                bizAssetChangeRecord.setBillJson(objRemark.toString()) ;// 记账json

                                iBizAssetChangeRecordService.save(bizAssetChangeRecord) ;

                                PayAccountAsset payAccountAsset = new PayAccountAsset() ;
                                payAccountAsset.setId(Integer.parseInt(mapAssetDb.get("id")+"")) ;
                                payAccountAsset.setFrozenAmount(frozen_amount) ; // 冻结金额
                                payAccountAsset.setAvailableAmount(available_amount) ; // 设置可用金额
                                iPayAccountAssetService.updateById(payAccountAsset) ;

                                Map mapAssetTarget = new HashMap() ;
                                mapAssetTarget.put("identity_no" , identity_no) ;
                                mapAssetTarget.put("account_type_code" , account_type_code_target) ;
                                Map<String,Object> mapAssetTargetDb = iPayAccountService.getPayAccountAssetByUserNo(mapAssetTarget) ;

                                long frozen_amount_target_before = Long.parseLong(mapAssetDb.get("frozen_amount")+"")  ; // 冻结金额
                                long available_amount_target_before = Long.parseLong(mapAssetDb.get("available_amount")+"")  ;
                                long available_amount_target = available_amount_target_before + targetmoney;
                                long frozen_amount_target = frozen_amount_target_before ;

                                PayAccountAsset payAccountAssetTarget = new PayAccountAsset() ;
                                payAccountAssetTarget.setId(Integer.parseInt(mapAssetTargetDb.get("id")+"")) ;
                                payAccountAssetTarget.setFrozenAmount(frozen_amount_target) ; // 冻结金额
                                payAccountAssetTarget.setAvailableAmount(available_amount_target) ; // 设置可用金额
                                iPayAccountAssetService.updateById(payAccountAsset) ; // 设置目标冻结

                            }

                        }else if(status.equals("99")){ //99-换汇失败
                            // 换汇失败 解冻
                            String id = mapexchargeDb.get("id")+"" ;
                            String trade_no = mapexchargeDb.get("trade_no")+"" ; //业务交易流水号(各业务保持唯一)

                            BizExchangeOrder bizRechargeOrder = new BizExchangeOrder() ;
                            bizRechargeOrder.setId(Integer.parseInt(id)) ;
                            bizRechargeOrder.setStatus(Integer.parseInt(status)) ;
                            iBizExchangeOrderService.updateById(bizRechargeOrder) ;

                            Map freezeMap = new HashMap() ;
                            freezeMap.put("trade_no" ,trade_no) ;
                            Map freezeMapdb = iBizFreezeOrderService.getFreezeOrderByTradeNo(freezeMap) ;

                            String freezeid = freezeMapdb.get("id")+"" ;
                            String biz_freeze_no =  freezeMapdb.get("biz_freeze_no")+"" ;
                            String statusFree =   freezeMapdb.get("status")+"" ;

                            if(statusFree == null|| statusFree.equals("")){

                            }else if(statusFree.equals("2")){
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                result.error("已经解冻，不能重复解冻");
                                result.setCode(516);
                                return result ;
                            }
                            /**解冻  解冻详情**/
                            BizFreezeOrder bizFrezzeOrder = new BizFreezeOrder() ;
                            bizFrezzeOrder.setId(Integer.parseInt(freezeid)) ;
                            bizFrezzeOrder.setStatus(2) ;
                            iBizFreezeOrderService.updateById(bizFrezzeOrder) ;

                            Map cashierMap = new HashMap() ;
                            cashierMap.put("biz_freeze_no" , biz_freeze_no) ;
                            Map cashierMapdb = iCashierFreezeOrderDetailService.getCashierFreezeOrderByFreezeNo(cashierMap) ;

                            String cashierId = cashierMapdb.get("id") + "" ;

                            CashierFreezeOrderDetail cashierFreezeOrderDetail = new CashierFreezeOrderDetail() ;
                            cashierFreezeOrderDetail.setId(Integer.parseInt(cashierId)) ;
                            cashierFreezeOrderDetail.setSubStatus(2) ; //解冻
                            cashierFreezeOrderDetail.setUnfreezeTime(new Date()) ;
                            iCashierFreezeOrderDetailService.updateById(cashierFreezeOrderDetail) ;
                            /**解冻  解冻详情**/

                            // 操作账户金额
                            String account_no = payaccount.get("account_no") +"";
                            String identity_no = payaccount.get("identity_no")+"" ;
                            String identity_type = payaccount.get("identity_type")+"" ;
                            Map mapAsset = new HashMap() ;
                            mapAsset.put("identity_no" , identity_no) ;
                            mapAsset.put("account_type_code" , account_type_code_source) ;
                            Map<String,Object> mapAssetDb = iPayAccountService.getPayAccountAssetByUserNo(mapAsset) ;
                            long frozen_amount_before = Long.parseLong(mapAssetDb.get("frozen_amount")+"")  ; // 冻结金额

                            if(moneyamount_long >frozen_amount_before ){
                                result.error("frozen amount is not right");
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                return result ;
                            }else{
                                long available_amount_before = Long.parseLong(mapAssetDb.get("available_amount")+"")  ; // 可用金额
                                // 设置 冻结金额
//                    long frozen_amount_before =  Long.parseLong(mapAssetDb.get("frozen_amount")+"") ;// 解冻金额
                                long available_amount = available_amount_before + moneyamount_long;
                                long frozen_amount = frozen_amount_before- moneyamount_long ;

                                Map assetRecordMap = new HashMap() ;
                                assetRecordMap.put("pay_no",order_id) ;
                                Map assetRecordMapdb =iBizAssetChangeRecordService.getAssetChangeRecordByRechargeNo(assetRecordMap) ;

                                String bill_no = assetRecordMapdb.get("bill_no")+"" ;
                                BizAssetChangeRecord bizAssetChangeRecord = new BizAssetChangeRecord() ;//账户资金变更聚合流水表

                                bizAssetChangeRecord.setPayNo(order_id) ;
                                bizAssetChangeRecord.setBillNo(bill_no) ;
                                bizAssetChangeRecord.setUserNo(userno) ;
                                bizAssetChangeRecord.setUserType(Integer.parseInt(usertype)) ;
                                bizAssetChangeRecord.setAccountNo(account_no) ;
                                bizAssetChangeRecord.setAccountType( Integer.parseInt(account_type_code_source)) ;
                                bizAssetChangeRecord.setIdentityNo(identity_no) ;

                                bizAssetChangeRecord.setChangeType(5) ; //变更类型 1-增加 2-减少 4-冻结 5-解冻
                                bizAssetChangeRecord.setChangeAmount(Integer.parseInt(moneyamount)) ;
                                bizAssetChangeRecord.setFrozenAmountBefore(frozen_amount_before) ;
                                bizAssetChangeRecord.setAvailableAmountBefore(available_amount_before) ;
                                bizAssetChangeRecord.setFrozenAmountAfter(frozen_amount) ;
                                bizAssetChangeRecord.setAvailableAmountAfter(available_amount) ;

                                bizAssetChangeRecord.setChangeTime(new Date()) ;//变更时间
                                bizAssetChangeRecord.setBillTime(new Date()) ;
                                bizAssetChangeRecord.setBillType(5) ;//记账类型 1-资产增加 2-转账(废弃) 3-资产减少 4-冻结 5-解冻 6-冻结资产减少
                                bizAssetChangeRecord.setPayBizType("1") ; //业务类型编码 1 充值 2 提现 3 转账这种类似的举
                                JSONObject objRemark = new JSONObject();
                                objRemark.put("pay_no",order_id) ;
                                objRemark.put("user_no" ,userno) ;
                                objRemark.put("changetype" , 5) ;
                                objRemark.put("msg","换汇解冻") ;
                                bizAssetChangeRecord.setRemark("换汇解冻") ;// 备注
                                bizAssetChangeRecord.setBillJson(objRemark.toString()) ;// 记账json

                                iBizAssetChangeRecordService.save(bizAssetChangeRecord) ;

                                PayAccountAsset payAccountAsset = new PayAccountAsset() ;
                                payAccountAsset.setId(Integer.parseInt(mapAssetDb.get("id")+"")) ;
                                payAccountAsset.setFrozenAmount(frozen_amount) ; // 冻结金额
                                payAccountAsset.setAvailableAmount(available_amount) ; // 设置可用金额
                                iPayAccountAssetService.updateById(payAccountAsset) ;


                            }


                        }else{
                            result.error("请传入正确解冻状态");
                            result.setCode(411);
                            return result ;
                        }




                    }else{
                        result.error("not allow to transfer out"); // 不允许 转出  同样就没有解冻了。
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return result ;
                    }


                }

            }else{
                result.error("not allow to transfer out");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result ;
            }

        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("操作失败 Operation failed");
            return result ;
        }

        result.setResult(obj);
        result.success(" changing currency  success !");
        result.setCode(CommonConstant.SC_OK_200);
        return result ;
    }


}
