package org.suhui.modules.suhui.suhui.service;

import org.suhui.modules.suhui.suhui.entity.PayIdentityChannelAccount;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 实体账号表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface IPayIdentityChannelAccountService extends IService<PayIdentityChannelAccount> {

    /**
     * 获取支付通道列表
     * @param map
     * @return
     */
    public List<Map<String,String>> getChannelAccountInfoByUserNo(Map<String,String> map ) ;
}
