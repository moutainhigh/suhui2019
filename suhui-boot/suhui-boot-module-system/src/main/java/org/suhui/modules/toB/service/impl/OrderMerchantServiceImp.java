package org.suhui.modules.toB.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.suhui.common.constant.CommonConstant;
import org.suhui.common.util.PasswordUtil;
import org.suhui.common.util.UUIDGenerator;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.suhui.suhui.entity.PayUserInfo;
import org.suhui.modules.suhui.suhui.entity.PayUserLogin;
import org.suhui.modules.suhui.suhui.service.IPayUserInfoService;
import org.suhui.modules.suhui.suhui.service.IPayUserLoginService;
import org.suhui.modules.toB.entity.OrderMerchant;
import org.suhui.modules.toB.mapper.OrderMerchantMapper;
import org.suhui.modules.toB.service.IOrderMerchantService;

/**
 * 类说明：商户相关
 *
 * @author: 蔡珊珊
 * @create:  2020-04-07 23:10
 **/
@Service
public class OrderMerchantServiceImp extends ServiceImpl<OrderMerchantMapper, OrderMerchant> implements IOrderMerchantService {

    @Autowired
    private IPayUserLoginService iPayUserLoginService;

    @Autowired
    private IPayUserInfoService iPayUserInfoService;

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
        orderMerchant.setMerchantName(phone);
        orderMerchant.setMerchantPhone(phone);
        orderMerchant.setMerchantRate(data.getDouble("assurerRate"));
        orderMerchant.setTotalLimit(data.getDouble("totalLimit") * 100);
        orderMerchant.setCanUseLimit(data.getDouble("totalLimit") * 100);
        this.save(orderMerchant);
        return orderMerchant;
    }
}