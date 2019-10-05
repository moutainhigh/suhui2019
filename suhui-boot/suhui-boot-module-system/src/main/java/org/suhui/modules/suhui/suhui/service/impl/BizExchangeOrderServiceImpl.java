package org.suhui.modules.suhui.suhui.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.suhui.modules.suhui.suhui.entity.BizExchangeOrder;
import org.suhui.modules.suhui.suhui.mapper.BizExchangeOrderMapper;
import org.suhui.modules.suhui.suhui.mapper.BizRechargeOrderMapper;
import org.suhui.modules.suhui.suhui.service.IBizExchangeOrderService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 换汇记录表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Service
public class BizExchangeOrderServiceImpl extends ServiceImpl<BizExchangeOrderMapper, BizExchangeOrder> implements IBizExchangeOrderService {

    @Autowired
    private BizExchangeOrderMapper bizExchangeOrderMapper ;

    @Override
    public Map<String,String> getExchargeOrderByExchargeNo(Map<String,String> map ) {
        return bizExchangeOrderMapper.getExchargeOrderByExchargeNo(map);
    }


}
