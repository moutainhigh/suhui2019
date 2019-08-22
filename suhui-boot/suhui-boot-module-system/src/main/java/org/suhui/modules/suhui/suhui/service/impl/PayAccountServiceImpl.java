package org.suhui.modules.suhui.suhui.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.suhui.modules.suhui.suhui.entity.PayAccount;
import org.suhui.modules.suhui.suhui.entity.PayUserLogin;
import org.suhui.modules.suhui.suhui.mapper.PayAccountMapper;
import org.suhui.modules.suhui.suhui.mapper.PayUserLoginMapper;
import org.suhui.modules.suhui.suhui.service.IPayAccountService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 账户表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Service
public class PayAccountServiceImpl extends ServiceImpl<PayAccountMapper, PayAccount> implements IPayAccountService {

    @Autowired
    private PayAccountMapper payAccountMapper ;

    @Override
    public Map<String,String> getPayAccountByUserNo(Map<String,String> map ) {
        return payAccountMapper.getPayAccountByUserNo(map);
    }

    @Override
    public List<Map<String,String>> getPayAccountMoneyByUserNo(Map<String,String> map ) {
        return payAccountMapper.getPayAccountMoneyByUserNo(map);
    }

    @Override
    public Map<String,String> getPayAccountAssetByUserNo(Map<String,String> map ) {
        return payAccountMapper.getPayAccountAssetByUserNo(map);
    }

    /**
     * 通过 用户id 获取 身份信息下的支付通道
     * @param map
     * @return
     */
    @Override
    public Map<String,String> getPayIdentityChannelAccountByUserNo(Map<String,String> map ) {
        return payAccountMapper.getPayIdentityChannelAccountByUserNo(map);
    }
}
