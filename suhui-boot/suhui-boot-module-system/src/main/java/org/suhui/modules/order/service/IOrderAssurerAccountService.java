package org.suhui.modules.order.service;

import org.suhui.modules.order.entity.OrderAssurerAccount;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 客户明细
 * @Author: jeecg-boot
 * @Date:   2019-12-29
 * @Version: V1.0
 */
public interface IOrderAssurerAccountService extends IService<OrderAssurerAccount> {

	public List<OrderAssurerAccount> selectByMainId(String mainId);
}
