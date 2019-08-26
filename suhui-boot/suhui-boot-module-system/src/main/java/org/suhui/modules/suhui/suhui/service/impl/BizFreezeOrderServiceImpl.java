package org.suhui.modules.suhui.suhui.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.suhui.modules.suhui.suhui.entity.BizFreezeOrder;
import org.suhui.modules.suhui.suhui.mapper.BizFreezeOrderMapper;
import org.suhui.modules.suhui.suhui.mapper.BizRechargeOrderMapper;
import org.suhui.modules.suhui.suhui.service.IBizFreezeOrderService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 冻结记录表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Service
public class BizFreezeOrderServiceImpl extends ServiceImpl<BizFreezeOrderMapper, BizFreezeOrder> implements IBizFreezeOrderService {

    @Autowired
    private BizFreezeOrderMapper bizFreezeOrderMapper ;

    @Override
    public Map<String,String> getFreezeOrderByTradeNo(Map<String,String> map ) {
        return bizFreezeOrderMapper.getFreezeOrderByTradeNo(map);
    }
}
