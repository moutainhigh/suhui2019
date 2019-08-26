package org.suhui.modules.suhui.suhui.service;

import org.suhui.modules.suhui.suhui.entity.BizRechargeOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: 充值交易表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface IBizRechargeOrderService extends IService<BizRechargeOrder> {

    /**
     * 根据充值 流水号  获取充值记录
     * @param map
     * @return
     */
    public Map<String,String> getRechargeOrderByRechargeNo(Map<String,String> map ) ;

    /**
     * 根据充值 流水号  获取充值记录
     * @param map
     * @return
     */
    public BizRechargeOrder getRechargeOrderObjectByRechargeNo(Map<String,String> map ) ;
}
