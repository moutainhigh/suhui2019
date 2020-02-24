package org.suhui.modules.order.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.suhui.modules.order.entity.OrderMain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 订单表
 * @Author: jeecg-boot
 * @Date:   2019-12-29
 * @Version: V1.0
 */
public interface OrderMainMapper extends BaseMapper<OrderMain> {

    List<OrderMain> findByUserId(Map map);

    // 获取承兑商未完成订单总金额
    Double sumAssurerNotFinishMoney(@Param("assurerId") String assurerId);
}
