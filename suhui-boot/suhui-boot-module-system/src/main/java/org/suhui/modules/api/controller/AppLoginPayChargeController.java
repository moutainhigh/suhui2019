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

    @Autowired
    private IBizRechargeOrderService iBizRechargeOrderService ;

    @Autowired
    private IBizFreezeOrderService iBizFreezeOrderService ;

    @Autowired
    private IPayAccountAssetService iPayAccountAssetService ;

    @Autowired
    private ICashierFreezeOrderDetailService iCashierFreezeOrderDetailService ;

    @Autowired
    private IBizAssetChangeRecordService iBizAssetChangeRecordService ;
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
        String userno = params.get("userno")+"" ; //用户编码
        String usertype = params.get("usertype")+"" ; //用户类型
        String accounttypecode = params.get("accounttypecode") +"";
        Double chargeMoney = Double.parseDouble( params.get("chargeMoney")+"") ;
        String user_pay_account = params.get("user_pay_account")  +""; //客户付款的支付账号:支付宝账号或者微信账号或者银行卡号
        String device_type = params.get("device_type")+"" ;// 支付设备来源类型 （1-android；2-ios；3-web; 4-h5）
        String city_code = params.get("city_code")  +"";
        String remark = params.get("remark") +"";
        String channel_type = params.get("channel_type")+"" ;//支付渠道 与支付渠道账户表的channel_type一致
        String recharge_type = params.get("recharge_type")+"";  //充值类型 1-在线充值 2-线下充值(由操作员在运营后台手动充值)
        Double discount_amount = Double.parseDouble(params.get("discount_amount")) ;
        String discount_info = params.get("discount_info")+ "" ;
        String status = params.get("status")+"" ;
        String is_refund = params.get("is_refund") +"" ; //是否退款过（0-否 1-是）
        int discountAmountInt = (int) discount_amount*10000 ;
        int chargeMoneyInt = (int) chargeMoney*10000 ;
        Map map = new HashMap() ;
        map.put("userno" , userno) ;
        map.put("usertype" , usertype) ;
        map.put("accounttypecode" , accounttypecode) ;

        try{
            String trade_no = UUIDGenerator.generate() ;//业务交易流水号(各业务保持唯一)
            String biz_recharge_no =  UUIDGenerator.generate() ; //由支付系统生成的唯一流水号

            BizRechargeOrder bizRechargeOrder = new BizRechargeOrder() ;
            bizRechargeOrder.setTradeNo(trade_no) ;
            bizRechargeOrder.setBizRechargeNo(biz_recharge_no) ;
            bizRechargeOrder.setUserNo(userno) ;
            bizRechargeOrder.setUserType(usertype) ;
            bizRechargeOrder.setAccountType(Integer.parseInt(accounttypecode)) ;

            bizRechargeOrder.setRechargeType(Integer.parseInt(recharge_type)) ;
            bizRechargeOrder.setChannelType(Integer.parseInt(channel_type)) ;
            bizRechargeOrder.setAmount(chargeMoneyInt) ; // 设置充值金额
            bizRechargeOrder.setDiscountAmount(discountAmountInt) ;
            bizRechargeOrder.setDiscountInfo(discount_info) ;

            bizRechargeOrder.setStatus(Integer.parseInt(status)) ;
            bizRechargeOrder.setDeviceType(Integer.parseInt(device_type)) ;
            bizRechargeOrder.setRemark(remark) ;
            bizRechargeOrder.setUserPayAccount(user_pay_account) ;
            bizRechargeOrder.setCityCode(city_code) ;

            bizRechargeOrder.setIsRefund(Integer.parseInt(is_refund)) ;
//  充值交易表 信息录入
            iBizRechargeOrderService.save(bizRechargeOrder) ;


            Map payaccount = iPayAccountService.getPayAccountByUserNo(map) ;
    //        是否允许充值  0 表示允许充值 1 表示不允许
            String is_allow_recharge = payaccount.get("is_allow_recharge")+"" ;
            if(is_allow_recharge.equals("0")){ // 允许充值情况
                String account_no = payaccount.get("account_no") +"";
                String identity_no = payaccount.get("identity_no")+"" ;
                String identity_type = payaccount.get("identity_type")+"" ;
                Map mapAsset = new HashMap() ;
                mapAsset.put("account_no" , account_no) ;
                mapAsset.put("account_type_code" , accounttypecode) ;
                Map<String,String> mapAccetDb = iPayAccountService.getPayAccountAssetByUserNo(mapAsset) ;
                int available_amount_before = Integer.parseInt(mapAccetDb.get("available_amount") +"") ; // 可用金额
                int frozen_amount_before = Integer.parseInt(mapAccetDb.get("frozen_amount") +"") ;// 冻结金额
                int available_amount = available_amount_before;
                int frozen_amount = frozen_amount_before+ chargeMoneyInt ;

                PayAccountAsset payAccountAsset = new PayAccountAsset() ;
                payAccountAsset.setId(Integer.parseInt(mapAccetDb.get("id"))) ;
                payAccountAsset.setFrozenAmount(frozen_amount) ; // 冻结金额
                payAccountAsset.setAvailableAmount(available_amount) ; // 设置可用金额
                iPayAccountAssetService.updateById(payAccountAsset) ;

                // biz_freeze_order  冻结记录
                String biz_freeze_no = UUIDGenerator.generate() ;
                BizFreezeOrder bizFreezeOrder = new BizFreezeOrder() ;
                bizFreezeOrder.setTradeNo(trade_no) ;
                bizFreezeOrder.setTotalFreezeAmount(chargeMoneyInt) ; //  设置充值冻结金额
                bizFreezeOrder.setBizFreezeNo(biz_freeze_no)  ;
                bizFreezeOrder.setFreezeType("2") ; //冻结类型  枚举维护，1-提现冻结 2- 充值冻结
                bizFreezeOrder.setFreezeTime(new Date()) ;
                bizFreezeOrder.setStatus(1) ;//1：冻结 2：解冻
                bizFreezeOrder.setRemark("账户："account_no+ "  账户类型："+account_type_code+ "充值："+chargeMoney  ) ;
                bizFreezeOrder.setCreateTime(new Date()) ;

//                冻结记录表
                iBizFreezeOrderService.save(bizFreezeOrder) ;


//              冻结详情表
                CashierFreezeOrderDetail cashierFreezeOrderDetail = new CashierFreezeOrderDetail() ;
//                String biz_freeze_no = UUIDGenerator.generate() ; //由支付系统生成的唯一流水号
                cashierFreezeOrderDetail.setBizFreezeNo(biz_freeze_no) ;
                cashierFreezeOrderDetail. (userno) ;
                cashierFreezeOrderDetail.setUserType(usertype) ;
                 //支付账户类型（1：渠道账户    2：本金账户    3：赠额账户    4：授信账户)
                cashierFreezeOrderDetail.setPayAccountType(Integer.parseInt(accounttypecode)) ;
                cashierFreezeOrderDetail.setPayAccount(account_no) ;
                cashierFreezeOrderDetail.setFreezeAmount(chargeMoneyInt) ;// 设置冻结金额
                cashierFreezeOrderDetail.setSubStatus(Integer.parseInt(status)) ;
                iCashierFreezeOrderDetailService.save(cashierFreezeOrderDetail) ;

                String bill_no = UUIDGenerator.generate() ; //记账流水

                BizAssetChangeRecord bizAssetChangeRecord = new BizAssetChangeRecord() ;//账户资金变更聚合流水表

                bizAssetChangeRecord.setPayNo(trade_no) ;
                bizAssetChangeRecord.setBillNo(bill_no) ;
                bizAssetChangeRecord.setUserNo(userno) ;
                bizAssetChangeRecord.setUserType(Integer.parseInt(usertype)) ;
                bizAssetChangeRecord.setAccountNo(account_no) ;
                bizAssetChangeRecord.setAccountType(accounttypecode) ;
                bizAssetChangeRecord.setIdentityNo(identity_no) ;

                bizAssetChangeRecord.setChangeType(5) ; //变更类型 1-增加 2-减少 4-冻结 5-解冻
                bizAssetChangeRecord.setChangeAmount(chargeMoneyInt) ;
                bizAssetChangeRecord.setFrozenAmountBefore(frozen_amount_before) ;
                bizAssetChangeRecord.setAvailableAmountBefore(available_amount_before) ;
                bizAssetChangeRecord.setFrozenAmountAfter(frozen_amount) ;
                bizAssetChangeRecord.setAvailableAmountAfter(available_amount) ;

                bizAssetChangeRecord.setChangeTime(new Date()) ;//变更时间
                bizAssetChangeRecord.setBillTime(new Date()) ;
                bizAssetChangeRecord.setBillType(4) ;//记账类型 1-资产增加 2-转账(废弃) 3-资产减少 4-冻结 5-解冻 6-冻结资产减少
                bizAssetChangeRecord.setPayBizType("") ; //业务类型编码
                bizAssetChangeRecord.setRemark("") ;// 备注
                bizAssetChangeRecord.setBillJson() ;// 记账json

                iBizAssetChangeRecordService.save(bizAssetChangeRecord) ;


            }else{
                result.error("not allow to recharge");
                return result ;
            }

        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("failure");
            return result ;
        }

        result.setResult(obj);
        result.success("add success");
        result.setCode(CommonConstant.SC_OK_200);
        return result ;
    }


}
