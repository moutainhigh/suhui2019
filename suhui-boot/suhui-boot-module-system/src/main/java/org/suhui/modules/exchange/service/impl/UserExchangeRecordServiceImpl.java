package org.suhui.modules.exchange.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.suhui.modules.exchange.entity.UserExchangeRecord;
import org.suhui.modules.exchange.mapper.UserExchangeRecordMapper;
import org.suhui.modules.exchange.service.IUserExchangeRecordService;
import org.suhui.modules.system.entity.SysUser;
import org.suhui.modules.system.mapper.SysUserMapper;

/**
 * @Description: 用户的换汇记录表
 * @Author: jeecg-boot
 * @Date:   2019-07-18
 * @Version: V1.0
 */
@Service
public class UserExchangeRecordServiceImpl extends ServiceImpl<UserExchangeRecordMapper, UserExchangeRecord> implements IUserExchangeRecordService {

    @Autowired
    private SysUserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    public void saveRecord(UserExchangeRecord userExchangeRecord, SysUser sysUser){

        userMapper.updateById(sysUser);
        save(userExchangeRecord);
    }


}

