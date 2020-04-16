package org.suhui.modules.toB.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.suhui.common.api.vo.Result;
import org.suhui.modules.toB.entity.OrderMain;

/**
 * 接口说明：订单相关
 *
 * @author: 蔡珊珊
 * @create: 2020-04-14 13:47
 */
public interface IOrderMainService extends IService<OrderMain> {
    //创建收款订单
    Result<Object> createPaymentOrder(OrderMain orderMain);
    //确认已收款
    Result<Object> confirmPaymentOrder(String orderId);
    //创建提款订单
    Result<Object> createWithdrawalOrder(OrderMain orderMain);
    //确认已处理
    Result<Object> confirmWithdrawalOrder(String orderId);
}