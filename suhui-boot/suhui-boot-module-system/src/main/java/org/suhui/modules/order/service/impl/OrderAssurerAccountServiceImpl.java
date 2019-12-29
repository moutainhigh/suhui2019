package org.suhui.modules.order.service.impl;

import org.suhui.modules.order.entity.OrderAssurerAccount;
import org.suhui.modules.order.mapper.OrderAssurerAccountMapper;
import org.suhui.modules.order.service.IOrderAssurerAccountService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 客户明细
 * @Author: jeecg-boot
 * @Date:   2019-12-29
 * @Version: V1.0
 */
@Service
public class OrderAssurerAccountServiceImpl extends ServiceImpl<OrderAssurerAccountMapper, OrderAssurerAccount> implements IOrderAssurerAccountService {
	
	@Autowired
	private OrderAssurerAccountMapper orderAssurerAccountMapper;
	
	@Override
	public List<OrderAssurerAccount> selectByMainId(String mainId) {
		return orderAssurerAccountMapper.selectByMainId(mainId);
	}
}
