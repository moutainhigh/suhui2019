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
import org.suhui.modules.suhui.suhui.entity.BizAssetChangeRecord;
import org.suhui.modules.suhui.suhui.entity.PayAccountAsset;
import org.suhui.modules.suhui.suhui.entity.PayUserLogin;
import org.suhui.modules.suhui.suhui.service.IBizAssetChangeRecordService;
import org.suhui.modules.suhui.suhui.service.IPayAccountAssetService;
import org.suhui.modules.suhui.suhui.service.IPayAccountService;
import org.suhui.modules.suhui.suhui.service.IPayUserLoginService;

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
@RequestMapping("/api/login/payTransferAccount")
@Api(tags="账户操作")
@Slf4j
public class ApiLoginTransferAccountController {

    @Autowired
    private IPayAccountService iPayAccountService ;

    @Autowired
    private IBizAssetChangeRecordService iBizAssetChangeRecordService ;

    @Autowired
    private IPayAccountAssetService iPayAccountAssetService ;

    @Autowired
    private IPayUserLoginService iPayUserLoginService ;


    /**
     *  转账
     * @param params
     * @return
     */
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    @Transactional
    public Result<JSONObject> transfer(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {

        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
//        String userno_from = params.get("user_no_from")+"" ; //用户编码
//        String usertype_from = params.get("user_type_from")+"" ; //用户类型
        String accounttypecode_from = params.get("account_type_code_from") +"";

        String phone_from =  params.get("phone_from")+"" ; //登陆用户名 （手机号）
        String areacode_from =  params.get("areacode_from")+"" ; //区域码

        String userno_from = "" ; ////登陆用户名 （手机号）
        String usertype_from = "" ; //区域码
        PayUserLogin payUserLogin_from = iPayUserLoginService.getUserByPhone(phone_from,areacode_from) ;
        if(payUserLogin_from == null){
            result.error("from account is not exit! ");
            result.setCode(511);
            return result ;
        }
        userno_from = payUserLogin_from.getUserNo() ;
        usertype_from = payUserLogin_from.getUserType()+"" ;

//        String userno_to = params.get("user_no_to")+"" ; //用户编码
//        String usertype_to = params.get("user_type_to")+"" ; //用户类型
//        String accounttypecode_to = params.get("account_type_code_to") +"";
        String phone_to =  params.get("phone_to")+"" ; //登陆用户名 （手机号）
        String areacode_to =  params.get("areacode_to")+"" ; //区域码
        String userno_to ="" ;
        String usertype_to="" ;

        PayUserLogin payUserLogin_to = iPayUserLoginService.getUserByPhone(phone_to,areacode_to) ;
        if(payUserLogin_to == null){
            result.error("to account is not exit! ");
            result.setCode(512);
            return result ;
        }
        userno_to = payUserLogin_to.getUserNo() ;
        usertype_to = payUserLogin_to.getUserType()+"" ;


        Double chargeMoney = Double.parseDouble( params.get("charge_money")+"") ;
        int chargeMoneyInt = (int)(chargeMoney*100) ;

        Map map_from = new HashMap() ;
        map_from.put("userno" , userno_from) ;
        map_from.put("usertype" , usertype_from) ;
        map_from.put("accounttypecode" , accounttypecode_from) ;
        Map payaccount_from = iPayAccountService.getPayAccountByUserNo(map_from) ;

        Map map_to = new HashMap() ;
        map_to.put("userno" , userno_to) ;
        map_to.put("usertype" , usertype_to) ;
        map_to.put("accounttypecode" , accounttypecode_from) ;
        Map payaccount_to= iPayAccountService.getPayAccountByUserNo(map_to) ;
        String is_allow_transfer_out = payaccount_from.get("is_allow_transfer_out")+"" ;
        String is_allow_transfer_in = payaccount_to.get("is_allow_transfer_in")+"" ;

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss") ;
            Random random = new Random();
            int x = random.nextInt(899);
            x = x+100; //生成一个3位的随机数
            int y = random.nextInt(899);
            y = y+100 ;
            //由支付系统生成的唯一流水号  通过日期和水机数生成
            String biz_transfer_no =  "T"+y+ sdf.format(new Date())+'0'+x;
            obj.put("biz_transfer_no" ,biz_transfer_no) ;
            if(is_allow_transfer_in.equals("0")&&is_allow_transfer_out.equals("0")){
            // 转出账户
                String account_no_from = payaccount_from.get("account_no") +"";
                String identity_no_from = payaccount_from.get("identity_no")+"" ;
                String identity_type_from = payaccount_from.get("identity_type")+"" ;
                Map mapAsset_from = new HashMap() ;
                mapAsset_from.put("identity_no" , identity_no_from) ;
                mapAsset_from.put("account_type_code" , accounttypecode_from) ;
                Map<String,Object> mapAssetDb_from = iPayAccountService.getPayAccountAssetByUserNo(mapAsset_from) ;
                long available_amount_before_from = Long.parseLong(mapAssetDb_from.get("available_amount")+"")  ; // 可用金额
                long frozen_amount_before_from =  Long.parseLong(mapAssetDb_from.get("frozen_amount")+"") ;// 冻结金额
                long available_amount_from = available_amount_before_from - chargeMoneyInt; // 体现的话 账户金额会减少
                long frozen_amount_from = frozen_amount_before_from ;

                PayAccountAsset payAccountAsset_from = new PayAccountAsset() ;
                payAccountAsset_from.setId(Integer.parseInt(mapAssetDb_from.get("id")+"")) ;
                payAccountAsset_from.setFrozenAmount(frozen_amount_from) ; // 冻结金额
                payAccountAsset_from.setAvailableAmount(available_amount_from) ; // 设置可用金额
                iPayAccountAssetService.updateById(payAccountAsset_from) ;

                String bill_no_from = UUIDGenerator.generate() ; //记账流水
                BizAssetChangeRecord bizAssetChangeRecord_from = new BizAssetChangeRecord() ;//账户资金变更聚合流水表

                bizAssetChangeRecord_from.setPayNo(biz_transfer_no) ;
                bizAssetChangeRecord_from.setBillNo(bill_no_from) ;
                bizAssetChangeRecord_from.setUserNo(userno_from) ;
                bizAssetChangeRecord_from.setUserType(Integer.parseInt(usertype_from)) ;
                bizAssetChangeRecord_from.setAccountNo(account_no_from) ;
                bizAssetChangeRecord_from.setAccountType( Integer.parseInt(accounttypecode_from)) ;
                bizAssetChangeRecord_from.setIdentityNo(identity_no_from) ;

                bizAssetChangeRecord_from.setChangeType(2) ; //变更类型 1-增加 2-减少 4-冻结 5-解冻
                bizAssetChangeRecord_from.setChangeAmount(chargeMoneyInt) ;
                bizAssetChangeRecord_from.setFrozenAmountBefore(frozen_amount_before_from) ;
                bizAssetChangeRecord_from.setAvailableAmountBefore(available_amount_before_from) ;
                bizAssetChangeRecord_from.setFrozenAmountAfter(frozen_amount_from) ;
                bizAssetChangeRecord_from.setAvailableAmountAfter(available_amount_from) ;

                bizAssetChangeRecord_from.setChangeTime(new Date()) ;//变更时间
                bizAssetChangeRecord_from.setBillTime(new Date()) ;
                bizAssetChangeRecord_from.setBillType(3) ;//记账类型 1-资产增加 2-转账(废弃) 3-资产减少 4-冻结 5-解冻 6-冻结资产减少
                bizAssetChangeRecord_from.setPayBizType("3") ; //业务类型编码 1 充值 2 提现 3 转账这种类似的举例
                bizAssetChangeRecord_from.setRemark("") ;// 备注
                bizAssetChangeRecord_from.setBillJson("") ;// 记账json

                iBizAssetChangeRecordService.save(bizAssetChangeRecord_from) ;

                // 转入账户
                String account_no_to = payaccount_to.get("account_no") +"";
                String identity_no_to = payaccount_to.get("identity_no")+"" ;
                String identity_type_to = payaccount_to.get("identity_type")+"" ;
                Map mapAsset_to = new HashMap() ;
                mapAsset_to.put("identity_no" , identity_no_to) ;
                mapAsset_to.put("account_type_code" , accounttypecode_from) ;
                Map<String,Object> mapAssetDb_to = iPayAccountService.getPayAccountAssetByUserNo(mapAsset_to) ;
                long available_amount_before_to = Long.parseLong(mapAssetDb_to.get("available_amount")+"")  ; // 可用金额
                long frozen_amount_before_to =  Long.parseLong(mapAssetDb_to.get("frozen_amount")+"") ;// 冻结金额
                long available_amount_to = available_amount_before_to + chargeMoneyInt; // 转入账户 金额要增加
                long frozen_amount_to = frozen_amount_before_to ;

                PayAccountAsset payAccountAsset_to = new PayAccountAsset() ;
                payAccountAsset_to.setId(Integer.parseInt(mapAssetDb_to.get("id")+"")) ;
                payAccountAsset_to.setFrozenAmount(frozen_amount_to) ; // 冻结金额
                payAccountAsset_to.setAvailableAmount(available_amount_to) ; // 设置可用金额
                iPayAccountAssetService.updateById(payAccountAsset_to) ;

                String bill_no_to = UUIDGenerator.generate() ; //记账流水
                BizAssetChangeRecord bizAssetChangeRecord_to = new BizAssetChangeRecord() ;//账户资金变更聚合流水表

                bizAssetChangeRecord_to.setPayNo(biz_transfer_no) ;
                bizAssetChangeRecord_to.setBillNo(bill_no_to) ;
                bizAssetChangeRecord_to.setUserNo(userno_to) ;
                bizAssetChangeRecord_to.setUserType(Integer.parseInt(usertype_to)) ;
                bizAssetChangeRecord_to.setAccountNo(account_no_to) ;
                bizAssetChangeRecord_to.setAccountType( Integer.parseInt(accounttypecode_from)) ;
                bizAssetChangeRecord_to.setIdentityNo(identity_no_to) ;

                bizAssetChangeRecord_to.setChangeType(2) ; //变更类型 1-增加 2-减少 4-冻结 5-解冻
                bizAssetChangeRecord_to.setChangeAmount(chargeMoneyInt) ;
                bizAssetChangeRecord_to.setFrozenAmountBefore(frozen_amount_before_to) ;
                bizAssetChangeRecord_to.setAvailableAmountBefore(available_amount_before_to) ;
                bizAssetChangeRecord_to.setFrozenAmountAfter(frozen_amount_to) ;
                bizAssetChangeRecord_to.setAvailableAmountAfter(available_amount_to) ;

                bizAssetChangeRecord_to.setChangeTime(new Date()) ;//变更时间
                bizAssetChangeRecord_to.setBillTime(new Date()) ;
                bizAssetChangeRecord_to.setBillType(3) ;//记账类型 1-资产增加 2-转账(废弃) 3-资产减少 4-冻结 5-解冻 6-冻结资产减少
                bizAssetChangeRecord_to.setPayBizType("3") ; //业务类型编码 1 充值 2 提现 3 转账这种类似的举例
                bizAssetChangeRecord_to.setRemark("") ;// 备注
                bizAssetChangeRecord_to.setBillJson("") ;// 记账json

                iBizAssetChangeRecordService.save(bizAssetChangeRecord_to) ;


            }else{
                if(!is_allow_transfer_in.equals("0")){
                    result.error("not allow to transfer in ");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return result ;
                }

                if(!is_allow_transfer_out.equals("0")){
                    result.error("not allow to transfer out ");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return result ;
                }
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
