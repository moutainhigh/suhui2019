package org.suhui.modules.api.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.suhui.common.api.vo.Result;
import org.suhui.common.constant.CommonConstant;
import org.suhui.common.util.UUIDGenerator;
import org.suhui.modules.api.utils.MD5Util;
import org.suhui.modules.suhui.suhui.entity.*;
import org.suhui.modules.suhui.suhui.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
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

    @Value("${goldsunpay.paysurl}")
    private String payurl ;

    @Value("${goldsunpay.payskey}")
    private String keys ;

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
        String userno = params.get("user_no")+"" ; //用户编码
        String usertype = params.get("user_type")+"" ; //用户类型
        String accounttypecode = params.get("account_type_code") +"";
        Double chargeMoney = Double.parseDouble( params.get("charge_money")+"") ;
//        String user_pay_account = params.get("user_pay_account")  +""; //客户付款的支付账号:支付宝账号或者微信账号或者银行卡号
        String user_pay_account = "" ;
        String device_type = params.get("device_type")+"" ;// 支付设备来源类型 （1-android；2-ios；3-web; 4-h5）
//        String city_code = params.get("city_code")  +"";  // 国家城市编码 现在没有用到。
        String remark = params.get("remark") +"";
        String channel_type = params.get("channel_type")+"" ;//支付渠道 与支付渠道账户表的channel_type一致  渠道类型 1-支付宝 2-微信 3-招行 4-XX银行
        String recharge_type = params.get("recharge_type")+"";  //充值类型 1-在线充值 2-线下充值(由操作员在运营后台手动充值)
        Double discount_amount = Double.parseDouble(params.get("discount_amount")+"") ;
        DecimalFormat df = new DecimalFormat("#0.00");

        discount_amount = Double.parseDouble(df.format(discount_amount))  ;
        chargeMoney = Double.parseDouble(df.format(chargeMoney))  ;

        String discount_info = params.get("discount_info")+ "" ;
        String status = params.get("status")+"" ;
        String is_refund = params.get("is_refund") +"" ; //是否退款过（0-否 1-是）
        int discountAmountInt = (int)(discount_amount*100) ;
        int chargeMoneyInt = (int)(chargeMoney*100) ;
        Map map = new HashMap() ;
        map.put("userno" , userno) ;
        map.put("usertype" , usertype) ;
        map.put("accounttypecode" , accounttypecode) ;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss") ;
        Random random = new Random();
        int x = random.nextInt(899);
        x = x+100; //生成一个3位的随机数
        int y = random.nextInt(899);
        y = y+100 ;
        try{

            Map mapChannel = new HashMap() ;
            mapChannel.put("userno" , userno) ;
            mapChannel.put("usertype" , usertype) ;
            mapChannel.put("channel_type" ,channel_type) ; // 获取支付的账号 通过用户 用户类型 和支付通道类型
            Map payidentitychannel = iPayAccountService.getPayIdentityChannelAccountByUserNo(mapChannel) ;
            if(payidentitychannel == null){
                result.success("does not has account channnel");
                result.setCode(513);
                return result ;
            }else{
                user_pay_account = payidentitychannel.get("channel_account_no")+"" ; // 银行账号  支付宝账户  微信账号等
            }

            String trade_no = UUIDGenerator.generate() ;//业务交易流水号(各业务保持唯一)
//            String biz_recharge_no =  UUIDGenerator.generate()
            //由支付系统生成的唯一流水号  通过日期和水机数生成
            String biz_recharge_no =  "R"+y+ sdf.format(new Date())+'0'+x;

            BizRechargeOrder bizRechargeOrder = new BizRechargeOrder() ;
            bizRechargeOrder.setTradeNo(trade_no) ;
            bizRechargeOrder.setBizRechargeNo(biz_recharge_no) ;
            bizRechargeOrder.setUserNo(userno) ;
            bizRechargeOrder.setUserType(Integer.parseInt(usertype) ) ;
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
//            bizRechargeOrder.setCityCode(city_code) ;

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
                Map<String,Object> mapAssetDb = iPayAccountService.getPayAccountAssetByUserNo(mapAsset) ;
                long available_amount_before = Long.parseLong(mapAssetDb.get("available_amount")+"")  ; // 可用金额
                long frozen_amount_before =  Long.parseLong(mapAssetDb.get("frozen_amount")+"") ;// 冻结金额
                long available_amount = available_amount_before;
                long frozen_amount = frozen_amount_before+ chargeMoneyInt ;

                PayAccountAsset payAccountAsset = new PayAccountAsset() ;
                payAccountAsset.setId(Integer.parseInt(mapAssetDb.get("id")+"")) ;
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
                JSONObject objRemark = new JSONObject();
                objRemark.put("account_no",account_no) ;
                objRemark.put("accounttypecode" ,accounttypecode) ;
                objRemark.put("chargeMoney" , chargeMoney) ;
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
                cashierFreezeOrderDetail.setFreezeAmount(chargeMoneyInt) ;// 设置冻结金额
                cashierFreezeOrderDetail.setSubStatus(Integer.parseInt(status)) ;
                iCashierFreezeOrderDetailService.save(cashierFreezeOrderDetail) ;

                String bill_no = UUIDGenerator.generate() ; //记账流水

                BizAssetChangeRecord bizAssetChangeRecord = new BizAssetChangeRecord() ;//账户资金变更聚合流水表

                bizAssetChangeRecord.setPayNo(biz_recharge_no) ;
                bizAssetChangeRecord.setBillNo(bill_no) ;
                bizAssetChangeRecord.setUserNo(userno) ;
                bizAssetChangeRecord.setUserType(Integer.parseInt(usertype)) ;
                bizAssetChangeRecord.setAccountNo(account_no) ;
                bizAssetChangeRecord.setAccountType( Integer.parseInt(accounttypecode)) ;
                bizAssetChangeRecord.setIdentityNo(identity_no) ;

                bizAssetChangeRecord.setChangeType(1) ; //变更类型 1-增加 2-减少 4-冻结 5-解冻
                bizAssetChangeRecord.setChangeAmount(chargeMoneyInt) ;
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

                String urlParam = payurl + "?app_id=52&format=web&goods_name=recharge&notify_url=http://3.93.15.101:3333/api/login/payCharge/rechargeCallback"
                        +"&order_id=" + biz_recharge_no +"&order_uid=" + userno + "&pay_type=alipay&price="+chargeMoney+"&version=v1.0&signature=" ;

                String urlKey = "app_id=52&format=web&goods_name=recharge&notify_url=http://3.93.15.101:3333/api/login/payCharge/rechargeCallback"
                        +"&order_id=" + biz_recharge_no +"&order_uid=" + userno  + "&pay_type=alipay&price="+chargeMoney+"&version=v1.0&"+keys ;

                String signature = MD5Util.encryption(urlKey).toUpperCase() ;
                urlParam = urlParam + signature;

                obj.put("url" , urlParam) ;

            }else{
                result.error("not allow to recharge");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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


    /**
     *  账号充值 回调
     * @param params
     * @return
     */
    @RequestMapping(value = "/rechargeCallback", method = RequestMethod.POST)
    @Transactional
    public Result<JSONObject> rechargeCallback(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        try{

        String version = params.get("version") +"" ;
        String platform_order_id = params.get("platform_order_id") +"" ;  //⽀付平台分配的订单号，便于对账 第三方账号
        String order_id = params.get("order_id") +"" ; // 系统订单号 对应biz_recharge_no
        String order_uid = params.get("order_uid") +"" ;
        String pay_type = params.get("pay_type") +"" ;
        String price = params.get("price") +"" ; // 支付金额
        String pay_price = params.get("pay_price") +"" ; // 实际支付金额
        String signature = params.get("signature") +"" ;

        Map mapRecharge = new HashMap() ;
        mapRecharge.put("biz_recharge_no" , order_id) ;
        // 修改 订单信息
        Map mapRechargeDb = iBizRechargeOrderService.getRechargeOrderByRechargeNo(mapRecharge) ;
        String id = mapRechargeDb.get("id")+"" ;
        String trade_no = mapRechargeDb.get("trade_no")+"" ; //业务交易流水号(各业务保持唯一)

        BizRechargeOrder bizRechargeOrder = new BizRechargeOrder() ;
        bizRechargeOrder.setId(Integer.parseInt(id)) ;
        bizRechargeOrder.setStatus(100) ;
        bizRechargeOrder.setThirdTransNo(platform_order_id) ;
        iBizRechargeOrderService.updateById(bizRechargeOrder) ;
        // 修改 订单信息
//        BizRechargeOrder bizRechargeOrder = iBizRechargeOrderService.getRechargeOrderObjectByRechargeNo(mapRecharge) ;
//        bizRechargeOrder.setThirdTransNo(platform_order_id) ;//
//        bizRechargeOrder.setStatus(100) ;
//        iBizRechargeOrderService.updateById(bizRechargeOrder) ;

        String user_no = mapRechargeDb.get("user_no") +"";
        String user_type = mapRechargeDb.get("user_type")+"" ;
        String accounttypecode = mapRechargeDb.get("account_type")+"" ;
        Map map = new HashMap() ;
        map.put("userno" , user_no) ;
        map.put("usertype" , user_type) ;
        map.put("accounttypecode" , accounttypecode) ;

        Map payaccount = iPayAccountService.getPayAccountByUserNo(map) ;

        String account_no = payaccount.get("account_no") +"";
        String identity_no = payaccount.get("identity_no")+"" ;
        String identity_type = payaccount.get("identity_type")+"" ;
        Map mapAsset = new HashMap() ;
        mapAsset.put("account_no" , account_no) ;
        mapAsset.put("account_type_code" , accounttypecode) ;
        Map<String,Object> mapAssetDb = iPayAccountService.getPayAccountAssetByUserNo(mapAsset) ;
        DecimalFormat df  =new DecimalFormat("#0.00") ;
        double priceDou = Double.parseDouble(price) ;//  字符转 double
        priceDou = Double.parseDouble(df.format(priceDou))  ; // double 取两位小数
        int chargeMoneyInt = (int)(priceDou*100) ; // 小数转 整数

        long available_amount_before = Long.parseLong(mapAssetDb.get("available_amount")+"")  ; // 可用金额
        long frozen_amount_before =  Long.parseLong(mapAssetDb.get("frozen_amount")+"") ;// 冻结金额
        long available_amount = available_amount_before + chargeMoneyInt ;
        long frozen_amount = frozen_amount_before - chargeMoneyInt ;



        Map freezeMap = new HashMap() ;
        freezeMap.put("trade_no" ,trade_no) ;
        Map freezeMapdb = iBizFreezeOrderService.getFreezeOrderByTradeNo(freezeMap) ;
        String freezeid = freezeMapdb.get("id")+"" ;
        String biz_freeze_no =  freezeMapdb.get("biz_freeze_no")+"" ;
        String status =   freezeMapdb.get("status")+"" ;

        if(status == null|| status.equals("")){

        }else if(status.equals("2")){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("已经解冻，不能重复解冻");
            return result ;
        }

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

        Map assetRecordMap = new HashMap() ;
        assetRecordMap.put("pay_no",order_id) ;
        Map assetRecordMapdb =iBizAssetChangeRecordService.getAssetChangeRecordByRechargeNo(assetRecordMap) ;

        String bill_no = assetRecordMapdb.get("bill_no")+"" ;
        BizAssetChangeRecord bizAssetChangeRecord = new BizAssetChangeRecord() ;//账户资金变更聚合流水表

        bizAssetChangeRecord.setPayNo(order_id) ;
        bizAssetChangeRecord.setBillNo(bill_no) ;
        bizAssetChangeRecord.setUserNo(user_no) ;
        bizAssetChangeRecord.setUserType(Integer.parseInt(user_type)) ;
        bizAssetChangeRecord.setAccountNo(account_no) ;
        bizAssetChangeRecord.setAccountType( Integer.parseInt(accounttypecode)) ;
        bizAssetChangeRecord.setIdentityNo(identity_no) ;

        bizAssetChangeRecord.setChangeType(5) ; //变更类型 1-增加 2-减少 4-冻结 5-解冻
        bizAssetChangeRecord.setChangeAmount(chargeMoneyInt) ;
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
            objRemark.put("user_no" ,user_no) ;
            objRemark.put("changetype" , 5) ;
            objRemark.put("msg","充值解冻") ;
        bizAssetChangeRecord.setRemark("充值解冻") ;// 备注
        bizAssetChangeRecord.setBillJson(objRemark.toString()) ;// 记账json

        iBizAssetChangeRecordService.save(bizAssetChangeRecord) ;

        PayAccountAsset payAccountAsset = new PayAccountAsset() ;
        payAccountAsset.setId(Integer.parseInt(mapAssetDb.get("id")+"")) ;
        payAccountAsset.setFrozenAmount(frozen_amount) ; // 冻结金额
        payAccountAsset.setAvailableAmount(available_amount) ; // 设置可用金额
        iPayAccountAssetService.updateById(payAccountAsset) ;


        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("failure");
            return result ;
        }
        System.out.println(params.toString()+">>>>>>>>>>>>>>>>"+keys);

        result.setResult(obj);
        result.success("add success");
        result.setCode(CommonConstant.SC_OK_200);
        return result ;
    }

}
