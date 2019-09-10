package org.suhui.modules.suhui.suhui.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.suhui.modules.suhui.suhui.entity.PayCurrencyRate;
import org.suhui.modules.suhui.suhui.mapper.PayAccountMapper;
import org.suhui.modules.suhui.suhui.mapper.PayCurrencyRateMapper;
import org.suhui.modules.suhui.suhui.service.IPayCurrencyRateService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 汇率记录表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Service
public class PayCurrencyRateServiceImpl extends ServiceImpl<PayCurrencyRateMapper, PayCurrencyRate> implements IPayCurrencyRateService {

    @Autowired
    private PayCurrencyRateMapper payCurrencyRateMapper ;

    @Override
    public List<Map<String, String>> getCurrencyRateTypeList() {
        return payCurrencyRateMapper.getCurrencyRateTypeList();
    }

    @Override
    public Map<String, String> getCurrencyRateValue(Map map) {
        return payCurrencyRateMapper.getCurrencyRateValue();
    }

}
