package org.suhui.modules.exchange.service;

import org.suhui.modules.exchange.entity.UserExchangeRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import org.suhui.modules.system.entity.SysUser;

/**
 * @Description: 用户的换汇记录表
 * @Author: org-boot
 * @Date:   2019-07-18
 * @Version: V1.0
 */
public interface IUserExchangeRecordService extends IService<UserExchangeRecord> {

    void saveRecord(UserExchangeRecord userExchangeRecord, SysUser sysUser);
}
