package org.suhui.modules.suhui.suhui.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.suhui.modules.suhui.suhui.entity.BizAssetChangeRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 账户资金变更聚合流水表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface BizAssetChangeRecordMapper extends BaseMapper<BizAssetChangeRecord> {

    public Map<String,String> getAssetChangeRecordByRechargeNo(Map<String,String> map);
}
