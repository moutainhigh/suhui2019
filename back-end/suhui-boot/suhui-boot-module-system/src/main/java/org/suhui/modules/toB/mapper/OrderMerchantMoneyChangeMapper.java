package org.suhui.modules.toB.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.suhui.modules.toB.entity.OrderAssurerMoneyChange;
import org.suhui.modules.toB.entity.OrderMerchantMoneyChange;

import java.util.List;

/**
 * 类说明：商户金额变动
 *
 * @author: 蔡珊珊
 * @create: 2020-04-08 22:15
 **/
@Repository
public interface OrderMerchantMoneyChangeMapper extends BaseMapper<OrderMerchantMoneyChange> {
    //列表
    public List<OrderMerchantMoneyChange> getInitListData();
}