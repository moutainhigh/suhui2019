package org.suhui.modules.toB.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.suhui.modules.toB.entity.OrderAssurer;

/**
 * 类说明：承兑商相关
 *
 * @author: 蔡珊珊
 * @create: 2020-04-07 22:17
 **/
public interface IOrderAssurerService extends IService<OrderAssurer> {

    /**
     * 添加承兑商
     *
     * @param
     * @return
     */
    OrderAssurer addAssurerMain(JSONObject data);

}