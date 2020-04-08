package org.suhui.modules.toB.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.suhui.modules.toB.entity.PayUserLogin;
import org.suhui.modules.toB.mapper.PayUserLoginMapper;
import org.suhui.modules.toB.service.IPayUserLoginService;

/**
 * 类说明：
 *
 * @author: 蔡珊珊
 * @create: 2020-04-08 11:49
 */
@Service
public class PayUserLoginServiceImpl extends ServiceImpl<PayUserLoginMapper, PayUserLogin> implements IPayUserLoginService {

    @Autowired
    private PayUserLoginMapper payUserLoginMapper ;

    @Override
    public PayUserLogin getUserByPhone(String phone , String areacode) {
        return payUserLoginMapper.getUserByPhone(phone ,areacode);
    }

    @Override
    public PayUserLogin getUserByPhoneAndUserType(String phone , String areacode, Integer usertype) {
        return payUserLoginMapper.getUserByPhoneAndUserType(phone ,areacode,usertype);
    }
}

