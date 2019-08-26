package org.suhui.modules.suhui.suhui.service;

import org.suhui.modules.suhui.suhui.entity.BizFreezeOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: 冻结记录表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface IBizFreezeOrderService extends IService<BizFreezeOrder> {

    public Map<String,String> getFreezeOrderByTradeNo(Map<String,String> map);
}
