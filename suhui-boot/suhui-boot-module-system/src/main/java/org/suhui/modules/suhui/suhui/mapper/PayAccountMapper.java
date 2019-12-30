package org.suhui.modules.suhui.suhui.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.suhui.modules.suhui.suhui.entity.PayAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.suhui.modules.suhui.suhui.entity.PayUserLogin;

/**
 * @Description: 账户表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface PayAccountMapper extends BaseMapper<PayAccount> {

    public Map<String,String> getPayAccountByUserNo(Map<String,String> map);

    public List<Map<String,String>> getPayAccountMoneyByUserNo(Map<String,String> map);

    public Map<String,String> getPayAccountAssetByUserNo(Map<String,String> map);

    public Map<String,String> getPayIdentityChannelAccountByUserNo(Map<String,String> map);

}

