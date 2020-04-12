package org.suhui.modules.toB.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.suhui.modules.toB.entity.PayCurrencyRate;
import org.suhui.modules.toB.entity.PayIdentityChannelAccount;

import java.util.List;
import java.util.Map;

/**
 * 类说明：用户支付渠道
 *
 * @author: 蔡珊珊
 * @create: 2020-04-12 19:56
 **/
@Repository
public interface PayIdentityChannelAccountMapper extends BaseMapper<PayIdentityChannelAccount> {

    public List<Map<String,String>> getChannelAccountInfoByUserNo(Map<String,String> map);

}
