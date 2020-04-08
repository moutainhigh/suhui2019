package org.suhui.modules.toB.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.suhui.modules.toB.entity.PayUserInfo;

/**
 * 接口说明：
 *
 * @author: 蔡珊珊
 * @create: 2020-04-08 11:48
 */
public interface IPayUserInfoService extends IService<PayUserInfo> {

    public PayUserInfo getUserByObj(PayUserInfo payUserInfo);

    public PayUserInfo getUserByPhone(String username,String areacode);


}
