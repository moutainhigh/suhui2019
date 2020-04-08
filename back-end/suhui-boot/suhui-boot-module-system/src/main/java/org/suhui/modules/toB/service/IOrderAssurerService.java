package org.suhui.modules.toB.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.suhui.common.api.vo.Result;
import org.suhui.modules.toB.entity.OrderAssurer;

/**
 * 类说明：承兑商相关
 *
 * @author: 蔡珊珊
 * @create: 2020-04-07 22:17
 **/
public interface IOrderAssurerService extends IService<OrderAssurer> {

    /**
     * 添加
     *
     * @param
     * @return
     */
    OrderAssurer addAssurerMain(JSONObject data);

    /**
     * 批量审核
     *
     * @param
     * @return
     */
    Result<Object> auditPassAssurer(String ids);

    /**
     * 更改 保证金、租赁金
     *
     * @param
     * @return
     */
    Result<Object> changeAssurerMoney(JSONObject jsonObject);
}