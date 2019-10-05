package org.suhui.modules.suhui.suhui.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.suhui.modules.suhui.suhui.entity.PayUserInfo;
import org.suhui.modules.suhui.suhui.mapper.PayUserInfoMapper;
import org.suhui.modules.suhui.suhui.mapper.PayUserLoginMapper;
import org.suhui.modules.suhui.suhui.service.IPayUserInfoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 用户信息表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
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
