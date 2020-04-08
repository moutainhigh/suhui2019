package org.suhui.modules.toB.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.suhui.common.api.vo.Result;
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

    /**
     * 批量审核
     *
     * @param
     * @return
     */
    Result<Object> auditPassMerchant(String ids);

    /**
     * 更改 保证金、租赁金
     *
     * @param
     * @return
     */
    Result<Object> changeMerchantMoney(JSONObject jsonObject);

    /**
     * 商户基本信息（userNo）
     *
     * @param
     * @return
     */
    OrderMerchant getMerchantByUserNo(String userNo);
}
