package org.suhui.modules.suhui.suhui.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.suhui.modules.suhui.suhui.entity.BizWithDrawOrder;
import org.suhui.modules.suhui.suhui.mapper.BizRechargeOrderMapper;
import org.suhui.modules.suhui.suhui.mapper.BizWithDrawOrderMapper;
import org.suhui.modules.suhui.suhui.service.IBizWithDrawOrderService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 提现订单表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Service
public class BizWithDrawOrderServiceImpl extends ServiceImpl<BizWithDrawOrderMapper, BizWithDrawOrder> implements IBizWithDrawOrderService {

    @Autowired
    private BizWithDrawOrderMapper bizWithDrawOrderMapper ;

    @Override
    public Map<String,String> getWithDrawOrderByWithDrawNo(Map<String,String> map ) {
        return bizWithDrawOrderMapper.getWithDrawOrderByWithDrawNo(map);
    }
}
