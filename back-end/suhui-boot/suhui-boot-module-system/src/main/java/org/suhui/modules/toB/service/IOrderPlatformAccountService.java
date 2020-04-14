package org.suhui.modules.toB.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.suhui.modules.toB.entity.OrderPlatformAccount;

import java.util.List;
import java.util.Map;

/**
 * 接口说明：平台支付账户
 *
 * @author: 蔡珊珊
 * @create: 2020-04-14 14:22
 */
public interface IOrderPlatformAccountService extends IService<OrderPlatformAccount> {
    //获取平台支付账户 （根据区域代码）
    List<OrderPlatformAccount> getAccountListByAreaCode(Map<String, Object> map);
}
