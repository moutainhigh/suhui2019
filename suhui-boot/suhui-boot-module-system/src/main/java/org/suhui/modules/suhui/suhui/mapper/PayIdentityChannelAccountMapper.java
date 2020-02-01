package org.suhui.modules.suhui.suhui.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.suhui.modules.suhui.suhui.entity.PayIdentityChannelAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 实体账号表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface PayIdentityChannelAccountMapper extends BaseMapper<PayIdentityChannelAccount> {

    public List<Map<String,String>> getChannelAccountInfoByUserNo(Map<String,String> map);

}
