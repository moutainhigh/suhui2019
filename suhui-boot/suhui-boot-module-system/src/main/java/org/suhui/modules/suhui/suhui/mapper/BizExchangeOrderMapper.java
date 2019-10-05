package org.suhui.modules.suhui.suhui.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.suhui.modules.suhui.suhui.entity.BizExchangeOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 换汇记录表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface BizExchangeOrderMapper extends BaseMapper<BizExchangeOrder> {

    public Map<String,String> getExchargeOrderByExchargeNo(Map<String,String> map);

}
