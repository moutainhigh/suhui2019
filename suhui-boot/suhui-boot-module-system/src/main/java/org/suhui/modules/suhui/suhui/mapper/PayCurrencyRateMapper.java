package org.suhui.modules.suhui.suhui.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.suhui.modules.suhui.suhui.entity.PayCurrencyRate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 汇率记录表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface PayCurrencyRateMapper extends BaseMapper<PayCurrencyRate> {

    public List<Map<String,String>> getCurrencyRateTypeList();

    public Map<String,String> getCurrencyRateValue(Map map) ;
}
