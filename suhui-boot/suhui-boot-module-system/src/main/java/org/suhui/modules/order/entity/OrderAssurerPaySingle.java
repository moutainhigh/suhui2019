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
 * @Description: 缴费单管理
 * @Author: jeecg-boot
 * @Date:   2020-02-19
 * @Version: V1.0
 */
@Data
@TableName("order_assurer_pay_single")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="order_assurer_pay_single对象", description="缴费单管理")
public class OrderAssurerPaySingle {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**账户id*/
	@Excel(name = "账户id", width = 15)
    @ApiModelProperty(value = "账户id")
	private java.lang.String platformAccountId;
	/**账户*/
	@Excel(name = "账户", width = 15)
    @ApiModelProperty(value = "账户")
	private java.lang.String platformAccountNo;
	/**账户类型*/
	@Excel(name = "账户类型", width = 15)
    @ApiModelProperty(value = "账户类型")
	private java.lang.String platformAccountType;
	/**缴费金额*/
	@Excel(name = "缴费金额", width = 15)
    @ApiModelProperty(value = "缴费金额")
	private java.lang.Double payMoney;
	/**缴费凭证*/
	@Excel(name = "缴费凭证", width = 15)
    @ApiModelProperty(value = "缴费凭证")
	private java.lang.String payVoucher;
	/**款项说明*/
	@Excel(name = "款项说明", width = 15)
    @ApiModelProperty(value = "款项说明")
	private java.lang.String payText;
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

	private java.lang.String assurerId;
	private java.lang.String assurerName;
	private java.lang.String assurerPhone;
	private java.lang.String openBank;
	private java.lang.String openBankBranch;
	private java.lang.String realName;
	private java.lang.String swiftCode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlatformAccountId() {
		return platformAccountId;
	}

	public void setPlatformAccountId(String platformAccountId) {
		this.platformAccountId = platformAccountId;
	}

	public String getPlatformAccountNo() {
		return platformAccountNo;
	}

	public void setPlatformAccountNo(String platformAccountNo) {
		this.platformAccountNo = platformAccountNo;
	}

	public String getPlatformAccountType() {
		return platformAccountType;
	}

	public void setPlatformAccountType(String platformAccountType) {
		this.platformAccountType = platformAccountType;
	}

	public Double getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}

	public String getPayVoucher() {
		return payVoucher;
	}

	public void setPayVoucher(String payVoucher) {
		this.payVoucher = payVoucher;
	}

	public String getPayText() {
		return payText;
	}

	public void setPayText(String payText) {
		this.payText = payText;
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
}
