package org.suhui.modules.toB.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.suhui.modules.toB.entity.OrderPlatformAccount;
import org.suhui.modules.toB.mapper.OrderPlatformAccountMapper;
import org.suhui.modules.toB.service.IOrderPlatformAccountService;

import java.util.List;
import java.util.Map;

/**
 * 类说明：平台支付账户
 *
 * @author: 蔡珊珊
 * @create: 2020-04-14 14:21
 */
@Service
public class OrderPlatformAccountServiceImpl  extends ServiceImpl<OrderPlatformAccountMapper, OrderPlatformAccount> implements IOrderPlatformAccountService {

    @Autowired
    private OrderPlatformAccountMapper orderPlatformAccountMapper;

    /**
     * 获取平台支付账户 （根据区域代码）
     */
    @Override
    public List<OrderPlatformAccount> getAccountListByAreaCode(Map<String,Object> map){
        return orderPlatformAccountMapper.getAccountListByAreaCode(map);
    }
}