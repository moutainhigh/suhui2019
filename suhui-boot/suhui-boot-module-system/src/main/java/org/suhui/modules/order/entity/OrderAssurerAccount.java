package org.suhui.modules.order.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;

/**
 * @Description: 客户明细
 * @Author: jeecg-boot
 * @Date:   2019-12-29
 * @Version: V1.0
 */
@Data
@TableName("order_assurer_account")
public class OrderAssurerAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**承兑商ID*/
	private java.lang.String assurerId;
	/**账户类型(支付宝(alipay)、银行卡(bank_card))*/
    @Excel(name = "账户类型(支付宝(alipay)、银行卡(bank_card))", width = 15)
	private java.lang.String accountType;
	/**账户*/
    @Excel(name = "账户", width = 15)
	private java.lang.String accountNo;
	/**真实姓名*/
    @Excel(name = "真实姓名", width = 15)
	private java.lang.String realName;
	/**使用方式 (支付(pay)、收款(collection)、支付+收款(pay_collection))*/
    @Excel(name = "使用方式 (支付(pay)、收款(collection)、支付+收款(pay_collection))", width = 15)
	private java.lang.String useType;
	/**每日支付限额*/
    @Excel(name = "每日支付限额", width = 15)
	private java.lang.Integer payLimit;
	/**支付已用额度*/
    @Excel(name = "支付已用额度", width = 15)
	private java.lang.Integer payUsedLimit;
	/**支付可用额度(日)*/
    @Excel(name = "支付可用额度(日)", width = 15)
	private java.lang.Integer payCanUseLimit;
	/**收款已用额度(日)*/
    @Excel(name = "收款已用额度(日)", width = 15)
	private java.lang.Integer collectionUsedLimit;
	/**创建人*/
    @Excel(name = "创建人", width = 15)
	private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**更新人*/
    @Excel(name = "更新人", width = 15)
	private java.lang.String updateBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**删除状态（0，正常，1已删除）*/
    @Excel(name = "删除状态（0，正常，1已删除）", width = 15)
	private java.lang.String delFlag;
}
