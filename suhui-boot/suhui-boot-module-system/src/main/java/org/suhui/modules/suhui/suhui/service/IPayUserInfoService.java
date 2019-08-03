package org.suhui.modules.suhui.suhui.service;

import org.suhui.modules.suhui.suhui.entity.PayUserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.suhui.modules.suhui.suhui.entity.PayUserLogin;

/**
 * @Description: 用户信息表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface IPayUserInfoService extends IService<PayUserInfo> {

    public PayUserInfo getUserByObj(PayUserInfo payUserInfo);
}
