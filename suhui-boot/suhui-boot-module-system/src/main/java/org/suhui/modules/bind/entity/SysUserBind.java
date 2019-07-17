package org.suhui.modules.bind.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 用户绑定关系
 * @Author: jeecg-boot
 * @Date:   2019-07-15
 * @Version: V1.0
 */
@Data
@TableName("sys_user_bind")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_user_bind对象", description="用户绑定关系")
public class SysUserBind {
    
	/**主键*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键")
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
	private java.lang.String userId;
	/**第三方账号*/
	@Excel(name = "第三方账号", width = 15)
    @ApiModelProperty(value = "第三方账号")
	private java.lang.String targetId;
	/**第三方账号类型 0支付宝 1微信 2银行卡*/
	@Excel(name = "第三方账号类型 0支付宝 1微信 2银行卡", width = 15)
    @ApiModelProperty(value = "第三方账号类型 0支付宝 1微信 2银行卡")
	private java.lang.Integer targetType;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
	private java.util.Date updateTime;
}
