package org.suhui.modules.toB.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.suhui.modules.toB.entity.PayIdentityChannelAccount;
import org.suhui.modules.toB.mapper.PayIdentityChannelAccountMapper;
import org.suhui.modules.toB.service.IPayIdentityChannelAccountService;

import java.util.List;
import java.util.Map;

/**
 * 类说明：用户支付渠道
 *
 * @author: 蔡珊珊
 * @create: 2020-04-12 19:54
 **/
@Service
public class PayIdentityChannelAccountServiceImpl extends ServiceImpl<PayIdentityChannelAccountMapper, PayIdentityChannelAccount> implements IPayIdentityChannelAccountService {
    @Autowired
    private  PayIdentityChannelAccountMapper payIdentityChannelAccountMapper ;

    @Override
    public List<Map<String,String>> getChannelAccountInfoByUserNo(Map<String,String> map ) {
        return payIdentityChannelAccountMapper.getChannelAccountInfoByUserNo(map);
    }
}
