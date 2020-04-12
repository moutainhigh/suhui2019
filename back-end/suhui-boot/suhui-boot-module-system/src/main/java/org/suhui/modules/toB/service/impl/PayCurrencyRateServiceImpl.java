package org.suhui.modules.toB.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.suhui.common.api.vo.Result;
import org.suhui.modules.toB.entity.PayCurrencyRate;
import org.suhui.modules.toB.mapper.PayCurrencyRateMapper;
import org.suhui.modules.toB.service.IPayCurrencyRateService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * 类说明：汇率记录表
 *
 * @author: 蔡珊珊
 * @create: 2020-04-12 15:21
 **/
@Service
public class PayCurrencyRateServiceImpl extends ServiceImpl<PayCurrencyRateMapper, PayCurrencyRate> implements IPayCurrencyRateService {

    @Autowired
    private PayCurrencyRateMapper payCurrencyRateMapper;

    /**
     * 获取当前汇率（根据源货币和目标货币）
     *
     * @param
     * @return
     */
    @Override
    public Result<JSONObject> getCurrencyRateValue(Map map) {
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        //1.获取汇率
        Map<String, Object> mapdb = payCurrencyRateMapper.getCurrencyRateValue(map);

        if (mapdb == null) {
            obj.put("message", "this rate is not in database");
            result.setResult(obj);
            result.setCode(515);
            return result;
        }

        int decimailnum = 4;
        String rate_nowStr = String.valueOf(mapdb.get("rate_now"));
        BigDecimal rate_nowDouble = new BigDecimal(rate_nowStr);
        BigDecimal bi2 = new BigDecimal("1000000000");
        BigDecimal rate_now_divide = rate_nowDouble.divide(bi2, 9, RoundingMode.HALF_UP);

        double rate_now_dou = rate_now_divide.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (rate_now_dou > 0.1) {
            decimailnum = 4;
        } else if (rate_now_dou * 10 > 0.1) {
            decimailnum = 5;
        } else if (rate_now_dou * 100 > 0.1) {
            decimailnum = 6;
        } else if (rate_now_dou * 1000 > 0.1) {
            decimailnum = 7;
        } else {
            decimailnum = 8;
        }
        rate_now_dou = rate_now_divide.setScale(decimailnum, BigDecimal.ROUND_HALF_UP).doubleValue();
        mapdb.put("rate_now", rate_now_dou);
        obj.put("data", mapdb);

        result.setResult(obj);
        return result;
    }

}
