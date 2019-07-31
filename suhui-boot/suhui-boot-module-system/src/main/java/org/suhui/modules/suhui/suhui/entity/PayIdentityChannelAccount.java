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
 * @Description: 实体账号表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("pay_identity_channel_account")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="pay_identity_channel_account对象", description="实体账号表")
public class PayIdentityChannelAccount {
    
	/**自增ID*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "自增ID")
	private Integer id;
	/**身份编号*/
	@Excel(name = "身份编号", width = 15)
    @ApiModelProperty(value = "身份编号")
	private Integer identityNo;
	/**身份类型 1-个人身份 2-企业身份*/
	@Excel(name = "身份类型 1-个人身份 2-企业身份", width = 15)
    @ApiModelProperty(value = "身份类型 1-个人身份 2-企业身份")
	private Integer identityType;
	/**交易类型 0-全部 1-支付 2-充值*/
	@Excel(name = "交易类型 0-全部 1-支付 2-充值", width = 15)
    @ApiModelProperty(value = "交易类型 0-全部 1-支付 2-充值")
	private Integer tradeType;
	/**支付渠道类型 1-支付宝 2-微信 3-招行 4-XX银行 */
	@Excel(name = "支付渠道类型 1-支付宝 2-微信 3-招行 4-XX银行 ", width = 15)
    @ApiModelProperty(value = "支付渠道类型 1-支付宝 2-微信 3-招行 4-XX银行 ")
	private Integer channelType;
	/**渠道账户名称(如用户在支付宝的真实姓名、银行卡真实姓名)*/
	@Excel(name = "渠道账户名称(如用户在支付宝的真实姓名、银行卡真实姓名)", width = 15)
    @ApiModelProperty(value = "渠道账户名称(如用户在支付宝的真实姓名、银行卡真实姓名)")
	private String channelAccountName;
	/**渠道账号如支付宝账户、银行卡账户 */
	@Excel(name = "渠道账号如支付宝账户、银行卡账户 ", width = 15)
    @ApiModelProperty(value = "渠道账号如支付宝账户、银行卡账户 ")
	private String channelAccountNo;
	/**关键信息json(如信用卡安全码、日期信息)*/
	@Excel(name = "关键信息json(如信用卡安全码、日期信息)", width = 15)
    @ApiModelProperty(value = "关键信息json(如信用卡安全码、日期信息)")
	private String keyMsg;
	/**状态 1-绑定中 99-已解绑 100-已绑定*/
	@Excel(name = "状态 1-绑定中 99-已解绑 100-已绑定", width = 15)
    @ApiModelProperty(value = "状态 1-绑定中 99-已解绑 100-已绑定")
	private Integer status;
	/**remark*/
	@Excel(name = "remark", width = 15)
    @ApiModelProperty(value = "remark")
	private String remark;
	/**绑定时间*/
    @ApiModelProperty(value = "绑定时间")
	private Date bindTime;
	/**解绑时间*/
    @ApiModelProperty(value = "解绑时间")
	private Date unbindTime;
	/**createTime*/
    @ApiModelProperty(value = "createTime")
	private Date createTime;
	/**updateTime*/
    @ApiModelProperty(value = "updateTime")
	private Date updateTime;
}
