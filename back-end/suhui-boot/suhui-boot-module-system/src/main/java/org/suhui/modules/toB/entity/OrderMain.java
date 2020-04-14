package org.suhui.modules.toB.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;
import org.suhui.modules.utils.BaseUtil;

import java.util.Date;

/**
 * 类说明：订单表
 *
 * @author: 蔡珊珊
 * @create: 2020-04-14 13:09
 */
@Data
@TableName("tob_order_main")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "order_main对象", description = "订单表")
public class OrderMain {
    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 订单编号
     */
    @Excel(name = "订单编号", width = 15)
    @ApiModelProperty(value = "订单编号")
    private String orderCode;
    /**
     * 订单状态(1 创建、2 待确认收款、3 已确认收款、0、已作废0)
     */
    @Excel(name = "订单状态(1 创建、2 待确认收款、3 已确认收款、0、已作废0)", width = 15)
    @ApiModelProperty(value = "订单状态(1 创建、2 待确认收款、3 已确认收款、0、已作废0)")

    private String orderState;
    /**
     * userNo
     */
    @Excel(name = "userNo", width = 15)
    @ApiModelProperty(value = "userNo")
    private String userNo;
    /**
     * 用户姓名
     */
    @Excel(name = "用户姓名", width = 15)
    @ApiModelProperty(value = "用户姓名")
    private String userName;
    /**
     * 用户联系方式
     */
    @Excel(name = "用户联系方式", width = 15)
    @ApiModelProperty(value = "用户联系方式")
    private String userContact;
    /**
     * 源币种
     */
    @Excel(name = "源币种", width = 15)
    @ApiModelProperty(value = "源币种")
    private String sourceCurrency;
    /**
     * 目标币种
     */
    @Excel(name = "目标币种", width = 15)
    @ApiModelProperty(value = "目标币种")
    private String targetCurrency;
    /**
     * 汇率
     */
    @Excel(name = "汇率", width = 15)
    @ApiModelProperty(value = "汇率")
    private Double exchangeRate;
    /**
     * 源币种金额
     */
    @Excel(name = "源币种金额", width = 15)
    @ApiModelProperty(value = "源币种金额")
    private Double sourceCurrencyMoney;
    /**
     * 目标币种金额
     */
    @Excel(name = "目标币种金额", width = 15)
    @ApiModelProperty(value = "目标币种金额")
    private java.lang.Double targetCurrencyMoney;
    /**
     * 用户支付方式(支付宝alipay、银行卡bank_card)
     */
    @Excel(name = "用户支付方式(支付宝alipay、银行卡bank_card)", width = 15)
    @ApiModelProperty(value = "用户支付方式(支付宝alipay、银行卡bank_card)")
    private String userPayMethod;
    private String userPayAccount;
    private String userPayAccountUser;
    private String userPayBank;
    private String userPayBankBranch;
    private String userPayAreaCode;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String orderText;
    /**
     * 创建人
     */
    @Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 更新人
     */
    @Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 更新时间
     */
    @Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    /**
     * 删除状态（0，正常，1已删除）
     */
    @Excel(name = "删除状态（0，正常，1已删除）", width = 15)
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
    private String delFlag;
    /**
     * 确认收款之后，商户的回调地址
     */
    private String notifyUrl;
    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Double getSourceCurrencyMoney() {
        return sourceCurrencyMoney;
    }

    public void setSourceCurrencyMoney(Double sourceCurrencyMoney) {
        this.sourceCurrencyMoney = sourceCurrencyMoney;
    }

    public String getUserPayMethod() {
        return userPayMethod;
    }

    public void setUserPayMethod(String userPayMethod) {
        this.userPayMethod = userPayMethod;
    }


    public String getOrderText() {
        return orderText;
    }

    public void setOrderText(String orderText) {
        this.orderText = orderText;
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

    public String getUserPayAccount() {
        return userPayAccount;
    }

    public void setUserPayAccount(String userPayAccount) {
        this.userPayAccount = userPayAccount;
    }
    public String getUserPayBank() {
        return userPayBank;
    }

    public void setUserPayBank(String userPayBank) {
        this.userPayBank = userPayBank;
    }

    public String getUserPayBankBranch() {
        return userPayBankBranch;
    }

    public void setUserPayBankBranch(String userPayBankBranch) {
        this.userPayBankBranch = userPayBankBranch;
    }
    public String getUserPayAreaCode() {
        return userPayAreaCode;
    }

    public void setUserPayAreaCode(String userPayAreaCode) {
        this.userPayAreaCode = userPayAreaCode;
    }
    public String getUserPayAccountUser() {
        return userPayAccountUser;
    }

    public void setUserPayAccountUser(String userPayAccountUser) {
        this.userPayAccountUser = userPayAccountUser;
    }

    public Double getTargetCurrencyMoney() {
        return targetCurrencyMoney;
    }

    public void setTargetCurrencyMoney(Double targetCurrencyMoney) {
        this.targetCurrencyMoney = targetCurrencyMoney;
    }


    public void changeMoneyToPoints() {
        if (BaseUtil.Base_HasValue(this.targetCurrencyMoney) && this.targetCurrencyMoney > 0) {
            this.targetCurrencyMoney = this.targetCurrencyMoney * 100;
        }
        if (BaseUtil.Base_HasValue(this.sourceCurrencyMoney) && this.sourceCurrencyMoney > 0) {
            this.sourceCurrencyMoney = this.sourceCurrencyMoney * 100;
        }
    }

    public void changeMoneyToBig() {
        if (BaseUtil.Base_HasValue(this.targetCurrencyMoney) && this.targetCurrencyMoney > 0) {
            this.targetCurrencyMoney = this.targetCurrencyMoney / 100;
        }
        if (BaseUtil.Base_HasValue(this.sourceCurrencyMoney) && this.sourceCurrencyMoney > 0) {
            this.sourceCurrencyMoney = this.sourceCurrencyMoney / 100;
        }
    }

    public String checkCreateRequireValue() {
        String message = "";
        if (!BaseUtil.Base_HasValue(this.userNo)) {
            message += "缺少值用户编号,";
        }
        if (!BaseUtil.Base_HasValue(this.userName)) {
            message += "缺少值用户姓名,";
        }
        if (!BaseUtil.Base_HasValue(this.userContact)) {
            message += "缺少值用户联系方式,";
        }
        if (!BaseUtil.Base_HasValue(this.sourceCurrency)) {
            message += "缺少值源币种,";
        }
        if (!BaseUtil.Base_HasValue(this.exchangeRate)) {
            message += "缺少值汇率,";
        }
        if (!BaseUtil.Base_HasValue(this.targetCurrency)) {
            message += "缺少值目标币种,";
        }
        if (!BaseUtil.Base_HasValue(this.targetCurrencyMoney)) {
            message += "缺少值目标币种金额,";
        }
        if (!BaseUtil.Base_HasValue(this.userPayMethod)) {
            message += "缺少值用户支付方式,";
        }
        if (BaseUtil.Base_HasValue(message)) {
            message = message.substring(0, message.length() - 1);
        }
        return message;
    }
}
