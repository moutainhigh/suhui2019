package org.suhui.modules.order.service.impl;

import org.suhui.common.api.vo.Result;
import org.suhui.modules.order.entity.OrderAssurerPaySingle;
import org.suhui.modules.order.entity.OrderPlatformAccount;
import org.suhui.modules.order.mapper.OrderAssurerPaySingleMapper;
import org.suhui.modules.order.service.IOrderAssurerPaySingleService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.suhui.modules.utils.BaseUtil;

/**
 * @Description: 缴费单管理
 * @Author: jeecg-boot
 * @Date: 2020-02-19
 * @Version: V1.0
 */
@Service
public class OrderAssurerPaySingleServiceImpl extends ServiceImpl<OrderAssurerPaySingleMapper, OrderAssurerPaySingle> implements IOrderAssurerPaySingleService {

    /**
     * 改变状态
     */
    @Override
    public Result<Object> changeSingleState(String ids, String state) {
        String[] idArr = ids.split(",");
        for (String orderId : idArr) {
            OrderAssurerPaySingle orderAssurerPaySingle = getById(orderId);
            if (BaseUtil.Base_HasValue(orderAssurerPaySingle)) {
                if (state.equals("pass") && (orderAssurerPaySingle.getPaySingleState().equals("auditing") || orderAssurerPaySingle.getPaySingleState().equals("reject"))) {
                    orderAssurerPaySingle.setPaySingleState(state);
                } else {
                    if (orderAssurerPaySingle.getPaySingleState().equals("auditing")) {
                        orderAssurerPaySingle.setPaySingleState(state);
                    }
                }
                updateById(orderAssurerPaySingle);
            }
        }
        return Result.ok("操作成功");
    }
}
