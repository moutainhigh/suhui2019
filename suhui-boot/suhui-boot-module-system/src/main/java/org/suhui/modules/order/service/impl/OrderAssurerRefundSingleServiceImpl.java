package org.suhui.modules.order.service.impl;

import org.suhui.common.api.vo.Result;
import org.suhui.modules.order.entity.OrderAssurerRefundSingle;
import org.suhui.modules.order.mapper.OrderAssurerRefundSingleMapper;
import org.suhui.modules.order.service.IOrderAssurerRefundSingleService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.suhui.modules.utils.BaseUtil;

/**
 * @Description: 退款单
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
@Service
public class OrderAssurerRefundSingleServiceImpl extends ServiceImpl<OrderAssurerRefundSingleMapper, OrderAssurerRefundSingle> implements IOrderAssurerRefundSingleService {

    /**
     * 更改状态
     */
    @Override
    public Result<Object> changeSingleState(String ids, String state) {
        String[] idArr = ids.split(",");
        for (String orderId : idArr) {
            OrderAssurerRefundSingle orderAssurerRefundSingle = getById(orderId);
            if (BaseUtil.Base_HasValue(orderAssurerRefundSingle)) {
                if (state.equals("pass") && (orderAssurerRefundSingle.getRefundSingleState().equals("auditing") || orderAssurerRefundSingle.getRefundSingleState().equals("reject"))) {
                    orderAssurerRefundSingle.setRefundSingleState(state);
                } else {
                    if (orderAssurerRefundSingle.getRefundSingleState().equals("auditing")) {
                        orderAssurerRefundSingle.setRefundSingleState(state);
                    }
                }
                updateById(orderAssurerRefundSingle);
            }
        }
        return Result.ok("操作成功");
    }

    /**
     * 上传打款凭证
     */
    @Override
    public Result<Object> uploadRefundVoucherConfirm(String id, String file) {
        OrderAssurerRefundSingle orderAssurerRefundSingle = getById(id);
        if(!BaseUtil.Base_HasValue(orderAssurerRefundSingle)){
            return Result.error(500,"实体不存在");
        }
        orderAssurerRefundSingle.setRefundVoucher(file);
        updateById(orderAssurerRefundSingle);
        return Result.ok("操作成功");
    }
}
