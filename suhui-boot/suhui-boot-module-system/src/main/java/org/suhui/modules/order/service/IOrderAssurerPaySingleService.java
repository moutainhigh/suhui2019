package org.suhui.modules.order.service;

import org.suhui.common.api.vo.Result;
import org.suhui.modules.order.entity.OrderAssurerPaySingle;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 缴费单管理
 * @Author: jeecg-boot
 * @Date:   2020-02-19
 * @Version: V1.0
 */
public interface IOrderAssurerPaySingleService extends IService<OrderAssurerPaySingle> {

    Result<Object> changeSingleState(String ids,String state);
}
