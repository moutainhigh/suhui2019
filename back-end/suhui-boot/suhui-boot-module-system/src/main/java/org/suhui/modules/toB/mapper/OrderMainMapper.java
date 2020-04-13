package org.suhui.modules.toB.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.suhui.modules.toB.entity.OrderMain;
import org.suhui.modules.toB.entity.OrderMerchant;

import java.util.List;
import java.util.Map;

/**
 * 类说明：订单相关
 *
 * @author: 蔡珊珊
 * @create: 2020-04-12 17:17
 **/
@Repository
public interface OrderMainMapper extends BaseMapper<OrderMain> {
    // 获取承兑商未完成订单总金额
    Double sumAssurerNotFinishMoney(@Param("assurerId") String assurerId);

    //通过用户id查询订单
    List<OrderMain> findByUserId(Map map);

    //通过OrderNo查询
    OrderMain queryByOrderNo(String orderNo);
}
