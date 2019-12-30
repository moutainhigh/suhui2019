package org.suhui.modules.order.service.impl;

import org.suhui.modules.order.entity.OrderAssurer;
import org.suhui.modules.order.entity.OrderAssurerAccount;
import org.suhui.modules.order.mapper.OrderAssurerAccountMapper;
import org.suhui.modules.order.service.IOrderAssurerAccountService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.suhui.modules.utils.BaseUtil;

/**
 * @Description: 客户明细
 * @Author: jeecg-boot
 * @Date: 2019-12-29
 * @Version: V1.0
 */
@Service
public class OrderAssurerAccountServiceImpl extends ServiceImpl<OrderAssurerAccountMapper, OrderAssurerAccount> implements IOrderAssurerAccountService {

    @Autowired
    private OrderAssurerAccountMapper orderAssurerAccountMapper;

    @Override
    public List<OrderAssurerAccount> selectByMainId(String mainId) {
        return orderAssurerAccountMapper.selectByMainId(mainId);
    }

    /**
     *为承兑商选择一个支付账户
     */
    @Override
    public OrderAssurerAccount getAssurerAccountByOrderPay(String assurerId, int orderMoney) {
        OrderAssurerAccount orderAssurerAccount = null;
        List<OrderAssurerAccount> list = orderAssurerAccountMapper.selectByMainId(assurerId);
        if (!BaseUtil.Base_HasValue(list)) {
            return null;
        }
        // 按当日可用额度排序
        list = orderAssurerAccounts(list);
        for (int i = 0; i < list.size(); i++) {
            OrderAssurerAccount account = list.get(i);
            // 如果是支付宝账户,并且当日可用金额足够的话,优先选择支付宝账户
            if ("alipay".equals(account.getAccountType()) && account.getPayCanUseLimit() > orderMoney) {
                orderAssurerAccount = account;
                break;
            } else if ("bank_card".equals(account.getAccountType())) {
                orderAssurerAccount = account;
                break;
            }
        }
        return orderAssurerAccount;
    }

    /**
     * 为承兑商选择一个收款账户-选择收款额度小的
     */
    @Override
    public OrderAssurerAccount getAssurerAccountByOrderCollection(String assurerId) {
        List<OrderAssurerAccount> list = orderAssurerAccountMapper.selectByMainId(assurerId);
        if (!BaseUtil.Base_HasValue(list)) {
            return null;
        }
        Collections.sort(list, new Comparator<OrderAssurerAccount>() {
            @Override
            public int compare(OrderAssurerAccount a, OrderAssurerAccount b) {
                if (a.getCollectionUsedLimit() > b.getCollectionUsedLimit())
                    return 1;
                else
                    return -1;
            }
        });
        return list.get(0);
    }

    /**
     * 为承兑商账户排序-按当日可用额度倒序排列
     */
    public List<OrderAssurerAccount> orderAssurerAccounts(List<OrderAssurerAccount> list) {
        Collections.sort(list, new Comparator<OrderAssurerAccount>() {
            @Override
            public int compare(OrderAssurerAccount a, OrderAssurerAccount b) {
                if (a.getPayCanUseLimit() > b.getPayCanUseLimit())
                    return -1;
                else
                    return 1;
            }
        });
        return list;
    }
}
