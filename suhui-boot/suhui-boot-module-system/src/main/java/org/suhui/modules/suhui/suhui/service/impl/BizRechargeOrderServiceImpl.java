package org.suhui.modules.suhui.suhui.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.suhui.modules.suhui.suhui.entity.BizRechargeOrder;
import org.suhui.modules.suhui.suhui.mapper.BizRechargeOrderMapper;
import org.suhui.modules.suhui.suhui.mapper.PayAccountMapper;
import org.suhui.modules.suhui.suhui.service.IBizRechargeOrderService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 充值交易表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Service
public class BizRechargeOrderServiceImpl extends ServiceImpl<BizRechargeOrderMapper, BizRechargeOrder> implements IBizRechargeOrderService {
    @Autowired
    private BizRechargeOrderMapper bizRechargeOrderMapper ;

    @Override
    public Map<String,String> getRechargeOrderByRechargeNo(Map<String,String> map ) {
        return bizRechargeOrderMapper.getRechargeOrderByRechargeNo(map);
    }


    @Override
    public BizRechargeOrder getRechargeOrderObjectByRechargeNo(Map<String,String> map ) {
        return bizRechargeOrderMapper.getRechargeOrderObjectByRechargeNo(map);
    }

}
