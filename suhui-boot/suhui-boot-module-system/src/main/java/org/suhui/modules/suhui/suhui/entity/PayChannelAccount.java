package org.suhui.modules.suhui.suhui.entity;

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
 * @Description: 支付渠道账户表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("pay_channel_account")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="pay_channel_account对象", description="支付渠道账户表")
public class PayChannelAccount {
    
	/**自增ID*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "自增ID")
	private Integer id;
	/**渠道账号编号*/
	@Excel(name = "渠道账号编号", width = 15)
    @ApiModelProperty(value = "渠道账号编号")
	private String channelAccountNo;
	/**渠道类型 1-支付宝 2-微信 3-招行 4-XX银行*/
	@Excel(name = "渠道类型 1-支付宝 2-微信 3-招行 4-XX银行", width = 15)
    @ApiModelProperty(value = "渠道类型 1-支付宝 2-微信 3-招行 4-XX银行")
	private Integer channelType;
	/**渠道商户号*/
	@Excel(name = "渠道商户号", width = 15)
    @ApiModelProperty(value = "渠道商户号")
	private String channelAccount;
	/**支付渠道登录账号*/
	@Excel(name = "支付渠道登录账号", width = 15)
    @ApiModelProperty(value = "支付渠道登录账号")
	private String channelLoginAccount;
	/**渠道账户配置信息json*/
	@Excel(name = "渠道账户配置信息json", width = 15)
    @ApiModelProperty(value = "渠道账户配置信息json")
	private String channelConfig;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;
	/**是否启用(0-停用 1-启用)*/
	@Excel(name = "是否启用(0-停用 1-启用)", width = 15)
    @ApiModelProperty(value = "是否启用(0-停用 1-启用)")
	private Integer status;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**更新时间*/
    @ApiModelProperty(value = "更新时间")
	private Date updateTime;
}
