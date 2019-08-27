package org.suhui.modules.suhui.suhui.service;

import org.suhui.modules.suhui.suhui.entity.BizWithDrawOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: 提现订单表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface IBizWithDrawOrderService extends IService<BizWithDrawOrder> {

    /**
     * 根据充值 流水号  获取充值记录
     * @param map
     * @return
     */
    public Map<String,String> getWithDrawOrderByWithDrawNo(Map<String,String> map ) ;
}
