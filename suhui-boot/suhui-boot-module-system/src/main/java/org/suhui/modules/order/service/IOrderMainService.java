package org.suhui.modules.order.service;

import com.alibaba.fastjson.JSONObject;
import org.suhui.common.api.vo.Result;
import org.suhui.modules.order.entity.OrderMain;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 订单表
 * @Author: jeecg-boot
 * @Date:   2019-12-29
 * @Version: V1.0
 */
public interface IOrderMainService extends IService<OrderMain> {

      Result<Object> manageOrderByAuto(OrderMain orderMain);

      Result<Object> userPayConfirm(String orderId,String voucher);

      Result<Object> assurerCollectionConfirm(String orderId);

      Result<Object> assurerPayConfirm(String orderId);

      Result<Object> userCollectionConfirm(String orderId);

      Result<Object> dispatchOrderAdmin(String orderId,String assurerId);

      JSONObject getUserPayMoney(String source, String target, String money, String token);

}
