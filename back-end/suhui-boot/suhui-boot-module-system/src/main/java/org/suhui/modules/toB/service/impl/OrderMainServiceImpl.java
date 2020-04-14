package org.suhui.modules.toB.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.suhui.common.api.vo.Result;
import org.suhui.modules.toB.entity.OrderMain;
import org.suhui.modules.toB.entity.OrderPlatformAccount;
import org.suhui.modules.toB.mapper.OrderMainMapper;
import org.suhui.modules.toB.service.IOrderMainService;
import org.suhui.modules.utils.BaseUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.suhui.modules.toB.service.impl.ToCOrderMainServiceImpl.getOrderNoByUUID;

/**
 * 类说明：订单相关
 *
 * @author: 蔡珊珊
 * @create: 2020-04-14 13:59
 */
@Slf4j
@Service
public class OrderMainServiceImpl extends ServiceImpl<OrderMainMapper, OrderMain> implements IOrderMainService {
    @Autowired
    PayCurrencyRateServiceImpl payCurrencyRateServiceImpl;

    @Autowired
    OrderPlatformAccountServiceImpl orderPlatformAccountServiceImpl;

    /**
     * 创建订单主方法
     *
     * @param
     * @return
     */
    @Override
    @Transactional
    public Result<Object> add(OrderMain orderMain) {
        Result<Object> result = new Result<Object>();
        JSONObject obj = new JSONObject();
        // 判断必填项是否有值
        String checkValue = orderMain.checkCreateRequireValue();
        if (BaseUtil.Base_HasValue(checkValue)) {
            return Result.error(531, checkValue);
        }
        //1.转换金额为最小单位
        orderMain.changeMoneyToPoints();
        //2.通过源货币和目标货币获取汇率和计算金额
        JSONObject rateObj = this.getUserPayMoney(orderMain.getSourceCurrency(), orderMain.getTargetCurrency(), orderMain.getTargetCurrencyMoney().toString());
        if (!BaseUtil.Base_HasValue(rateObj)) {
            return Result.error(515, "获取汇率失败");
        }
        if (BaseUtil.Base_HasValue(rateObj)) {
            orderMain.setSourceCurrencyMoney(rateObj.getDouble("money"));
            orderMain.setExchangeRate(rateObj.getDouble("rate"));
        }
        //<editor-fold desc="3.根据区域代码，获取平台支付账号">
        Map keyMap = new HashMap();
        keyMap.put("CNY", "+86");
        keyMap.put("KRW", "+82");
        keyMap.put("USD", "+1");
        keyMap.put("PHP", "+63");
        keyMap.put("AUD", "+61");
        keyMap.put("JPY", "+81");
        keyMap.put("MYR", "+60");
        if (!BaseUtil.Base_HasValue(keyMap.get(orderMain.getTargetCurrency()))) {
            return null;
        }
        String areaCode = keyMap.get(orderMain.getTargetCurrency()).toString();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("areaCode", areaCode);
        List<OrderPlatformAccount> platformAccountList = orderPlatformAccountServiceImpl.getAccountListByAreaCode(paramMap);
        if (platformAccountList == null || platformAccountList.size() == 0) {
            return Result.error(532, "获取用户收款账户失败");
        }
        OrderPlatformAccount platformAccount = platformAccountList.get(0);
        //</editor-fold>

        orderMain.setOrderCode(getOrderNoByUUID());
        orderMain.setOrderState("2"); //订单状态(1 创建、2 待确认收款、3 已确认收款、0、已作废0)
        orderMain.setUserPayAccount(platformAccount.getAccountNo());
        orderMain.setUserPayAccountUser(platformAccount.getRealName());
        orderMain.setUserPayBank(platformAccount.getOpenBank());
        orderMain.setUserPayBankBranch(platformAccount.getOpenBankBranch());
        orderMain.setUserPayAreaCode(platformAccount.getAreaCode());

        JSONObject data = new JSONObject();
        try {
            //4.保存订单
            save(orderMain);

            //<editor-fold desc="接口返回信息">
            orderMain.changeMoneyToBig();
            JSONObject bank = new JSONObject();
            bank.put("account_no", orderMain.getUserPayAccount());
            bank.put("real_name", orderMain.getUserPayAccountUser());
            bank.put("open_bank", orderMain.getUserPayBank());
            bank.put("area_code", orderMain.getUserPayAreaCode());

            data.put("order_no", orderMain.getOrderCode());
            data.put("order_state", orderMain.getOrderState());
            data.put("user_no", orderMain.getUserNo());
            data.put("user_name", orderMain.getUserName());
            data.put("user_contact", orderMain.getUserContact());
            data.put("source_currency", orderMain.getSourceCurrency());
            data.put("target_currency", orderMain.getTargetCurrency());
            data.put("source_currency_money", orderMain.getSourceCurrencyMoney());
            data.put("target_currency_money", orderMain.getTargetCurrencyMoney());
            data.put("exchange_rate", orderMain.getExchangeRate());
            data.put("user_pay_method", orderMain.getUserPayMethod());
            data.put("bank_info", bank);
            //</editor-fold>
        } catch (Exception ex) {
            log.error("创建订单失败", ex);
            return Result.error(562, "订单创建失败");
        }
        result.setResult(data);
        result.success("订单创建成功");
        return result;
    }


    /**
     * 获取应支付金额（根据目标货币&金额、源货币）
     */
    public JSONObject getUserPayMoney(String source, String target, String money) {
        JSONObject valueObj = new JSONObject();
        try {
            //1.获取汇率
            Map<String, String> map = new HashMap<>();
            map.put("source_currency_code", source);
            map.put("target_currency_code", target);
            Result<JSONObject> rateResult = payCurrencyRateServiceImpl.getCurrencyRateValue(map);
            if (rateResult.getCode() == 0) {
                JSONObject result = rateResult.getResult();
                JSONObject data = result.getJSONObject("data");
                String rate = data.getString("rate_now");
                BigDecimal a1 = new BigDecimal(money);
                BigDecimal b1 = new BigDecimal(rate);
                Double value = a1.divide(b1, 0, BigDecimal.ROUND_UP).doubleValue();
                valueObj.put("money", value);
                valueObj.put("rate", rate);
            }
        } catch (Exception e) {
            return null;
        }
        return valueObj;
    }


}