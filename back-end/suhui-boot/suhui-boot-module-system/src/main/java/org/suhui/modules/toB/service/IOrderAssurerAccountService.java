package org.suhui.modules.toB.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.suhui.modules.toB.entity.OrderAssurerAccount;

import java.util.List;

/**
 * 类说明：承兑商账户明细
 *
 * @author: 蔡珊珊
 * @create: 2020-04-12 21:01
 **/
public interface IOrderAssurerAccountService extends IService<OrderAssurerAccount> {
    //为承兑商选择一个支付账户
    OrderAssurerAccount getAssurerAccountByOrderPay(String assurerId, Double orderMoney, String userCollectionMethod, String userCollectionAreaCode);

    //为承兑商选择一个收款账户-选择收款额度小的
    OrderAssurerAccount getAssurerAccountByOrderCollection(String assurerId, String userPayMethod, String userPayAreaCode);
}
