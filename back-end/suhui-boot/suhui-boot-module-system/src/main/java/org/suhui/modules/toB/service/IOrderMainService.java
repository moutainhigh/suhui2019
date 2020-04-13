package org.suhui.modules.toB.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.suhui.common.api.vo.Result;
import org.suhui.modules.toB.entity.OrderMain;

/**
 * 类说明：订单相关
 *
 * @author: 蔡珊珊
 * @create: 2020-04-12 17:13
 **/
public interface IOrderMainService extends IService<OrderMain> {
    //创建订单
    Result<Object> manageOrderByAuto(OrderMain orderMain);

    //取消订单
    Result<Object> revokeOrderAdmin(String orderId);

    //用户确认已支付
    Result<Object> userPayConfirm(String orderId, String voucher);

    //承兑商确认已收款
    Result<Object> assurerCollectionConfirm(String orderId);

    //承兑商确认已兑付
    Result<Object> assurerPayConfirm(String orderId, String fileList);

    //用户确认已收款
    Result<Object> userCollectionConfirm(String orderId);

}
