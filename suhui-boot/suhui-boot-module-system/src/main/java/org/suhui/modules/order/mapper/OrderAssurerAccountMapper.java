package org.suhui.modules.order.mapper;

import java.util.List;
import org.suhui.modules.order.entity.OrderAssurerAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
/**
 * @Description: 客户明细
 * @Author: jeecg-boot
 * @Date:   2019-12-29
 * @Version: V1.0
 */
public interface OrderAssurerAccountMapper extends BaseMapper<OrderAssurerAccount> {

	public boolean deleteByMainId(String mainId);
    
	public List<OrderAssurerAccount> selectByMainId(@Param("mainId") String mainId,@Param("accountType") String accountType);

	List<OrderAssurerAccount> selectByAssurerIdToOrder(String assurerId);
}
