package org.suhui.modules.suhui.suhui.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.suhui.modules.suhui.suhui.entity.CashierFreezeOrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 冻结详情表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface CashierFreezeOrderDetailMapper extends BaseMapper<CashierFreezeOrderDetail> {
    public Map<String,String> getCashierFreezeOrderByFreezeNo(Map<String,String> map);
}
