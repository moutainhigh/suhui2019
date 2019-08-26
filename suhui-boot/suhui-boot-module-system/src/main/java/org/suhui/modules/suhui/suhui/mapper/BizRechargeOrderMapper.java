package org.suhui.modules.suhui.suhui.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.suhui.modules.suhui.suhui.entity.BizRechargeOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 充值交易表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface BizRechargeOrderMapper extends BaseMapper<BizRechargeOrder> {

    public Map<String,String> getRechargeOrderByRechargeNo(Map<String,String> map);

    public BizRechargeOrder getRechargeOrderObjectByRechargeNo(Map<String,String> map);
}
