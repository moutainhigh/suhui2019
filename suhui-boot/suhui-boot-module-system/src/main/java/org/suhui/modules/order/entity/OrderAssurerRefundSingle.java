package org.suhui.modules.order.entity;

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
 * @Description: 退款单
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
@Data
@TableName("order_assurer_refund_single")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="order_assurer_refund_single对象", description="退款单")
public class OrderAssurerRefundSingle {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**承兑商ID*/
	@Excel(name = "承兑商ID", width = 15)
    @ApiModelProperty(value = "承兑商ID")
	private java.lang.String assurerId;
	/**承兑商名称*/
	@Excel(name = "承兑商名称", width = 15)
    @ApiModelProperty(value = "承兑商名称")
	private java.lang.String assurerName;
	/**电话*/
	@Excel(name = "电话", width = 15)
    @ApiModelProperty(value = "电话")
	private java.lang.String assurerPhone;
	/**账户id*/
	@Excel(name = "账户id", width = 15)
    @ApiModelProperty(value = "账户id")
	private java.lang.String accountId;
	/**账户*/
	@Excel(name = "账户", width = 15)
    @ApiModelProperty(value = "账户")
	private java.lang.String accountNo;
	/**账户类型*/
	@Excel(name = "账户类型", width = 15)
    @ApiModelProperty(value = "账户类型")
	private java.lang.String accountType;
	/**开户行*/
	@Excel(name = "开户行", width = 15)
    @ApiModelProperty(value = "开户行")
	private java.lang.String openBank;
	/**开户网点*/
	@Excel(name = "开户网点", width = 15)
    @ApiModelProperty(value = "开户网点")
	private java.lang.String openBankBranch;
	/**真实姓名*/
	@Excel(name = "真实姓名", width = 15)
    @ApiModelProperty(value = "真实姓名")
	private java.lang.String realName;
	/**国际电汇代码*/
	@Excel(name = "国际电汇代码", width = 15)
    @ApiModelProperty(value = "国际电汇代码")
	private java.lang.String swiftCode;
	/**退款金额*/
	@Excel(name = "退款金额", width = 15)
    @ApiModelProperty(value = "退款金额")
	private java.lang.Double refundMoney;
	/**平台缴费凭证*/
	@Excel(name = "平台缴费凭证", width = 15)
    @ApiModelProperty(value = "平台缴费凭证")
	private java.lang.String refundVoucher;
	/**退款原因*/
	@Excel(name = "退款原因", width = 15)
    @ApiModelProperty(value = "退款原因")
	private java.lang.String refundText;
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
	/**退款单状态 审核中(auditing)、审核通过(pass)、审核拒绝(reject)*/
	@Excel(name = "退款单状态 审核中(auditing)、审核通过(pass)、审核拒绝(reject)", width = 15)
    @ApiModelProperty(value = "退款单状态 审核中(auditing)、审核通过(pass)、审核拒绝(reject)")
	private java.lang.String refundSingleState;
	private java.lang.String moneyType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAssurerId() {
		return assurerId;
	}

	public void setAssurerId(String assurerId) {
		this.assurerId = assurerId;
	}

	public String getAssurerName() {
		return assurerName;
	}

	public void setAssurerName(String assurerName) {
		this.assurerName = assurerName;
	}

	public String getAssurerPhone() {
		return assurerPhone;
	}

	public void setAssurerPhone(String assurerPhone) {
		this.assurerPhone = assurerPhone;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getOpenBank() {
		return openBank;
	}

	public void setOpenBank(String openBank) {
		this.openBank = openBank;
	}

	public String getOpenBankBranch() {
		return openBankBranch;
	}

	public void setOpenBankBranch(String openBankBranch) {
		this.openBankBranch = openBankBranch;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public Double getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(Double refundMoney) {
		this.refundMoney = refundMoney;
	}

	public String getRefundVoucher() {
		return refundVoucher;
	}

	public void setRefundVoucher(String refundVoucher) {
		this.refundVoucher = refundVoucher;
	}

	public String getRefundText() {
		return refundText;
	}

	public void setRefundText(String refundText) {
		this.refundText = refundText;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getRefundSingleState() {
		return refundSingleState;
	}

	public void setRefundSingleState(String refundSingleState) {
		this.refundSingleState = refundSingleState;
	}

	public String getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}
}
