package org.suhui.modules.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.suhui.common.api.vo.Result;
import org.suhui.modules.order.entity.OrderAssurer;
import org.suhui.modules.order.entity.OrderAssurerAccount;
import org.suhui.modules.order.entity.OrderMain;
import org.suhui.modules.order.mapper.OrderMainMapper;
import org.suhui.modules.order.service.IOrderMainService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.suhui.modules.utils.BaseUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Autowired
    OrderAssurerAccountServiceImpl orderAssurerAccountService;


    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final AtomicInteger atomicInteger = new AtomicInteger(1000000);

    /**
     * 创建订单主方法
     */
    @Override
    @Transactional
    public Result<Object> manageOrderByAuto(OrderMain orderMain) {
        Result<Object> result = new Result<Object>();
        // 判断必填项是否有值
        String checkValue = orderMain.checkCreateRequireValue();
        if (BaseUtil.Base_HasValue(checkValue)) {
            return Result.error(511, checkValue);
        }
        // 为订单选择最优承兑商
        Map resutMap = orderAssurerService.getAssurerByOrder(orderMain);
        if (BaseUtil.Base_HasValue(resutMap) && resutMap.get("state").equals("success")) {
            dispatchAssurerToOrder(orderMain, resutMap);
        } else {
            dispatchAssurerToOrder(orderMain, resutMap);
        }
        result.success("订单创建成功");
        return result;
    }

    /**
     * 给订单分配一个承兑商和账户
     */
    public void dispatchAssurerToOrder(OrderMain orderMain, Map resutMap) {
        OrderAssurer orderAssurer = (OrderAssurer) resutMap.get("orderAssurer");
        OrderAssurerAccount pay = (OrderAssurerAccount) resutMap.get("orderAssurerAccountPay");
        OrderAssurerAccount collection = (OrderAssurerAccount) resutMap.get("orderAssurerAccountCollection");
        if (BaseUtil.Base_HasValue(orderAssurer) && BaseUtil.Base_HasValue(pay) && BaseUtil.Base_HasValue(collection)) {
            orderMain.setAssurerId(orderAssurer.getId());
            orderMain.setAssurerName(orderAssurer.getAssurerName());
            orderMain.setAssurerCollectionAccount(collection.getAccountNo());
            orderMain.setAssurerCollectionMethod(collection.getAccountType());
            orderMain.setAssurerPayAccount(pay.getAccountNo());
            orderMain.setAssurerPayMethod(pay.getAccountType());
            orderMain.setOrderState(2);
            orderMain.setAutoDispatchState(1);
            // 锁定承兑商金额
            lockAssurerMoney(orderMain.getTargetCurrencyMoney(), orderAssurer);
            // 锁定承兑账户金额
            lockAssurerAccountMoney(orderMain.getTargetCurrencyMoney(), pay);
        } else {
            orderMain.setOrderState(1);
            orderMain.setAutoDispatchState(0);
            orderMain.setAutoDispatchText(resutMap.get("message").toString());
        }
        if (!BaseUtil.Base_HasValue(orderMain.getId())) {
            orderMain.setOrderCode(getOrderNoByUUID());
            save(orderMain);
        } else {
            updateById(orderMain);
        }
    }


    public static synchronized String getOrderNoByUUID() {
        Integer uuidHashCode = UUID.randomUUID().toString().hashCode();
        if (uuidHashCode < 0) {
            uuidHashCode = uuidHashCode * (-1);
        }
        String date = simpleDateFormat.format(new Date());
        return date + uuidHashCode;
    }

    /**
     * 锁定承运商支付金额
     */
    public void lockAssurerMoney(int money, OrderAssurer orderAssurer) {
        int lockMoney = orderAssurer.getPayLockMoney() + money;
        int canUseLimit = orderAssurer.getCanUseLimit() - money;
        orderAssurer.setCanUseLimit(canUseLimit);
        orderAssurer.setPayLockMoney(lockMoney);
        orderAssurerService.updateById(orderAssurer);
    }

    /**
     * 锁定承运商账号支付金额
     */
    public void lockAssurerAccountMoney(int money, OrderAssurerAccount orderAssurerAccount) {
        if (orderAssurerAccount.getAccountType().equals("alipay")) {
            int lockMoney = orderAssurerAccount.getPayLockMoney() + money;
            int canUseLimit = orderAssurerAccount.getPayCanUseLimit() - money;
            orderAssurerAccount.setPayCanUseLimit(canUseLimit);
            orderAssurerAccount.setPayLockMoney(lockMoney);
            orderAssurerAccountService.updateById(orderAssurerAccount);
        }
    }

    /**
     * 通过源货币和目标货币获取汇率计算金额
     */
    @Override
    public JSONObject getUserPayMoney(String source, String target, String money, String token) {
        JSONObject valueObj=new JSONObject();
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
                JSONObject data = result.getJSONObject("data");
                String rate = data.getString("rate_now");
                BigDecimal a1 = new BigDecimal(money);
                BigDecimal b1 = new BigDecimal(rate);
                BigDecimal rate_now_divide = a1.multiply(b1);
                Double value = rate_now_divide.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                valueObj.put("money",value);
                valueObj.put("rate",rate);
            }
        } catch (Exception e) {
            return null;
        }
        return valueObj;
    }

}
