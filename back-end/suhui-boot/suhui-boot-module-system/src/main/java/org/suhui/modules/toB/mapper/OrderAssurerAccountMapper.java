package org.suhui.modules.toB.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.suhui.modules.toB.entity.OrderAssurerAccount;

import java.util.List;

/**
 * 类说明：承兑商账户明细
 *
 * @author: 蔡珊珊
 * @create: 2020-04-12 21:03
 **/
@Repository
public interface OrderAssurerAccountMapper extends BaseMapper<OrderAssurerAccount> {

    boolean deleteByMainId(String mainId);

    List<OrderAssurerAccount> selectByMainId(@Param("mainId") String mainId, @Param("accountType") String accountType, @Param("areaCode") String areaCode);

    List<OrderAssurerAccount> selectByAssurerIdToOrder(String assurerId);
}
