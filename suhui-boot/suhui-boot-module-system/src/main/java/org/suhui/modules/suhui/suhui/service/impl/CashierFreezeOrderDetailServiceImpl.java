package org.suhui.modules.suhui.suhui.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.suhui.modules.suhui.suhui.entity.CashierFreezeOrderDetail;
import org.suhui.modules.suhui.suhui.mapper.BizFreezeOrderMapper;
import org.suhui.modules.suhui.suhui.mapper.CashierFreezeOrderDetailMapper;
import org.suhui.modules.suhui.suhui.service.ICashierFreezeOrderDetailService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 冻结详情表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Service
public class CashierFreezeOrderDetailServiceImpl extends ServiceImpl<CashierFreezeOrderDetailMapper, CashierFreezeOrderDetail> implements ICashierFreezeOrderDetailService {


    @Autowired
    private CashierFreezeOrderDetailMapper cashierFreezeOrderDetailMapper ;

    @Override
    public Map<String,String> getCashierFreezeOrderByFreezeNo(Map<String,String> map ) {
        return cashierFreezeOrderDetailMapper.getCashierFreezeOrderByFreezeNo(map);
    }
}
