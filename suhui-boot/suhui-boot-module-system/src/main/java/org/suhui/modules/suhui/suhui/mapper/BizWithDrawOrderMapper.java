package org.suhui.modules.suhui.suhui.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.suhui.modules.suhui.suhui.entity.BizWithDrawOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 提现订单表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface BizWithDrawOrderMapper extends BaseMapper<BizWithDrawOrder> {

    public Map<String,String> getWithDrawOrderByWithDrawNo(Map<String,String> map);

}
