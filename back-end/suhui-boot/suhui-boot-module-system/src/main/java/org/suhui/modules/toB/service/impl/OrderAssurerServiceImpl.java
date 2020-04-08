package org.suhui.modules.toB.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.suhui.common.constant.CommonConstant;
import org.suhui.common.util.PasswordUtil;
import org.suhui.common.util.UUIDGenerator;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.toB.entity.OrderAssurer;
import org.suhui.modules.toB.entity.PayUserInfo;
import org.suhui.modules.toB.entity.PayUserLogin;
import org.suhui.modules.toB.mapper.OrderAssurerMapper;
import org.suhui.modules.toB.service.IOrderAssurerService;
import org.suhui.modules.toB.service.IPayUserInfoService;
import org.suhui.modules.toB.service.IPayUserLoginService;

/**
 * 类说明：承兑商相关
 *
 * @author: 蔡珊珊
 * @create: 2020-04-07 22:30
 **/
@Service
public class OrderAssurerServiceImpl extends ServiceImpl<OrderAssurerMapper, OrderAssurer> implements IOrderAssurerService {

    @Autowired
    private IPayUserLoginService iPayUserLoginService;

    @Autowired
    private IPayUserInfoService iPayUserInfoService;

    /**
     * 添加承兑商
     *
     * @param
     * @return
     */
    @Override
    public OrderAssurer addAssurerMain(JSONObject data) {
        System.out.println(data);
        String phone = data.getString("phone");
        String pwd = data.getString("password");
        String areacode = data.getString("areacode");
        String countryCode = data.getString("countryCode");
        String assurerName = data.getString("assurerName");
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
        payUserLogin.setUserType(CommonConstant.usertype_toB_acceptor);
        iPayUserLoginService.save(payUserLogin);
        PayUserInfo payUserInfo = new PayUserInfo();
        payUserInfo.setUserNo(userno);
        payUserInfo.setPhoneNo(phone);
        payUserInfo.setUserType(CommonConstant.usertype_toB_acceptor);
        iPayUserInfoService.save(payUserInfo);
        OrderAssurer orderAssurer = new OrderAssurer();
        orderAssurer.setUserNo(userno);
        orderAssurer.setAssurerName(assurerName);
        orderAssurer.setCountryCode(countryCode);
        orderAssurer.setAssurerPhone(phone);
        orderAssurer.setAssurerRate(data.getDouble("assurerRate"));
        orderAssurer.setTotalLimit(data.getDouble("totalLimit") * 100);
        orderAssurer.setCanUseLimit(data.getDouble("totalLimit") * 100);
        orderAssurer.setEnsureProportion(data.getDouble("ensureProportion"));
        this.save(orderAssurer);
        return orderAssurer;
    }

}
