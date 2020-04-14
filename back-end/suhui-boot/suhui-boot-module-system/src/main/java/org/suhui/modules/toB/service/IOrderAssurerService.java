package org.suhui.modules.toB.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.suhui.common.api.vo.Result;
import org.suhui.modules.toB.entity.OrderAssurer;
import org.suhui.modules.toB.entity.ToCOrderMain;

import java.util.Map;

/**
 * 类说明：承兑商相关
 *
 * @author: 蔡珊珊
 * @create: 2020-04-07 22:17
 **/
public interface IOrderAssurerService extends IService<OrderAssurer> {
    //新增
    OrderAssurer addAssurerMain(JSONObject data);

    //批量审核
    Result<Object> auditPassAssurer(String ids);

    //更改 保证金、租赁金
    Result<Object> changeAssurerMoney(JSONObject jsonObject);

    //承兑商基本信息（userNo）
    OrderAssurer getAssurerByUserNo(String userNo);

    //为订单查询最优承兑商及支付账户
    Map<String, Object> getAssurerByOrder(ToCOrderMain toCOrderMain);
}