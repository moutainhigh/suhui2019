package org.suhui.modules.toB.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.suhui.modules.toB.entity.OrderPlatformAccount;

import java.util.List;
import java.util.Map;

/**
 * 接口说明：平台支付账号
 *
 * @author: 蔡珊珊
 * @create: 2020-04-14 14:20
 */
@Repository
public interface OrderPlatformAccountMapper extends BaseMapper<OrderPlatformAccount> {
    //根据区域代码查
    List<OrderPlatformAccount> getAccountListByAreaCode(Map<String, Object> map);
    //根据区域代码 和 银行账户 查
    OrderPlatformAccount getAccountByAreaCodeAndAccountNo(@Param("accountNo") String accountNo, @Param("areaCode") String areaCode);
}

