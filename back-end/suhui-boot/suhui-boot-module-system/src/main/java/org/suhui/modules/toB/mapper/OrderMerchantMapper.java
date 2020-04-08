package org.suhui.modules.toB.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.suhui.modules.toB.entity.OrderMerchant;

/**
 * 类说明：商户相关
 *
 * @author: 蔡珊珊
 * @create: 2020-04-07 23:14
 **/
public interface OrderMerchantMapper extends BaseMapper<OrderMerchant> {
    /**
     * 商户基本信息（userNo）
     *
     * @param
     * @return
     */
    OrderMerchant getMerchantByUserNo(@Param("userNo") String userNo);

}
