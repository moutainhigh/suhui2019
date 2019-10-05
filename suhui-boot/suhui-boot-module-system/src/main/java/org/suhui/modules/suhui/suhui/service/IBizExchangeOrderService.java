package org.suhui.modules.suhui.suhui.service;

import org.suhui.modules.suhui.suhui.entity.BizExchangeOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: 换汇记录表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface IBizExchangeOrderService extends IService<BizExchangeOrder> {

    /**
     * 根据充值 流水号  获取充值记录
     * @param map
     * @return
     */
    public Map<String,String> getExchargeOrderByExchargeNo(Map<String,String> map ) ;
}
