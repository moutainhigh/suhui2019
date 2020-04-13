package org.suhui.modules.toB.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.suhui.modules.toB.entity.PayIdentityChannelAccount;

import java.util.List;
import java.util.Map;

/**
 * 类说明：用户支付渠道
 *
 * @author: 蔡珊珊
 * @create: 2020-04-12 18:56
 **/
public interface IPayIdentityChannelAccountService extends IService<PayIdentityChannelAccount> {
    //获取支付通道列表
    public List<Map<String, String>> getChannelAccountInfoByUserNo(Map<String, String> map);
}
