package org.suhui.modules.order.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.suhui.modules.order.entity.OrderAssurer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 去
 * @Author: jeecg-boot
 * @Date:   2019-12-29
 * @Version: V1.0
 */
public interface OrderAssurerMapper extends BaseMapper<OrderAssurer> {

    List<OrderAssurer> getAssurerByOrderData(Map<String,Object> map);
}
