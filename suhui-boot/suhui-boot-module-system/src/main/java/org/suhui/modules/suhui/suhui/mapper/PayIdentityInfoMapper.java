package org.suhui.modules.suhui.suhui.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.suhui.modules.suhui.suhui.entity.PayIdentityInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 用户身份表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface PayIdentityInfoMapper extends BaseMapper<PayIdentityInfo> {

    public Map<String,String> getIdentityInfoByIdentityNo(Map<String,String> map);

    public Map<String,String> getIdentityInfoByUserNo(Map<String,String> map);



}
