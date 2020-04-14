package org.suhui.modules.toB.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.suhui.modules.toB.entity.OrderPlatformAccount;

import java.util.HashMap;
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
    List<OrderPlatformAccount> getAccountListByAreaCode(Map<String,Object> map);
    Map<String, Object> paramMap = new HashMap<>();
}

