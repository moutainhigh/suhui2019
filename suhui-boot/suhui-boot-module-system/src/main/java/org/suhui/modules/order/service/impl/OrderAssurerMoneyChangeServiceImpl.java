package org.suhui.modules.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.suhui.modules.order.entity.OrderAssurerMoneyChange;
import org.suhui.modules.order.mapper.OrderAssurerMoneyChangeMapper;
import org.suhui.modules.order.service.IOrderAssurerMoneyChangeService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 承兑商金额变动
 * @Author: jeecg-boot
 * @Date:   2020-02-22
 * @Version: V1.0
 */
@Service
public class OrderAssurerMoneyChangeServiceImpl extends ServiceImpl<OrderAssurerMoneyChangeMapper, OrderAssurerMoneyChange> implements IOrderAssurerMoneyChangeService {

    @Autowired
    OrderAssurerMoneyChangeMapper orderAssurerMoneyChangeMapper;

    @Override
    public List<OrderAssurerMoneyChange> getInitData() {
        return orderAssurerMoneyChangeMapper.getInitListData();
    }
}
