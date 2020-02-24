package org.suhui.modules.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.suhui.modules.order.entity.OrderAssurerMoneyChange;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 承兑商金额变动
 * @Author: jeecg-boot
 * @Date:   2020-02-22
 * @Version: V1.0
 */
public interface OrderAssurerMoneyChangeMapper extends BaseMapper<OrderAssurerMoneyChange> {

   public List<OrderAssurerMoneyChange> getInitListData();
}
