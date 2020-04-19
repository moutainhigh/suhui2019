package org.suhui.modules.toB.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.suhui.common.api.vo.Result;
import org.suhui.common.constant.CommonConstant;
import org.suhui.common.util.PasswordUtil;
import org.suhui.common.util.UUIDGenerator;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.toB.entity.OrderMerchant;
import org.suhui.modules.toB.entity.OrderMerchantMoneyChange;
import org.suhui.modules.toB.entity.PayUserInfo;
import org.suhui.modules.toB.entity.PayUserLogin;
import org.suhui.modules.toB.mapper.OrderMerchantMapper;
import org.suhui.modules.toB.service.IOrderMerchantMoneyChangeService;
import org.suhui.modules.toB.service.IOrderMerchantService;
import org.suhui.modules.toB.service.IPayUserInfoService;
import org.suhui.modules.toB.service.IPayUserLoginService;
import org.suhui.modules.utils.BaseUtil;

/**
 * 类说明：商户相关
 *
 * @author: 蔡珊珊
 * @create:  2020-04-07 23:10
 **/
@Service
public class OrderMerchantServiceImpl extends ServiceImpl<OrderMerchantMapper, OrderMerchant> implements IOrderMerchantService {

    @Autowired
    private IPayUserLoginService iPayUserLoginService;

    @Autowired
    private IPayUserInfoService iPayUserInfoService;

    @Autowired
    private IOrderMerchantMoneyChangeService iOrderMerchantMoneyChangeService;

    @Autowired
    private OrderMerchantMapper orderMerchantMapper;

    /**
     * 添加商户
     *
     * @param
     * @return
     */
    @Override
    public OrderMerchant addMerchant(JSONObject data) {
        System.out.println(data);
        String phone = data.getString("phone");
        String pwd = data.getString("password");
        String areacode = data.getString("areacode");
        String countryCode = data.getString("countryCode");
        String merchantName = data.getString("merchantName");
        String merchantRate = data.getString("merchantRate");
        String totalLimit = data.getString("totalLimit");
        String ensureProportion = data.getString("ensureProportion");


        String userno = UUIDGenerator.generate();
        // 盐值
        String salt = oConvertUtils.randomGen(8);
        // 密码加密
        String passwordEncode = PasswordUtil.encrypt(phone, pwd + "", salt);
        PayUserLogin payUserLogin = new PayUserLogin();
        payUserLogin.setLoginName(phone);
        payUserLogin.setPassword(passwordEncode);
        payUserLogin.setSalt(salt);
        // 设置有效状态  状态 0-默认 1-有效 2-无效
        payUserLogin.setStatus(0);
        payUserLogin.setUserNo(userno); // 通过生成的uuid
        payUserLogin.setAreacode(areacode);
        payUserLogin.setUserType(CommonConstant.usertype_toB_merchant);
        iPayUserLoginService.save(payUserLogin);
        PayUserInfo payUserInfo = new PayUserInfo();
        payUserInfo.setUserNo(userno);
        payUserInfo.setPhoneNo(phone);
        payUserInfo.setUserType(CommonConstant.usertype_toB_merchant);
        iPayUserInfoService.save(payUserInfo);
        OrderMerchant orderMerchant = new OrderMerchant();
        orderMerchant.setUserNo(userno);
        orderMerchant.setMerchantName(merchantName);
        orderMerchant.setCountryCode(countryCode);
        orderMerchant.setMerchantPhone(phone);
        orderMerchant.setMerchantRate(data.getDouble(merchantRate));
        orderMerchant.setTotalLimit(data.getDouble("totalLimit") * 100);
        orderMerchant.setCanUseLimit(data.getDouble("totalLimit") * 100);
        orderMerchant.setEnsureProportion(data.getDouble("ensureProportion"));
        this.save(orderMerchant);
        return orderMerchant;
    }

