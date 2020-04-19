package org.suhui.modules.toB.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.suhui.modules.toB.entity.OrderAssurerAccount;
import org.suhui.modules.toB.mapper.OrderAssurerAccountMapper;
import org.suhui.modules.toB.service.IOrderAssurerAccountService;
import org.suhui.modules.utils.BaseUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 类说明：承兑商账户明细
 *
 * @author: 蔡珊珊
 * @create: 2020-04-12 21:01
 **/
@Service
public class OrderAssurerAccountServiceImpl extends ServiceImpl<OrderAssurerAccountMapper, OrderAssurerAccount> implements IOrderAssurerAccountService {

    @Autowired
    private OrderAssurerAccountMapper orderAssurerAccountMapper;

     /**
     * 为承兑商选择一个支付账户
     *
     * @param
     * @return
     */
    @Override
    public OrderAssurerAccount getAssurerAccountByOrderPay(String assurerId, Double orderMoney, String merchantCollectionMethod, String targetreaCode) {
        OrderAssurerAccount orderAssurerAccount = null;
        List<OrderAssurerAccount> list = orderAssurerAccountMapper.selectByMainId(assurerId, merchantCollectionMethod, targetreaCode);
        if (!BaseUtil.Base_HasValue(list)) {
            return null;
        }
        // 收款方式是支付宝
        if (merchantCollectionMethod.equals("alipay")) {
            // 按当日可用额度排序
//            list = orderAssurerAccounts(list);
            List<OrderAssurerAccount> useList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                OrderAssurerAccount account = list.get(i);
                //支付可用金额小于订单金额，不能接单
                if (account.getPayCanUseLimit() > orderMoney) {
                    useList.add(account);
                }
            }
            if (BaseUtil.Base_HasValue(useList)) {
                return null;
            }
            orderAssurerAccount = useList.get(BaseUtil.getRandomInt(0,useList.size()));
        } else if (merchantCollectionMethod.equals("bank_card")) {
            // 获取随机数
            int n = BaseUtil.getRandomInt(0, list.size());
            orderAssurerAccount = list.get(n);
        }
        return orderAssurerAccount;
    }

    /**
     * 为承兑商选择一个收款账户-选择收款额度小的
     *
     * @param
     * @return
     */
    @Override
    public OrderAssurerAccount getAssurerAccountByOrderCollection(String assurerId, String userPayMethod, String userPayAreaCode) {
        // 用户选择哪种支付方式,就提供承兑商哪种收款方式
        List<OrderAssurerAccount> list = orderAssurerAccountMapper.selectByMainId(assurerId, userPayMethod, userPayAreaCode);
        if (!BaseUtil.Base_HasValue(list)) {
            return null;
        }
        if(userPayMethod.equals("alipay")){
            int n = BaseUtil.getRandomInt(0, list.size());
            return list.get(n);
        }
        // 正序排列
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

}
