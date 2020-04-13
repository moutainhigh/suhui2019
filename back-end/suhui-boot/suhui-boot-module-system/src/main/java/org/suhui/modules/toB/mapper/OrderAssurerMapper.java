package org.suhui.modules.toB.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.suhui.modules.toB.entity.OrderAssurer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * 类说明：承兑商相关
 *
 * @author: 蔡珊珊
 * @create: 2020-04-07 22:35
 **/
@Repository
public interface OrderAssurerMapper extends BaseMapper<OrderAssurer> {

    List<OrderAssurer> getAssurerByOrderData(Map<String, Object> map);

    //承兑商基本信息（userNo）
    OrderAssurer getAssurerByUserNo(@Param("userNo") String userNo);

    OrderAssurer getByIdForUpdate(@Param("id") String id);

}