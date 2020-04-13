package org.suhui.modules.toB.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.suhui.modules.toB.entity.PayCurrencyRate;

import java.util.List;
import java.util.Map;

/**
 * 类说明：汇率记录表
 *
 * @author: 蔡珊珊
 * @create: 2020-04-12 15:13
 **/
@Repository
public interface PayCurrencyRateMapper extends BaseMapper<PayCurrencyRate> {

    //获取当前汇率（根据源货币和目标货币）
    public Map<String, Object> getCurrencyRateValue(Map map);
}
