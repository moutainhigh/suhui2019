package org.suhui.modules.suhui.suhui.service;

import org.suhui.modules.suhui.suhui.entity.PayCurrencyRate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 汇率记录表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface IPayCurrencyRateService extends IService<PayCurrencyRate> {

    public List<Map<String,String>> getCurrencyRateTypeList( ) ;

    Map<String,Object> getCurrencyRateValue(Map map) ;
}
