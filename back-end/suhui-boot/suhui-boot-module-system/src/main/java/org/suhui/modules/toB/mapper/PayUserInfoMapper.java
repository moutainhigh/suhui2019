package org.suhui.modules.toB.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.suhui.modules.toB.entity.PayUserInfo;

/**
 * 接口说明：
 *
 * @author: 蔡珊珊
 * @create: 2020-04-08 11:55
 */
@Repository
public interface PayUserInfoMapper extends BaseMapper<PayUserInfo> {

    public PayUserInfo getUserByObj(PayUserInfo payUserInfo);

    public PayUserInfo getUserByPhoneInfo(@Param("phone") String phone, @Param("areacode") String areacode);
}
