package org.suhui.modules.toB.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.suhui.common.api.vo.Result;
import org.suhui.modules.toB.entity.PayCurrencyRate;

import java.util.List;
import java.util.Map;

/**
 * 类说明：汇率记录表
 *
 * @author: 蔡珊珊
 * @create: 2020-04-12 15:19
 **/
public interface IPayCurrencyRateService extends IService<PayCurrencyRate> {
    //获取当前汇率（根据源货币和目标货币）
    Result<JSONObject> getCurrencyRateValue(Map map);

}

