package org.suhui.modules.api.controller;

import com.alibaba.fastjson.JSON;
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

@RestController
@RequestMapping("/api/login/sendhome")
@Api(tags="送汇上门")
@Slf4j
public class AppLoginSendhomeController {

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
     *  扣取手续费接口(费率由配送模块提供)
     * @param params
     * @return
     */
    @RequestMapping(value = "/deductionfee", method = RequestMethod.POST)
    @Transactional
    public Result<JSONObject> deductionfee(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();

        String rate = params.get("rate")+"" ;
        String money = params.get("money")+"" ;
        String userno =params.get("userno")+"" ;
        String usertype =params.get("usertype")+"" ;
        String accounttypecode = params.get("accounttypecode")+"" ; // 账户类型  101 人民币账户  201 美元账户  301 菲律宾账户

        String userno_kkzh = "4028f3816d9a075a016da549eb150008" ; // 扣款账户 所有账户都放到本账户下

        try {

                long moneylong = Long.parseLong(money) ;
                Map map = new HashMap() ;
                map.put("userno" , userno) ;
                map.put("usertype" , usertype) ;
                map.put("accounttypecode" , accounttypecode) ;
                Map payaccount =  iPayAccountService.getPayAccountByUserNo(map) ;

                String account_no = payaccount.get("account_no") +"";
                Map mapAsset = new HashMap() ;
                mapAsset.put("account_no" , account_no) ;
                mapAsset.put("account_type_code" , accounttypecode) ;
                Map<String,Object> mapAssetDb = iPayAccountService.getPayAccountAssetByUserNo(mapAsset) ;
                long available_amount = Long.parseLong(mapAssetDb.get("available_amount")+"")  ; // 可用金额

                Map map_kkzh = new HashMap() ;
                map_kkzh.put("userno" , userno_kkzh) ;
                map_kkzh.put("usertype" , "0") ;
                map_kkzh.put("accounttypecode" , accounttypecode) ;
                Map payaccount_kkzh =  iPayAccountService.getPayAccountByUserNo(map_kkzh) ;

                String account_no_kkzh = payaccount_kkzh.get("account_no") +"";
                Map mapAsset_kkzh = new HashMap() ;
                mapAsset_kkzh.put("account_no" , account_no_kkzh) ;
                mapAsset_kkzh.put("account_type_code" , accounttypecode) ;
                Map<String,Object> mapAssetDb_kkzh = iPayAccountService.getPayAccountAssetByUserNo(mapAsset_kkzh) ;
                long available_amount_kkzh = Long.parseLong(mapAssetDb_kkzh.get("available_amount")+"")  ; // 可用金额

                double ratelong = Double.parseDouble(rate)  ;

                double deductmoney = ratelong*moneylong;
                if(deductmoney+moneylong > available_amount){
                    // 账号资金不够 ，无法扣款
                    result.error("insufficient funds in your account");
//                    result.setResult(JSON.parseObject("less than"));
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return result ;
                }else{
                    double availableafter = available_amount - deductmoney ;
                    int availableafterint = (int)availableafter ;
                    int frozenamountint = Integer.parseInt(mapAssetDb.get("frozen_amount")+"")  ;

                    PayAccountAsset payAccountAsset = new PayAccountAsset() ;
                    payAccountAsset.setId(Integer.parseInt(mapAssetDb.get("id")+"")) ;
                    payAccountAsset.setAvailableAmount(availableafterint) ; // 设置可用金额
                    payAccountAsset.setFrozenAmount(frozenamountint) ;
                    iPayAccountAssetService.updateById(payAccountAsset) ;

                    double availableafter_kkzh =  available_amount_kkzh+deductmoney ;
                    int availableafter_kkzh_int = (int)availableafter_kkzh ;
                    PayAccountAsset payAccountAsset_kkzh = new PayAccountAsset() ;
                    payAccountAsset_kkzh.setId(Integer.parseInt(mapAssetDb_kkzh.get("id")+"")) ;
                    payAccountAsset_kkzh.setAvailableAmount(availableafter_kkzh_int) ; // 设置可用金额
                    payAccountAsset_kkzh.setFrozenAmount(Integer.parseInt(mapAssetDb_kkzh.get("frozen_amount")+"")) ; // 设置可用金额
                    iPayAccountAssetService.updateById(payAccountAsset_kkzh) ;
                    result.setMessage("deducute money :"+ deductmoney);
                }

        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("error");
            return  result ;
        }
        result.success("dedecute success");

        return result ;
    }



