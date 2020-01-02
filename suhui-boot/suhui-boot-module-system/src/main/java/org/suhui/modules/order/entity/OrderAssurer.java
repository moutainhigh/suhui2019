package org.suhui.modules.order.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 去
 * @Author: jeecg-boot
 * @Date:   2019-12-29
 * @Version: V1.0
 */
@Data
@TableName("order_assurer")
public class OrderAssurer implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户编号*/
	private java.lang.String userNo;
	/**承兑商名称*/
	private java.lang.String assurerName;
	/**承兑商国家码*/
	private java.lang.String countryCode;
	/**承兑商状态(待审核(to_audit)、正常接单(normal)、余额不足(lack_balance)、禁止接单(ban_order))*/
	private java.lang.String assurerState;
	/**费率*/
	private java.lang.Double assurerRate;
	/**可用额度*/
	private java.lang.Integer canUseLimit;
	/**已用额度*/
	private java.lang.Integer usedLimit;
	/**总额度=已用额度+可用额度*/
	private java.lang.Integer totalLimit;
	/**策略(单一制(one)、多单制(more))*/
	private java.lang.String assurerStrategy;
	/**创建人*/
	private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**更新人*/
	private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**删除状态（0，正常，1已删除）*/
	private java.lang.String delFlag;
	private java.lang.Integer onlineState;
	private java.lang.Integer payLockMoney;
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getAssurerName() {
		return assurerName;
	}

	public void setAssurerName(String assurerName) {
		this.assurerName = assurerName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getAssurerState() {
		return assurerState;
	}

	public void setAssurerState(String assurerState) {
		this.assurerState = assurerState;
	}

	public Double getAssurerRate() {
		return assurerRate;
	}

	public void setAssurerRate(Double assurerRate) {
		this.assurerRate = assurerRate;
	}

	public Integer getCanUseLimit() {
		return canUseLimit;
	}

	public void setCanUseLimit(Integer canUseLimit) {
		this.canUseLimit = canUseLimit;
	}

	public Integer getUsedLimit() {
		return usedLimit;
	}

	public void setUsedLimit(Integer usedLimit) {
		this.usedLimit = usedLimit;
	}

	public Integer getTotalLimit() {
		return totalLimit;
	}

	public void setTotalLimit(Integer totalLimit) {
		this.totalLimit = totalLimit;
	}

	public String getAssurerStrategy() {
		return assurerStrategy;
	}

	public void setAssurerStrategy(String assurerStrategy) {
		this.assurerStrategy = assurerStrategy;
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

	public Integer getOnlineState() {
		return onlineState;
	}

	public void setOnlineState(Integer onlineState) {
		this.onlineState = onlineState;
	}

	public Integer getPayLockMoney() {
		return payLockMoney;
	}

	public void setPayLockMoney(Integer payLockMoney) {
		this.payLockMoney = payLockMoney;
	}
}