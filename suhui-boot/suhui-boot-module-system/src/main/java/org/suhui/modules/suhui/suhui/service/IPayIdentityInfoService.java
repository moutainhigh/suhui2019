package org.suhui.modules.suhui.suhui.service;

import org.suhui.modules.suhui.suhui.entity.PayIdentityInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: 用户身份表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface IPayIdentityInfoService extends IService<PayIdentityInfo> {

    /**
     * 根据identityno identitytype 获取身份信息。
     * @param map
     * @return
     */
    public Map<String,String> getIdentityInfoByIdentityNo(Map<String,String> map ) ;


    /**
     * 根据identityno identitytype 获取身份信息。
     * @param map
     * @return
     */
    public Map<String,String> getIdentityInfoByUserNo(Map<String,String> map ) ;

}
