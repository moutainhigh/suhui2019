package org.suhui.modules.toB.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.suhui.modules.toB.entity.OrderAssurerMoneyChange;
import org.suhui.modules.toB.mapper.OrderAssurerMoneyChangeMapper;
import org.suhui.modules.toB.service.IOrderAssurerMoneyChangeService;

import java.util.List;

/**
 * 类说明：承兑商金额变动
 *
 * @author: 蔡珊珊
 * @create: 2020-04-08 22:02
 **/
@Service
public class OrderAssurerMoneyChangeServiceImpl extends ServiceImpl<OrderAssurerMoneyChangeMapper, OrderAssurerMoneyChange> implements IOrderAssurerMoneyChangeService {

    @Autowired
    OrderAssurerMoneyChangeMapper orderAssurerMoneyChangeMapper;

    /**
     * 列表
     *
     * @param
     * @return
     */
    @Override
    public List<OrderAssurerMoneyChange> getInitData() {
        return orderAssurerMoneyChangeMapper.getInitListData();
    }
}
