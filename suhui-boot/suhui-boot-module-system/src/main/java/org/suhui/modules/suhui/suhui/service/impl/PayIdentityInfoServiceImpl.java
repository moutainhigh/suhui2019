package org.suhui.modules.suhui.suhui.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.suhui.modules.suhui.suhui.entity.PayIdentityInfo;
import org.suhui.modules.suhui.suhui.mapper.PayAccountMapper;
import org.suhui.modules.suhui.suhui.mapper.PayIdentityInfoMapper;
import org.suhui.modules.suhui.suhui.service.IPayIdentityInfoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 用户身份表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Service
public class PayIdentityInfoServiceImpl extends ServiceImpl<PayIdentityInfoMapper, PayIdentityInfo> implements IPayIdentityInfoService {

    @Autowired
    private PayIdentityInfoMapper payIdentityInfoMapper ;

    @Override
    public Map<String,String> getIdentityInfoByIdentityNo(Map<String,String> map ) {
        return payIdentityInfoMapper.getIdentityInfoByIdentityNo(map);
    }


    @Override
    public Map<String,String> getIdentityInfoByUserNo(Map<String,String> map ) {
        return payIdentityInfoMapper.getIdentityInfoByUserNo(map);
    }



}
