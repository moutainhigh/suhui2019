package org.suhui.modules.toB.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.suhui.modules.utils.BaseUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 类说明：商户
 *
 * @author: 蔡珊珊
 * @create:  2020-04-07 22:57
 **/

@Data
@TableName("tob_order_merchant")
public class OrderMerchant implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    private java.lang.String id;
    /**
     * 用户编号
     */
    private java.lang.String userNo;
    /**
     * 商户名称
     */
    private java.lang.String merchantName;
    /**
     * 商户国家码
     */
    private java.lang.String countryCode;
    /**
     * 承兑商状态(待审核(to_audit)、正常接单(normal)、余额不足(lack_balance)、禁止接单(ban_order))
     */
    private java.lang.String merchantState;
    /**
     * 费率
     */
    private java.lang.Double merchantRate;
    /**
     * 可用额度
     */
    private java.lang.Double canUseLimit;
    /**
     * 已用额度
     */
    private java.lang.Double usedLimit;
    /**
     * 总额度=已用额度+可用额度
     */
    private java.lang.Double totalLimit;
    /**
     * 策略(单一制(one)、多单制(more))
     */
    private java.lang.String merchantStrategy;
    /**
     * 创建人
     */
    private java.lang.String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    private java.lang.String updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;
    /**
     * 删除状态（0，正常，1已删除）
     */
    private java.lang.String delFlag;
    private java.lang.String merchantPhone;
    private java.lang.Integer onlineState;
    private java.lang.Double payLockMoney;
    private java.lang.Double ensureMoney;
    private java.lang.Double leaseMoney;
    private java.lang.Double ensureProportion;
    private java.lang.String merchantPicture;
    private java.lang.String cardType;
    private java.lang.String cardNo;
    private java.lang.String cardFrontPicture;
    private java.lang.String cardBackPicture;
    private java.lang.String cardHoldPicture;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public void changeMoneyToPoints() {
        if (BaseUtil.Base_HasValue(this.totalLimit)) {
            this.totalLimit = this.totalLimit * 100;
        }
        if (BaseUtil.Base_HasValue(this.canUseLimit)) {
            this.canUseLimit = this.canUseLimit * 100;
        }
        if (BaseUtil.Base_HasValue(this.usedLimit)) {
            this.usedLimit = this.usedLimit * 100;
        }
        if (BaseUtil.Base_HasValue(this.payLockMoney)) {
            this.payLockMoney = this.payLockMoney * 100;
        }
        if (BaseUtil.Base_HasValue(this.ensureMoney)) {
            this.ensureMoney = this.ensureMoney * 100;
        }
        if (BaseUtil.Base_HasValue(this.leaseMoney)) {
            this.leaseMoney = this.leaseMoney * 100;
        }
    }

    public void changeMoneyToBig() {
        if (BaseUtil.Base_HasValue(this.canUseLimit) && this.canUseLimit > 0) {
            this.canUseLimit = this.canUseLimit / 100;
        }
        if (BaseUtil.Base_HasValue(this.usedLimit) && this.usedLimit > 0) {
            this.usedLimit = this.usedLimit / 100;
        }
        if (BaseUtil.Base_HasValue(this.totalLimit) && this.totalLimit > 0) {
            this.totalLimit = this.totalLimit / 100;
        }
        if (BaseUtil.Base_HasValue(this.payLockMoney) && this.payLockMoney > 0) {
            this.payLockMoney = this.payLockMoney / 100;
        }
        if (BaseUtil.Base_HasValue(this.ensureMoney)) {
            this.ensureMoney = this.ensureMoney / 100;
        }
        if (BaseUtil.Base_HasValue(this.leaseMoney)) {
            this.leaseMoney = this.leaseMoney / 100;
        }
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

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMerchantState() {
        return merchantState;
    }

    public void setMerchantState(String merchantState) {
        this.merchantState = merchantState;
    }

    public Double getMerchantRate() {
        return merchantRate;
    }

    public void setMerchantRate(Double merchantRate) {
        this.merchantRate = merchantRate;
    }

    public Double getCanUseLimit() {
        return canUseLimit;
    }

    public void setCanUseLimit(Double canUseLimit) {
        this.canUseLimit = canUseLimit;
    }

    public Double getUsedLimit() {
        return usedLimit;
    }

    public void setUsedLimit(Double usedLimit) {
        this.usedLimit = usedLimit;
    }

    public Double getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(Double totalLimit) {
        this.totalLimit = totalLimit;
    }

    public String getmMerchantStrategy() {
        return merchantStrategy;
    }

    public void setMerchantStrategy(String merchantStrategy) {
        this.merchantStrategy = merchantStrategy;
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

    public Double getPayLockMoney() {
        return payLockMoney;
    }

    public void setPayLockMoney(Double payLockMoney) {
        this.payLockMoney = payLockMoney;
    }

    public String getMerchantPhone() {
        return merchantPhone;
    }

    public void setMerchantPhone(String merchantPhone) {
        this.merchantPhone = merchantPhone;
    }

    public Double getEnsureMoney() {
        return ensureMoney;
    }

    public void setEnsureMoney(Double ensureMoney) {
        this.ensureMoney = ensureMoney;
    }

    public Double getLeaseMoney() {
        return leaseMoney;
    }

    public void setLeaseMoney(Double leaseMoney) {
        this.leaseMoney = leaseMoney;
    }

    public Double getEnsureProportion() {
        return ensureProportion;
    }

    public void setEnsureProportion(Double ensureProportion) {
        this.ensureProportion = ensureProportion;
    }

    public String getMerchantPicture() {
        return merchantPicture;
    }

    public void setMerchantPicture(String merchantPicture) {
        this.merchantPicture = merchantPicture;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardFrontPicture() {
        return cardFrontPicture;
    }

    public void setCardFrontPicture(String cardFrontPicture) {
        this.cardFrontPicture = cardFrontPicture;
    }

    public String getCardBackPicture() {
        return cardBackPicture;
    }

    public void setCardBackPicture(String cardBackPicture) {
        this.cardBackPicture = cardBackPicture;
    }

    public String getCardHoldPicture() {
        return cardHoldPicture;
    }

    public void setCardHoldPicture(String cardHoldPicture) {
        this.cardHoldPicture = cardHoldPicture;
    }

}