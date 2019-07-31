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
 * @Description: 充值交易表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("biz_recharge_order")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="biz_recharge_order对象", description="充值交易表")
public class BizRechargeOrder {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**业务交易流水号(各业务保持唯一)*/
	@Excel(name = "业务交易流水号(各业务保持唯一)", width = 15)
    @ApiModelProperty(value = "业务交易流水号(各业务保持唯一)")
	private String tradeNo;
	/**由支付系统生成的唯一流水号*/
	@Excel(name = "由支付系统生成的唯一流水号", width = 15)
    @ApiModelProperty(value = "由支付系统生成的唯一流水号")
	private String bizRechargeNo;
	/**第三方支付平台订单号，回调返回*/
	@Excel(name = "第三方支付平台订单号，回调返回", width = 15)
    @ApiModelProperty(value = "第三方支付平台订单号，回调返回")
	private String thirdTransNo;
	/**用户编号*/
	@Excel(name = "用户编号", width = 15)
    @ApiModelProperty(value = "用户编号")
	private String userNo;
	/**用户类型（1-乘客 2-企业）*/
	@Excel(name = "用户类型（1-乘客 2-企业）", width = 15)
    @ApiModelProperty(value = "用户类型（1-乘客 2-企业）")
	private Integer userType;
	/**充值账户类型*/
	@Excel(name = "充值账户类型", width = 15)
    @ApiModelProperty(value = "充值账户类型")
	private Integer accountType;
	/**充值类型 1-在线充值 2-线下充值(由操作员在运营后台手动充值)*/
	@Excel(name = "充值类型 1-在线充值 2-线下充值(由操作员在运营后台手动充值)", width = 15)
    @ApiModelProperty(value = "充值类型 1-在线充值 2-线下充值(由操作员在运营后台手动充值)")
	private Integer rechargeType;
	/**支付渠道 与支付渠道账户表的channel_type一致*/
	@Excel(name = "支付渠道 与支付渠道账户表的channel_type一致", width = 15)
    @ApiModelProperty(value = "支付渠道 与支付渠道账户表的channel_type一致")
	private Integer channelType;
	/**充值金额*/
	@Excel(name = "充值金额", width = 15)
    @ApiModelProperty(value = "充值金额")
	private Integer amount;
	/**优惠金额*/
	@Excel(name = "优惠金额", width = 15)
    @ApiModelProperty(value = "优惠金额")
	private Integer discountAmount;
	/**充值优惠信息*/
	@Excel(name = "充值优惠信息", width = 15)
    @ApiModelProperty(value = "充值优惠信息")
	private String discountInfo;
	/**状态 0-默认 1-初始化  2-充值中  99-充值失败 100-充值成功*/
	@Excel(name = "状态 0-默认 1-初始化  2-充值中  99-充值失败 100-充值成功", width = 15)
    @ApiModelProperty(value = "状态 0-默认 1-初始化  2-充值中  99-充值失败 100-充值成功")
	private Integer status;
	/**支付设备来源类型 （1-android；2-ios；3-web; 4-h5）*/
	@Excel(name = "支付设备来源类型 （1-android；2-ios；3-web; 4-h5）", width = 15)
    @ApiModelProperty(value = "支付设备来源类型 （1-android；2-ios；3-web; 4-h5）")
	private Integer deviceType;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;
	/**客户付款的支付账号:支付宝账号或者微信账号或者银行卡号*/
	@Excel(name = "客户付款的支付账号:支付宝账号或者微信账号或者银行卡号", width = 15)
    @ApiModelProperty(value = "客户付款的支付账号:支付宝账号或者微信账号或者银行卡号")
	private String userPayAccount;
	/**国家编号+城市编号*/
	@Excel(name = "国家编号+城市编号", width = 15)
    @ApiModelProperty(value = "国家编号+城市编号")
	private String cityCode;
	/**是否退款过（0-否 1-是）*/
	@Excel(name = "是否退款过（0-否 1-是）", width = 15)
    @ApiModelProperty(value = "是否退款过（0-否 1-是）")
	private Integer isRefund;
	/**确认充值时间*/
    @ApiModelProperty(value = "确认充值时间")
	private Date affirmTime;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**更新时间*/
    @ApiModelProperty(value = "更新时间")
	private Date updateTime;
}
