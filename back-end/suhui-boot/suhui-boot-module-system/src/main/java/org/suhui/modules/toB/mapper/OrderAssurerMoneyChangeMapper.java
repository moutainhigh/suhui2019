package org.suhui.modules.toB.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.suhui.modules.toB.entity.OrderAssurerMoneyChange;

import java.util.List;

/**
 * 类说明：承兑商金额变动
 *
 * @author: 蔡珊珊
 * @create: 2020-04-08 22:03
 **/
public interface OrderAssurerMoneyChangeMapper extends BaseMapper<OrderAssurerMoneyChange> {
    /**
     * 列表
     *
     * @param
     * @return
     */
    public List<OrderAssurerMoneyChange> getInitListData();
}
