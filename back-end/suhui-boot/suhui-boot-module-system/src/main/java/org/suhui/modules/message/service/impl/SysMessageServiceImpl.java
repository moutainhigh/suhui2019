package org.suhui.modules.message.service.impl;

import org.suhui.common.system.base.service.impl.JeecgServiceImpl;
import org.suhui.modules.message.entity.SysMessage;
import org.suhui.modules.message.mapper.SysMessageMapper;
import org.suhui.modules.message.service.ISysMessageService;
import org.springframework.stereotype.Service;

/**
 * @Description: 消息
 * @Author: jeecg-boot
 * @Date:  2019-04-09
 * @Version: V1.0
 */
@Service
public class SysMessageServiceImpl extends JeecgServiceImpl<SysMessageMapper, SysMessage> implements ISysMessageService {

}
