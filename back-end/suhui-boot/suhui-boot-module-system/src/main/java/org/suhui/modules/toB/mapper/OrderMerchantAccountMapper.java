package org.suhui.modules.toB.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.suhui.modules.toB.entity.OrderMerchantAccount;

/**
 * 接口说明：商户账户
 *
 * @author: 蔡珊珊
 * @create: 2020-04-14 17:50
 */
@Repository
public interface OrderMerchantAccountMapper extends BaseMapper<OrderMerchantAccount> {
    //商户基本信息（id）
    OrderMerchantAccount getAccountByMerchantId(@Param("merchantId") String merchantId, @Param("areaCode") String areaCode);
}
