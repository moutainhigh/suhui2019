package org.suhui.modules.suhui.suhui.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.suhui.modules.suhui.suhui.entity.BizAssetChangeRecord;
import org.suhui.modules.suhui.suhui.mapper.BizAssetChangeRecordMapper;
import org.suhui.modules.suhui.suhui.mapper.BizFreezeOrderMapper;
import org.suhui.modules.suhui.suhui.service.IBizAssetChangeRecordService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: 账户资金变更聚合流水表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Service
public class BizAssetChangeRecordServiceImpl extends ServiceImpl<BizAssetChangeRecordMapper, BizAssetChangeRecord> implements IBizAssetChangeRecordService {

    @Autowired
    private BizAssetChangeRecordMapper bizAssetChangeRecordMapper ;

    @Override
    public Map<String,String> getAssetChangeRecordByRechargeNo(Map<String,String> map ) {
        return bizAssetChangeRecordMapper.getAssetChangeRecordByRechargeNo(map);
    }
}
