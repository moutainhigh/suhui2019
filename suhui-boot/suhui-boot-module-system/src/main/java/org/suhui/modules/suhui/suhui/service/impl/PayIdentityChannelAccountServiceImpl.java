package org.suhui.modules.suhui.suhui.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.suhui.modules.suhui.suhui.entity.PayIdentityChannelAccount;
import org.suhui.modules.suhui.suhui.mapper.PayIdentityChannelAccountMapper;
import org.suhui.modules.suhui.suhui.service.IPayIdentityChannelAccountService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 实体账号表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Service
public class PayIdentityChannelAccountServiceImpl extends ServiceImpl<PayIdentityChannelAccountMapper, PayIdentityChannelAccount> implements IPayIdentityChannelAccountService {

    @Autowired
    private  PayIdentityChannelAccountMapper payIdentityChannelAccountMapper ;

    @Override
    public List<Map<String,String>> getChannelAccountInfoByUserNo(Map<String,String> map ) {
        return payIdentityChannelAccountMapper.getChannelAccountInfoByUserNo(map);
    }
}