    /**
     * 批量审核
     *
     * @param
     * @return
     */
    @Override
    public Result<Object> auditPassMerchant(String ids) {
        String[] idArr = ids.split(",");
        Result<Object> result = new Result<>();
        boolean check = true;
        for (String orderId : idArr) {
            OrderMerchant orderMerchant = getById(orderId);
            if (!BaseUtil.Base_HasValue(orderMerchant)) {
                result = Result.error(513, "商户不存在");
                check = false;
                break;
            }
            if (!orderMerchant.getMerchantState().equals("to_audit")) {
                result = Result.error(514, "【待审核】状态的商户可执行该操作");
                check = false;
                break;
            }
            if (orderMerchant.getTotalLimit() <= 0) {
                result = Result.error(514, "商户总限额必须大于0");
                check = false;
                break;
            }
            if(!BaseUtil.Base_HasValue(orderMerchant.getCardType())){
                result = Result.error(515, "商户缺少实名认证信息");
                check = false;
                break;
            }
            if(!BaseUtil.Base_HasValue(orderMerchant.getCardNo())){
                result = Result.error(515, "商户缺少实名认证信息");
                check = false;
                break;
            }
            if(!BaseUtil.Base_HasValue(orderMerchant.getCardFrontPicture())){
                result = Result.error(515, "商户缺少实名认证信息");
                check = false;
                break;
            }
            if(!BaseUtil.Base_HasValue(orderMerchant.getCardBackPicture())){
                result = Result.error(515, "商户缺少实名认证信息");
                check = false;
                break;
            }
            orderMerchant.setMerchantState("normal");
            updateById(orderMerchant);
        }
        if (!check) {
            return result;
        }
        return Result.ok("操作成功");
    }

    /**
     * 更改 保证金、租赁金
     *
     * @param
     * @return
     */
    @Override
    public Result<Object> changeMerchantMoney(JSONObject jsonObject) {
        String merchantId = jsonObject.getString("merchantId");
        String classChange = jsonObject.getString("classChange");
        String typeChange = jsonObject.getString("typeChange");
        Double changeMoney = jsonObject.getDouble("changeMoney");
        String changeText = jsonObject.getString("changeText");
        OrderMerchant orderMerchant = getById(merchantId);
        if (!BaseUtil.Base_HasValue(orderMerchant)) {
            return Result.error("该承兑商不存在");
        }
        // 保证金
        if ("ensure".equals(classChange)) {
            if ("sub".equals(typeChange)) {
                if (orderMerchant.getEnsureMoney() < changeMoney * 100) {
                    return Result.error("保证金额度不足");
                }
            }
        } else if ("lease".equals(classChange)) { // 租赁金
            if ("sub".equals(typeChange)) {
                if (orderMerchant.getLeaseMoney() < changeMoney * 100) {
                    return Result.error("租赁金额度不足");
                }
            }
        }
        OrderMerchantMoneyChange orderMerchantMoneyChange = new OrderMerchantMoneyChange();
        orderMerchantMoneyChange.setMerchantId(merchantId);
        orderMerchantMoneyChange.setMerchantName(orderMerchant.getMerchantName());
        orderMerchantMoneyChange.setMerchantPhone(orderMerchant.getMerchantPhone());
        orderMerchantMoneyChange.setChangeClass(classChange);
        orderMerchantMoneyChange.setChangeType(typeChange);
        orderMerchantMoneyChange.setChangeMoney(changeMoney);
        orderMerchantMoneyChange.setChangeText(changeText);
        iOrderMerchantMoneyChangeService.save(orderMerchantMoneyChange);
        return Result.ok("执行成功");
    }

    /**
     * 商户基本信息（userNo）
     *
     * @param
     * @return
     */
    @Override
    public OrderMerchant getMerchantByUserNo(String userNo) {
        OrderMerchant orderMerchant = orderMerchantMapper.getMerchantByUserNo(userNo);
        return orderMerchant;
    }

}