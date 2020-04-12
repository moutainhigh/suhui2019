package org.suhui.modules.toB.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.suhui.modules.toB.entity.OrderMerchantMoneyChange;
import org.suhui.modules.toB.mapper.OrderMerchantMoneyChangeMapper;
import org.suhui.modules.toB.service.IOrderMerchantMoneyChangeService;

import java.util.List;

/**
 * 类说明：商户金额变动
 *
 * @author: 蔡珊珊
 * @create: 2020-04-08 22:08
 **/
@Service
public class OrderMerchantMoneyChangeServiceImpl extends ServiceImpl<OrderMerchantMoneyChangeMapper, OrderMerchantMoneyChange> implements IOrderMerchantMoneyChangeService {

    @Autowired
    OrderMerchantMoneyChangeMapper orderMerchantMoneyChangeMapper;

    /**
     * 列表
     *
     * @param
     * @return
     */
    @Override
    public List<OrderMerchantMoneyChange> getInitData() {
        return orderMerchantMoneyChangeMapper.getInitListData();
    }
}