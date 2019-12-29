package org.suhui.modules.order.service.impl;

import org.suhui.common.api.vo.Result;
import org.suhui.modules.order.entity.OrderMain;
import org.suhui.modules.order.mapper.OrderMainMapper;
import org.suhui.modules.order.service.IOrderMainService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.suhui.modules.utils.BaseUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 订单表
 * @Author: jeecg-boot
 * @Date:   2019-12-29
 * @Version: V1.0
 */
@Service
public class OrderMainServiceImpl extends ServiceImpl<OrderMainMapper, OrderMain> implements IOrderMainService {

    /**
     * 创建订单主方法
     */
    @Override
    public Result<Object> manageOrder(OrderMain orderMain) {
        Result<Object> result = new Result<Object>();
        // 判断必填项是否有值
        String checkValue = orderMain.checkCreateRequireValue();
        if(BaseUtil.Base_HasValue(checkValue)){
            return Result.error(402,checkValue);
        }
        result.success("订单创建成功");
        return result;
    }


}
