package org.suhui.modules.order.service.impl;

import org.suhui.modules.order.entity.OrderAssurer;
import org.suhui.modules.order.entity.OrderAssurerAccount;
import org.suhui.modules.order.entity.OrderMain;
import org.suhui.modules.order.mapper.OrderAssurerAccountMapper;
import org.suhui.modules.order.mapper.OrderAssurerMapper;
import org.suhui.modules.order.service.IOrderAssurerService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Collection;
import java.util.Map;

/**
 * @Description: 去
 * @Author: jeecg-boot
 * @Date:   2019-12-29
 * @Version: V1.0
 */
@Service
public class OrderAssurerServiceImpl extends ServiceImpl<OrderAssurerMapper, OrderAssurer> implements IOrderAssurerService {

	@Autowired
	private OrderAssurerMapper orderAssurerMapper;
	@Autowired
	private OrderAssurerAccountMapper orderAssurerAccountMapper;
	
	@Override
	@Transactional
	public void saveMain(OrderAssurer orderAssurer, List<OrderAssurerAccount> orderAssurerAccountList) {
		orderAssurerMapper.insert(orderAssurer);
		for(OrderAssurerAccount entity:orderAssurerAccountList) {
			//外键设置
			entity.setAssurerId(orderAssurer.getId());
			orderAssurerAccountMapper.insert(entity);
		}
	}

	@Override
	@Transactional
	public void updateMain(OrderAssurer orderAssurer,List<OrderAssurerAccount> orderAssurerAccountList) {
		orderAssurerMapper.updateById(orderAssurer);
		
		//1.先删除子表数据
		orderAssurerAccountMapper.deleteByMainId(orderAssurer.getId());
		
		//2.子表数据重新插入
		for(OrderAssurerAccount entity:orderAssurerAccountList) {
			//外键设置
			entity.setAssurerId(orderAssurer.getId());
			orderAssurerAccountMapper.insert(entity);
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		orderAssurerAccountMapper.deleteByMainId(id);
		orderAssurerMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			orderAssurerAccountMapper.deleteByMainId(id.toString());
			orderAssurerMapper.deleteById(id);
		}
	}

	@Override
	public Map getAssurerByOrder(OrderMain orderMain) {
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("order_money",orderMain.getTargetCurrencyMoney());
		List<Map> orderAssurers = orderAssurerMapper.getAssurerByOrderData(paramMap);
		return orderAssurers.get(0);
	}

}
