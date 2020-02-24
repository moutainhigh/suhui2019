package org.suhui.modules.order.service.impl;

import org.suhui.common.api.vo.Result;
import org.suhui.modules.order.entity.OrderPlatformAccount;
import org.suhui.modules.order.mapper.OrderPlatformAccountMapper;
import org.suhui.modules.order.service.IOrderPlatformAccountService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.suhui.modules.utils.BaseUtil;

/**
 * @Description: 平台账户管理
 * @Author: jeecg-boot
 * @Date:   2020-02-19
 * @Version: V1.0
 */
@Service
public class OrderPlatformAccountServiceImpl extends ServiceImpl<OrderPlatformAccountMapper, OrderPlatformAccount> implements IOrderPlatformAccountService {

    /**
     * 改变平台账户状态
     */
    @Override
    public Result<Object> changeAccountState(String ids,String type) {
        String[] idArr = ids.split(",");
        Result<Object> result = new Result<>();
        boolean check = true;
        for (String orderId : idArr) {
            OrderPlatformAccount orderPlatformAccount = getById(orderId);
            if (!BaseUtil.Base_HasValue(orderPlatformAccount)) {
                result = Result.error(530, "账户不存在");
                check = false;
                break;
            }
            if (type.equals("enable")) {
                if(!orderPlatformAccount.getAccountState().equals(1)){
                    orderPlatformAccount.setAccountState(1);
                }
            }else{
                if(orderPlatformAccount.getAccountState().equals(1)){
                    orderPlatformAccount.setAccountState(0);
                }
            }
            updateById(orderPlatformAccount);
        }
        if (!check) {
            return result;
        }
        return Result.ok("操作成功");
    }
}
