package org.suhui.modules.suhui.suhui.service;

import org.suhui.modules.suhui.suhui.entity.CashierFreezeOrderDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: 冻结详情表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface ICashierFreezeOrderDetailService extends IService<CashierFreezeOrderDetail> {
    public Map<String,String> getCashierFreezeOrderByFreezeNo(Map<String,String> map);
}
