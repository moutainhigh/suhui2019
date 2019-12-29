package org.suhui.modules.order.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import org.suhui.modules.utils.BaseUtil;

/**
 * @Description: 订单表
 * @Author: jeecg-boot
 * @Date:   2019-12-29
 * @Version: V1.0
 */
@Data
@TableName("order_main")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="order_main对象", description="订单表")
public class OrderMain {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**订单编号*/
	@Excel(name = "订单编号", width = 15)
    @ApiModelProperty(value = "订单编号")
	private java.lang.String orderCode;
	/**订单状态(待分配1、待用户支付2、待承兑商收款3、待承兑商兑付4、待用户确认收款5、已完成6、已作废0)*/
	@Excel(name = "订单状态(待分配1、待用户支付2、待承兑商收款3、待承兑商兑付4、待用户确认收款5、已完成6、已作废0)", width = 15)
    @ApiModelProperty(value = "订单状态(待分配1、待用户支付2、待承兑商收款3、待承兑商兑付4、待用户确认收款5、已完成6、已作废0)")
	private java.lang.Integer orderState;
	/**userNo*/
	@Excel(name = "userNo", width = 15)
    @ApiModelProperty(value = "userNo")
	private java.lang.String userNo;
	/**用户姓名*/
	@Excel(name = "用户姓名", width = 15)
    @ApiModelProperty(value = "用户姓名")
	private java.lang.String userName;
	/**用户联系方式*/
	@Excel(name = "用户联系方式", width = 15)
    @ApiModelProperty(value = "用户联系方式")
	private java.lang.String userContact;
	/**源币种*/
	@Excel(name = "源币种", width = 15)
    @ApiModelProperty(value = "源币种")
	private java.lang.String sourceCurrency;
	/**目标币种*/
	@Excel(name = "目标币种", width = 15)
    @ApiModelProperty(value = "目标币种")
	private java.lang.String targetCurrency;
	/**汇率*/
	@Excel(name = "汇率", width = 15)
    @ApiModelProperty(value = "汇率")
	private java.lang.Double exchangeRate;
	/**订单金额*/
	@Excel(name = "订单金额", width = 15)
    @ApiModelProperty(value = "订单金额")
	private java.lang.Integer orderMoney;
	/**用户支付方式(支付宝alipay、银行卡bank_card)*/
	@Excel(name = "用户支付方式(支付宝alipay、银行卡bank_card)", width = 15)
    @ApiModelProperty(value = "用户支付方式(支付宝alipay、银行卡bank_card)")
	private java.lang.String userPayMethod;
	/**用户支付时间*/
	@Excel(name = "用户支付时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "用户支付时间")
	private java.util.Date userPayTime;
	/**用户收款方式(支付宝alipay、银行卡bank_card)*/
	@Excel(name = "用户收款方式(支付宝alipay、银行卡bank_card)", width = 15)
    @ApiModelProperty(value = "用户收款方式(支付宝alipay、银行卡bank_card)")
	private java.lang.String userCollectionMethod;
	/**用户收款账号*/
	@Excel(name = "用户收款账号", width = 15)
    @ApiModelProperty(value = "用户收款账号")
	private java.lang.String userCollectionAccount;
	/**用户确认收款时间*/
	@Excel(name = "用户确认收款时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "用户确认收款时间")
	private java.util.Date userCollectionTime;
	/**承兑商ID*/
	@Excel(name = "承兑商ID", width = 15)
    @ApiModelProperty(value = "承兑商ID")
	private java.lang.String assurerId;
	/**承兑商名称*/
	@Excel(name = "承兑商名称", width = 15)
    @ApiModelProperty(value = "承兑商名称")
	private java.lang.String assurerName;
	/**承兑商收款方式(支付宝alipay、银行卡bank_card)*/
	@Excel(name = "承兑商收款方式(支付宝alipay、银行卡bank_card)", width = 15)
    @ApiModelProperty(value = "承兑商收款方式(支付宝alipay、银行卡bank_card)")
	private java.lang.String assurerCollectionMethod;
	/**承兑商收款账号*/
	@Excel(name = "承兑商收款账号", width = 15)
    @ApiModelProperty(value = "承兑商收款账号")
	private java.lang.String assurerCollectionAccount;
	/**承兑商确认收款时间*/
	@Excel(name = "承兑商确认收款时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "承兑商确认收款时间")
	private java.util.Date assurerCollectionTime;
	/**承兑商需兑付金额*/
	@Excel(name = "承兑商需兑付金额", width = 15)
    @ApiModelProperty(value = "承兑商需兑付金额")
	private java.lang.Integer assurerPayMoney;
	/**承兑商兑付时间*/
	@Excel(name = "承兑商兑付时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "承兑商兑付时间")
	private java.util.Date assurerPayTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String orderText;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
	private java.lang.String updateBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
	private java.util.Date updateTime;
	/**删除状态（0，正常，1已删除）*/
	@Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
	private java.lang.String delFlag;

	public String checkCreateRequireValue(){
		String message = "";
		if(!BaseUtil.Base_HasValue(this.userNo)){
			message += "缺少值用户编号,";
		}
		if(!BaseUtil.Base_HasValue(this.userName)){
			message += "缺少值用户姓名,";
		}
		if(!BaseUtil.Base_HasValue(this.userContact)){
			message += "缺少值用户联系方式,";
		}
		if(!BaseUtil.Base_HasValue(this.sourceCurrency)){
			message += "缺少值源币种,";
		}
		if(!BaseUtil.Base_HasValue(this.targetCurrency)){
			message += "缺少值目标币种,";
		}
		if(!BaseUtil.Base_HasValue(this.orderMoney)){
			message += "缺少值订单金额,";
		}
		if(!BaseUtil.Base_HasValue(this.userPayMethod)){
			message += "缺少值用户支付方式,";
		}
		if(!BaseUtil.Base_HasValue(this.userCollectionMethod)){
			message += "缺少值用户收款方式,";
		}
		if(!BaseUtil.Base_HasValue(this.userCollectionAccount)){
			message += "缺少值用户收款账号,";
		}
		if(BaseUtil.Base_HasValue(message)){
			message = message.substring(0, message.length()-1);
		}
		return message;
	}
}
