package org.suhui.modules.toB.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;
import org.suhui.modules.utils.BaseUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 类说明：商户账户表
 *
 * @author: 蔡珊珊
 * @create: 2020-04-14 17:14
 */
@Data
@TableName("tob_order_merchant_account")
public class OrderMerchantAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.UUID)
    private java.lang.String id;
    /**承兑商ID*/
    private java.lang.String merchantId;
    /**账户类型(支付宝(alipay)、银行卡(bank_card))*/
    @Excel(name = "账户类型(支付宝(alipay)、银行卡(bank_card))", width = 15)
    private java.lang.String accountType;
    /**开户行*/
    @Excel(name = "开户行", width = 15)
    private java.lang.String openBank;
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
    private java.lang.Double payLimit;
    /**支付已用额度*/
    @Excel(name = "支付已用额度", width = 15)
    private java.lang.Double payUsedLimit;
    /**支付可用额度(日)*/
    @Excel(name = "支付可用额度(日)", width = 15)
    private java.lang.Double payCanUseLimit;
    /**收款已用额度(日)*/
    @Excel(name = "收款已用额度(日)", width = 15)
    private java.lang.Double collectionUsedLimit;
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
    private java.lang.Double payLockMoney;
    private java.lang.String openBankBranch;
    private java.lang.String areaCode;
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public void changeMoneyToPoints(){
        if (BaseUtil.Base_HasValue(this.payLimit) && this.payLimit > 0) {
            this.payLimit = this.payLimit * 100;
        }
        if (BaseUtil.Base_HasValue(this.payUsedLimit) && this.payUsedLimit > 0) {
            this.payUsedLimit = this.payUsedLimit * 100;
        }
        if (BaseUtil.Base_HasValue(this.payCanUseLimit) && this.payCanUseLimit > 0) {
            this.payCanUseLimit = this.payCanUseLimit * 100;
        }
        if (BaseUtil.Base_HasValue(this.payLockMoney) && this.payLockMoney > 0) {
            this.payLockMoney = this.payLockMoney * 100;
        }
    }

    public void changeMoneyToBig(){
        if (BaseUtil.Base_HasValue(this.payLimit) && this.payLimit > 0) {
            this.payLimit = this.payLimit / 100;
        }
        if (BaseUtil.Base_HasValue(this.payUsedLimit) && this.payUsedLimit > 0) {
            this.payUsedLimit = this.payUsedLimit / 100;
        }
        if (BaseUtil.Base_HasValue(this.payCanUseLimit) && this.payCanUseLimit > 0) {
            this.payCanUseLimit = this.payCanUseLimit / 100;
        }
        if (BaseUtil.Base_HasValue(this.payLockMoney) && this.payLockMoney > 0) {
            this.payLockMoney = this.payLockMoney / 100;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
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

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    public Double getPayLimit() {
        return payLimit;
    }

    public void setPayLimit(Double payLimit) {
        this.payLimit = payLimit;
    }

    public Double getPayUsedLimit() {
        return payUsedLimit;
    }

    public void setPayUsedLimit(Double payUsedLimit) {
        this.payUsedLimit = payUsedLimit;
    }

    public Double getPayCanUseLimit() {
        return payCanUseLimit;
    }

    public void setPayCanUseLimit(Double payCanUseLimit) {
        this.payCanUseLimit = payCanUseLimit;
    }

    public Double getCollectionUsedLimit() {
        return collectionUsedLimit;
    }

    public void setCollectionUsedLimit(Double collectionUsedLimit) {
        this.collectionUsedLimit = collectionUsedLimit;
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

    public Double getPayLockMoney() {
        return payLockMoney;
    }

    public void setPayLockMoney(Double payLockMoney) {
        this.payLockMoney = payLockMoney;
    }

    public String getOpenBankBranch() {
        return openBankBranch;
    }

    public void setOpenBankBranch(String openBankBranch) {
        this.openBankBranch = openBankBranch;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}

