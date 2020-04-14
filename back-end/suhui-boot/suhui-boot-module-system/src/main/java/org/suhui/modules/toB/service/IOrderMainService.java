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
    //创建订单
    Result<Object> add(OrderMain orderMain);
}