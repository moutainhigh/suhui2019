package org.suhui.modules.toB.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.suhui.modules.toB.entity.OrderAssurerMoneyChange;

import java.util.List;

/**
 * 类说明：承兑商金额变动
 *
 * @author: 蔡珊珊
 * @create: 2020-04-08 22:06
 **/
public interface IOrderAssurerMoneyChangeService extends IService<OrderAssurerMoneyChange> {
    //列表
    List<OrderAssurerMoneyChange> getInitData();
}

