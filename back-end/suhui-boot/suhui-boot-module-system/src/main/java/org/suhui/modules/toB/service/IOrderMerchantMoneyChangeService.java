package org.suhui.modules.toB.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.suhui.modules.toB.entity.OrderAssurerMoneyChange;
import org.suhui.modules.toB.entity.OrderMerchantMoneyChange;

import java.util.List;

/**
 * 类说明：商户金额变动
 *
 * @author: 蔡珊珊
 * @create: 2020-04-08 22:07
 **/
public interface IOrderMerchantMoneyChangeService extends IService<OrderMerchantMoneyChange> {
    /**
     * 列表
     *
     * @param
     * @return
     */
    List<OrderMerchantMoneyChange> getInitData();
}

