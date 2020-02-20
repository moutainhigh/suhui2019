package org.suhui.modules.order.service;

import org.suhui.common.api.vo.Result;
import org.suhui.modules.order.entity.OrderPlatformAccount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 平台账户管理
 * @Author: jeecg-boot
 * @Date:   2020-02-19
 * @Version: V1.0
 */
public interface IOrderPlatformAccountService extends IService<OrderPlatformAccount> {

    Result<Object> changeAccountState(String ids,String type);
}
