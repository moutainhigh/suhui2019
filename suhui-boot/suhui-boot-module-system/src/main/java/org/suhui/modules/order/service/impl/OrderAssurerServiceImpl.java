package org.suhui.modules.order.service.impl;

import org.suhui.modules.order.entity.OrderAssurer;
import org.suhui.modules.order.entity.OrderAssurerAccount;
import org.suhui.modules.order.entity.OrderMain;
import org.suhui.modules.order.mapper.OrderAssurerAccountMapper;
import org.suhui.modules.order.mapper.OrderAssurerMapper;
import org.suhui.modules.order.service.IOrderAssurerService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.suhui.modules.utils.BaseUtil;

import java.io.Serializable;
import java.util.*;

/**
 * @Description: 去
 * @Author: jeecg-boot
 * @Date: 2019-12-29
 * @Version: V1.0
 */
@Service
public class OrderAssurerServiceImpl extends ServiceImpl<OrderAssurerMapper, OrderAssurer> implements IOrderAssurerService {

    @Autowired
    private OrderAssurerMapper orderAssurerMapper;
    @Autowired
    private OrderAssurerAccountMapper orderAssurerAccountMapper;

    @Autowired
    OrderAssurerAccountServiceImpl orderAssurerAccountService;

    @Override
    @Transactional
    public void saveMain(OrderAssurer orderAssurer, List<OrderAssurerAccount> orderAssurerAccountList) {
        orderAssurerMapper.insert(orderAssurer);
        for (OrderAssurerAccount entity : orderAssurerAccountList) {
            //外键设置
            entity.setAssurerId(orderAssurer.getId());
            orderAssurerAccountMapper.insert(entity);
        }
    }

    @Override
    @Transactional
    public void updateMain(OrderAssurer orderAssurer, List<OrderAssurerAccount> orderAssurerAccountList) {
        orderAssurerMapper.updateById(orderAssurer);

        //1.先删除子表数据
        orderAssurerAccountMapper.deleteByMainId(orderAssurer.getId());

        //2.子表数据重新插入
        for (OrderAssurerAccount entity : orderAssurerAccountList) {
            //外键设置
            entity.setAssurerId(orderAssurer.getId());
            orderAssurerAccountMapper.insert(entity);
        }
    }

    @Override
    @Transactional
    public void delMain(String id) {
        orderAssurerAccountMapper.deleteByMainId(id);
        orderAssurerMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            orderAssurerAccountMapper.deleteByMainId(id.toString());
            orderAssurerMapper.deleteById(id);
        }
    }

    /**
     * 为订单获取最优承兑商
     */
    @Override
    public Map<String, Object> getAssurerByOrder(OrderMain orderMain) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("order_money", orderMain.getTargetCurrencyMoney());
        // 选择一个合适的承兑商
        List<OrderAssurer> orderAssurers = orderAssurerMapper.getAssurerByOrderData(paramMap);
        if (!BaseUtil.Base_HasValue(orderAssurers)) {
            resultMap.put("state","error");
            resultMap.put("message","未找到合适的承兑商");
            return resultMap;
        }
        OrderAssurer orderAssurer = null;
        OrderAssurerAccount orderAssurerAccount = null;
        for (int i = 0; i < orderAssurers.size(); i++) {
            OrderAssurer assurer = orderAssurers.get(i);
            // 为承兑商选择一个支付账号,同时排除掉没有账号得承兑商和账号支付宝金额不足的承兑商
            OrderAssurerAccount account = orderAssurerAccountService.getAssurerAccountByOrderPay(assurer.getId(), orderMain.getTargetCurrencyMoney());
            if (BaseUtil.Base_HasValue(account)) {
                orderAssurer = assurer;
                orderAssurerAccount = account;
                break;
            }
        }
        // 如果没找到账号,说明没有承兑商合适
        if (!BaseUtil.Base_HasValue(orderAssurerAccount)) {
            resultMap.put("state","error");
            resultMap.put("message","未找到合适的承兑商账户");
            return resultMap;
        }
        // 为承兑商选择一个收款账户
        OrderAssurerAccount orderAssurerAccountCollection = orderAssurerAccountService.getAssurerAccountByOrderCollection(orderAssurer.getId());
        resultMap.put("state","success");
        resultMap.put("orderAssurer", orderAssurer);
        resultMap.put("orderAssurerAccountPay", orderAssurerAccount);
        resultMap.put("orderAssurerAccountCollection", orderAssurerAccountCollection);
        return resultMap;
    }


    /**
     * 为承兑商排序，目前默认以费率排序，后期可加入其它排序规则
     */
    public List<OrderAssurer> orderAssurersList(List<OrderAssurer> orderAssurers) {
        Collections.sort(orderAssurers, new Comparator<OrderAssurer>() {
            @Override
            public int compare(OrderAssurer a, OrderAssurer b) {
                if (a.getAssurerRate() > b.getAssurerRate())
                    return -1;
                else
                    return 1;
            }
        });
        return orderAssurers;
    }

}
