package org.suhui.modules.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.suhui.common.api.vo.Result;
import org.suhui.modules.order.entity.OrderAssurer;
import org.suhui.modules.order.entity.OrderMain;
import org.suhui.modules.order.mapper.OrderMainMapper;
import org.suhui.modules.order.service.IOrderMainService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.suhui.modules.utils.BaseUtil;

import java.util.Map;

/**
 * @Description: 订单表
 * @Author: jeecg-boot
 * @Date: 2019-12-29
 * @Version: V1.0
 */
@Service
public class OrderMainServiceImpl extends ServiceImpl<OrderMainMapper, OrderMain> implements IOrderMainService {

    @Autowired
    OrderAssurerServiceImpl orderAssurerService;

    /**
     * 创建订单主方法
     */
    @Override
    public Result<Object> manageOrder(OrderMain orderMain,String token) {
        Result<Object> result = new Result<Object>();
        // 判断必填项是否有值
        String checkValue = orderMain.checkCreateRequireValue();
        if (BaseUtil.Base_HasValue(checkValue)) {
            return Result.error(511, checkValue);
        }
        // 自动分配承兑商
        Map orderAssurer = orderAssurerService.getAssurerByOrder(orderMain);
        System.out.println("------" + orderAssurer);
        result.success("订单创建成功");
        return result;
    }



    /**
     * 通过源货币和目标货币获取当前费率
     */
    public JSONObject getRate(String source, String target,String token) {
        JSONObject data = new JSONObject();
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:3333/api/login/payCurrencyRate/getCurrencyRateByRateCode";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("X-Access-Token", token);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("source_currency_code", source);
            map.add("target_currency_code", target);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, request, JSONObject.class);
            if (response.getBody().getInteger("code") == 0) {
                JSONObject result = response.getBody().getJSONObject("result");
                data = result.getJSONObject("data");
            }
        } catch (Exception e) {
            return null;
        }
        return data;
    }

}
