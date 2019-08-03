package org.suhui.modules.suhui.suhui.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.suhui.modules.suhui.suhui.entity.PayUserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.suhui.modules.suhui.suhui.entity.PayUserLogin;

/**
 * @Description: 用户信息表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
public interface PayUserInfoMapper extends BaseMapper<PayUserInfo> {

    public PayUserInfo getUserByObj(PayUserInfo payUserInfo);
}
