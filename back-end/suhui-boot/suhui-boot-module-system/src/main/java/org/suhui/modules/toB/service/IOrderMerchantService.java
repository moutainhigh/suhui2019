package org.suhui.modules.toB.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.suhui.modules.toB.entity.OrderMerchant;

/**
 * 类说明：商户相关
 *
 * @author: 蔡珊珊
 * @create:  2020-04-07 23:10
 **/
public interface IOrderMerchantService extends IService<OrderMerchant> {

    /**
     * 添加商户
     *
     * @param
     * @return
     */
    OrderMerchant addMerchant(JSONObject data);
}
