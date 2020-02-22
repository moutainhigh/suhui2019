package org.suhui.modules.order.service;

import org.suhui.common.api.vo.Result;
import org.suhui.modules.order.entity.OrderAssurerRefundSingle;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 退款单
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
public interface IOrderAssurerRefundSingleService extends IService<OrderAssurerRefundSingle> {

    Result<Object> changeSingleState(String ids, String state);
    Result<Object> uploadRefundVoucherConfirm(String id, String file);

}
