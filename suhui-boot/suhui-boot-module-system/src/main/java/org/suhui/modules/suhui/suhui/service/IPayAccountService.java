package org.suhui.modules.suhui.suhui.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.suhui.modules.suhui.suhui.entity.PayAccount;

import java.util.List;
import java.util.Map;

/**
 * @Description: 账户表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface IPayAccountService extends IService<PayAccount> {

    /**
     * 根据userNo userType 获取账户支付信息。
     * @param map
     * @return
     */
    public Map<String,String> getPayAccountByUserNo(Map<String,String> map ) ;

    /**
     * 根据userNo userType 获取账户资产信息。
     * @param map
     * @return
     */
    public List<Map<String,String>> getPayAccountMoneyByUserNo(Map<String,String> map ) ;

    /**
     * 根据userNo userType 获取资产账户信息信息。
     * @param map
     * @return
     */
    public Map<String,String> getPayAccountAssetByUserNo(Map<String,String> map ) ;

    /**
     * 根据userNo userType 获取 身份信息下 支付通道。
     * @param map
     * @return
     */
    public Map<String,String> getPayIdentityChannelAccountByUserNo(Map<String,String> map ) ;


}
