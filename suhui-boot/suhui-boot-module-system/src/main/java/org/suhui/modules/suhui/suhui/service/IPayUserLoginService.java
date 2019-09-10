package org.suhui.modules.suhui.suhui.service;

import org.suhui.modules.suhui.suhui.entity.PayUserLogin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: app用户登陆
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface IPayUserLoginService extends IService<PayUserLogin> {

    public PayUserLogin getUserByPhone(String username,String areacode);
}
