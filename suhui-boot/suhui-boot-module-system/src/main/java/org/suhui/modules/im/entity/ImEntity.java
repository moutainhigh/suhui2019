package org.suhui.modules.im.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("im_message")
public class ImEntity {
    private int id;
    private String senduser;
    private String receiveuser;
    private String msg;
    private String createtime;
}
