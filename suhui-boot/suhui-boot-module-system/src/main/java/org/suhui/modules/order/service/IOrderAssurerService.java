package org.suhui.modules.order.service;

import org.suhui.modules.order.entity.OrderAssurerAccount;
import org.suhui.modules.order.entity.OrderAssurer;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 去
 * @Author: jeecg-boot
 * @Date:   2019-12-29
 * @Version: V1.0
 */
public interface IOrderAssurerService extends IService<OrderAssurer> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(OrderAssurer orderAssurer,List<OrderAssurerAccount> orderAssurerAccountList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(OrderAssurer orderAssurer,List<OrderAssurerAccount> orderAssurerAccountList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
