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
 * @Description: 承兑商金额变动
 * @Author: jeecg-boot
 * @Date:   2020-02-22
 * @Version: V1.0
 */
@Data
@TableName("order_assurer_money_change")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="order_assurer_money_change对象", description="承兑商金额变动")
public class OrderAssurerMoneyChange {
    
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
	/**变动金额*/
	@Excel(name = "变动金额", width = 15)
    @ApiModelProperty(value = "变动金额")
	private java.lang.Double changeMoney;
	/**增加、减少*/
	@Excel(name = "增加、减少", width = 15)
    @ApiModelProperty(value = "增加、减少")
	private java.lang.String changeType;
	/**变动种类 保证金、租赁金、订单*/
	@Excel(name = "变动种类 保证金、租赁金、订单", width = 15)
    @ApiModelProperty(value = "变动种类 保证金、租赁金、订单")
	private java.lang.String changeClass;
	/**标记 待处理、已处理*/
	@Excel(name = "标记 待处理、已处理", width = 15)
    @ApiModelProperty(value = "标记 待处理、已处理")
	private java.lang.String flag;
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
	private java.lang.String changeText;
	private java.lang.String orderId;
	private java.lang.String orderNo;
	private java.lang.String errorText;

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

	public Double getChangeMoney() {
		return changeMoney;
	}

	public void setChangeMoney(Double changeMoney) {
		this.changeMoney = changeMoney;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getChangeClass() {
		return changeClass;
	}

	public void setChangeClass(String changeClass) {
		this.changeClass = changeClass;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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

	public String getChangeText() {
		return changeText;
	}

	public void setChangeText(String changeText) {
		this.changeText = changeText;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
}
