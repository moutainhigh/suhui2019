package org.suhui.modules.toB.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.suhui.modules.toB.entity.OrderMerchant;

/**
 * 类说明：商户相关
 *
 * @author: 蔡珊珊
 * @create: 2020-04-07 23:14
 **/
@Repository
public interface OrderMerchantMapper extends BaseMapper<OrderMerchant> {
    //商户基本信息（userNo）
    OrderMerchant getMerchantByUserNo(@Param("userNo") String userNo);

}
