package org.suhui.modules.toB.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.suhui.modules.toB.entity.PayUserLogin;

/**
 * 接口说明：
 *
 * @author: 蔡珊珊
 * @create: 2020-04-08 11:48
 */
public interface IPayUserLoginService extends IService<PayUserLogin> {

    public PayUserLogin getUserByPhone(String username,String areacode);

    public PayUserLogin getUserByPhoneAndUserType(String username,String areacode,Integer usertype);
}