    @RequestMapping(value = "/frozen", method = RequestMethod.POST)
    @Transactional
    public Result<JSONObject> frozen(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();

        String userno = params.get("userno")+"" ; //用户id
        String usertype = params.get("usertype")+"" ; //用户类型 0-默认 1-个人 2-企业',
        String accounttypecode = params.get("accounttypecode")+"" ; //账户类型
        String moneyamount = params.get("moneyamount") +""; // 获取
        String status =  params.get("status") +"";
        String biz_sendhome_no =  params.get("bizsendhomeno") + "" ; // 送汇上门编码 通过此编码 能获取到唯一数据  本地生成规则是 字母 + 3位随机码 +当前时间+3位随机码

        long moneyamount_long = Long.parseLong(moneyamount) ;
        Map map = new HashMap() ;
        map.put("userno" , userno) ;
        map.put("usertype" , usertype) ;
        map.put("accounttypecode" , accounttypecode) ;
        Map payaccount =  iPayAccountService.getPayAccountByUserNo(map) ;


        try {
                String account_no = payaccount.get("account_no") +"";
                String identity_no = payaccount.get("identity_no")+"" ;
                Map mapAsset = new HashMap() ;
                mapAsset.put("account_no" , account_no) ;
                mapAsset.put("identity_no" , identity_no) ;
                mapAsset.put("account_type_code" , accounttypecode) ;
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
                    objRemark.put("accounttypecode" ,accounttypecode) ;
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
                    cashierFreezeOrderDetail.setPayAccountType(Integer.parseInt(accounttypecode)) ;
                    cashierFreezeOrderDetail.setPayAccount(account_no) ;
                    cashierFreezeOrderDetail.setFreezeAmount(Integer.parseInt(moneyamount_long+"")) ;// 设置冻结金额
                    cashierFreezeOrderDetail.setSubStatus(Integer.parseInt(status)) ;
                    iCashierFreezeOrderDetailService.save(cashierFreezeOrderDetail) ;

                    String bill_no = UUIDGenerator.generate() ; //记账流水

                    BizAssetChangeRecord bizAssetChangeRecord = new BizAssetChangeRecord() ;//账户资金变更聚合流水表

                    bizAssetChangeRecord.setPayNo(biz_sendhome_no) ;
                    bizAssetChangeRecord.setBillNo(bill_no) ;
                    bizAssetChangeRecord.setUserNo(userno) ;
                    bizAssetChangeRecord.setUserType(Integer.parseInt(usertype)) ;
                    bizAssetChangeRecord.setAccountNo(account_no) ;
                    bizAssetChangeRecord.setAccountType( Integer.parseInt(accounttypecode)) ;
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

                    JSONObject obj1 = new JSONObject();
                    obj1.put("tradeno",trade_no) ;
                    result.setResult(obj1);
                }

            }catch (Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.error("error");
                return  result ;
            }
        result.success("frozen success");
        result.setCode(200);
        return result ;
    }


    /**
     *  送汇 上门 解冻
     * @param params
     * @return
     */
    @RequestMapping(value = "/unfrozen", method = RequestMethod.POST)
    @Transactional
    public Result<JSONObject> unfrozen(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();

        String biz_sendhome_no = params.get("bizsendhomeno") +"" ; // 系统订单号 对应biz_sendhome_no
        String trade_no = params.get("tradeno")+"" ;
        String userno = params.get("userno")+"" ; //用户id
        String usertype = params.get("usertype")+"" ; //用户类型 0-默认 1-个人 2-企业',
        String accounttypecode = params.get("accounttypecode")+"" ;
        String moneyamount = params.get("moneyamount") +""; // 获取

        String status = params.get("status") +"" ;//状态：状态：1-冻结成功 2-送汇中 100-送汇成功 99-送汇失败
        try{
                long moneyamount_long = Long.parseLong(moneyamount) ;
                Map map = new HashMap() ;
                map.put("userno" , userno) ;
                map.put("usertype" , usertype) ;
                map.put("accounttypecode" , accounttypecode) ;
                Map payaccount =  iPayAccountService.getPayAccountByUserNo(map) ;

//                Map assetRecordMapBef = new HashMap() ;
//                assetRecordMapBef.put("pay_no",biz_sendhome_no) ;
//                Map assetRecordMapdbBef =iBizAssetChangeRecordService.getAssetChangeRecordByRechargeNo(assetRecordMapBef) ;

//                String trade_no = assetRecordMapdbBef.get("bill_no")+"" ;

                if(status == null || status.equals("")){
                    result.error("请传入解冻状态");
                    result.setCode(411);
                    return result ;
                }else if(status.equals("100")){ //100-送汇成功

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
                        return result ;
                    }

                    /**解冻  解冻详情**/
                    BizFreezeOrder bizFrezzeOrder = new BizFreezeOrder() ;
                    bizFrezzeOrder.setId(Integer.parseInt(freezeid)) ;
                    bizFrezzeOrder.setStatus(2) ;
                    bizFrezzeOrder.setUnfreezeTime(new Date()) ;
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

                    String account_no = payaccount.get("account_no") +"";
                    String identity_no = payaccount.get("identity_no")+"" ;
                    Map mapAsset = new HashMap() ;
                    mapAsset.put("account_no" , account_no) ;
                    mapAsset.put("account_type_code" , accounttypecode) ;
                    mapAsset.put("identity_no" , identity_no) ;
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
                        assetRecordMap.put("pay_no",biz_sendhome_no) ;
                        Map assetRecordMapdb =iBizAssetChangeRecordService.getAssetChangeRecordByRechargeNo(assetRecordMap) ;

                        String bill_no = assetRecordMapdb.get("bill_no")+"" ;
                        BizAssetChangeRecord bizAssetChangeRecord = new BizAssetChangeRecord() ;//账户资金变更聚合流水表

                        bizAssetChangeRecord.setPayNo(biz_sendhome_no) ;
                        bizAssetChangeRecord.setBillNo(bill_no) ;
                        bizAssetChangeRecord.setUserNo(userno) ;
                        bizAssetChangeRecord.setUserType(Integer.parseInt(usertype)) ;
                        bizAssetChangeRecord.setAccountNo(account_no) ;
                        bizAssetChangeRecord.setAccountType( Integer.parseInt(accounttypecode)) ;
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
                        bizAssetChangeRecord.setPayBizType("5") ; //业务类型编码 1 充值 2 提现 3 转账 4 换汇  5 送汇 这种类似的举
                        JSONObject objRemark = new JSONObject();
                        objRemark.put("pay_no",biz_sendhome_no) ;
                        objRemark.put("user_no" ,userno) ;
                        objRemark.put("changetype" , 5) ;
                        objRemark.put("msg","送汇解冻") ;
                        bizAssetChangeRecord.setRemark("送汇解冻") ;// 备注
                        bizAssetChangeRecord.setBillJson(objRemark.toString()) ;// 记账json

                        iBizAssetChangeRecordService.save(bizAssetChangeRecord) ;

                        PayAccountAsset payAccountAsset = new PayAccountAsset() ;
                        payAccountAsset.setId(Integer.parseInt(mapAssetDb.get("id")+"")) ;
                        payAccountAsset.setFrozenAmount(frozen_amount) ; // 冻结金额
                        payAccountAsset.setAvailableAmount(available_amount) ; // 设置可用金额
                        iPayAccountAssetService.updateById(payAccountAsset) ;
                    }

                }else if(status.equals("99")){ //99-送汇失败
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
                        return result ;
                    }

                    /**解冻  解冻详情**/
                    BizFreezeOrder bizFrezzeOrder = new BizFreezeOrder() ;
                    bizFrezzeOrder.setId(Integer.parseInt(freezeid)) ;
                    bizFrezzeOrder.setStatus(2) ;
                    bizFrezzeOrder.setUnfreezeTime(new Date()) ;
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

                    String account_no = payaccount.get("account_no") +"";
                    String identity_no = payaccount.get("identity_no")+"" ;
                    Map mapAsset = new HashMap() ;
                    mapAsset.put("account_no" , account_no) ;
                    mapAsset.put("account_type_code" , accounttypecode) ;
                    mapAsset.put("identity_no" , identity_no) ;
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
                        assetRecordMap.put("pay_no",biz_sendhome_no) ;
                        Map assetRecordMapdb =iBizAssetChangeRecordService.getAssetChangeRecordByRechargeNo(assetRecordMap) ;

                        String bill_no = assetRecordMapdb.get("bill_no")+"" ;
                        BizAssetChangeRecord bizAssetChangeRecord = new BizAssetChangeRecord() ;//账户资金变更聚合流水表

                        bizAssetChangeRecord.setPayNo(biz_sendhome_no) ;
                        bizAssetChangeRecord.setBillNo(bill_no) ;
                        bizAssetChangeRecord.setUserNo(userno) ;
                        bizAssetChangeRecord.setUserType(Integer.parseInt(usertype)) ;
                        bizAssetChangeRecord.setAccountNo(account_no) ;
                        bizAssetChangeRecord.setAccountType( Integer.parseInt(accounttypecode)) ;
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
                        bizAssetChangeRecord.setPayBizType("5") ; //业务类型编码 1 充值 2 提现 3 转账 4 换汇  5 送汇 这种类似的举
                        JSONObject objRemark = new JSONObject();
                        objRemark.put("pay_no",biz_sendhome_no) ;
                        objRemark.put("user_no" ,userno) ;
                        objRemark.put("changetype" , 5) ;
                        objRemark.put("msg","送汇解冻") ;
                        bizAssetChangeRecord.setRemark("送汇解冻") ;// 备注
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
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("error");
            return  result ;
        }
        result.success("unfrozen success");
        result.setCode(200);
        return  result ;
    }

}
