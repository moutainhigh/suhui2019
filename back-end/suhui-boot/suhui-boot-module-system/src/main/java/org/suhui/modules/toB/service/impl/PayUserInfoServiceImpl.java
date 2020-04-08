package org.suhui.modules.toB.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.suhui.modules.toB.entity.PayUserInfo;
import org.suhui.modules.toB.mapper.PayUserInfoMapper;
import org.suhui.modules.toB.service.IPayUserInfoService;

/**
 * 类说明：
 *
 * @author: 蔡珊珊
 * @create: 2020-04-08 11:49
 */
@Service
public class PayUserInfoServiceImpl extends ServiceImpl<PayUserInfoMapper, PayUserInfo> implements IPayUserInfoService {

    @Autowired
    private PayUserInfoMapper payUserInfoMapper ;

    @Override
    public PayUserInfo getUserByObj(PayUserInfo payUserInfo){

        return payUserInfoMapper.getUserByObj(payUserInfo) ;

    }



    @Override
    public PayUserInfo getUserByPhone(String phone , String areacode) {
        return payUserInfoMapper.getUserByPhoneInfo(phone ,areacode);
    }
}
