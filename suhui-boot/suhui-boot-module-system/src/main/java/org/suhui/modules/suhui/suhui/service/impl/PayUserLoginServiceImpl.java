package org.suhui.modules.suhui.suhui.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.suhui.modules.suhui.suhui.entity.PayUserLogin;
import org.suhui.modules.suhui.suhui.mapper.PayUserLoginMapper;
import org.suhui.modules.suhui.suhui.service.IPayUserLoginService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: app用户登陆
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Service
public class PayUserLoginServiceImpl extends ServiceImpl<PayUserLoginMapper, PayUserLogin> implements IPayUserLoginService {

    @Autowired
    private PayUserLoginMapper payUserLoginMapper ;

    @Override
    public PayUserLogin getUserByPhone(String phone) {
        return payUserLoginMapper.getUserByPhone(phone);
    }
}
