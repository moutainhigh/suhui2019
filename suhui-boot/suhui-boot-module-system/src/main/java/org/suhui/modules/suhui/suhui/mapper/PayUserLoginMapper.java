package org.suhui.modules.suhui.suhui.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.suhui.modules.suhui.suhui.entity.PayUserLogin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: app用户登陆
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface PayUserLoginMapper extends BaseMapper<PayUserLogin> {

    /**
     * 通过用户账号查询用户信息
     * @param username
     * @return
     */
    public PayUserLogin getUserByPhone(@Param("phone") String phone , @Param("areacode") String areacode);
}
