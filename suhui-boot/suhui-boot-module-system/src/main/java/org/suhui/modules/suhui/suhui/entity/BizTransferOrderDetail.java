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
 * @Description: 转账详情表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("biz_transfer_order_detail")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="biz_transfer_order_detail对象", description="转账详情表")
public class BizTransferOrderDetail {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**收银台转账流水号*/
	@Excel(name = "收银台转账流水号", width = 15)
    @ApiModelProperty(value = "收银台转账流水号")
	private String bizTransferNo;
	/**账户类型 1-人民币账户 2-美元账户 3-菲币账户 在枚举中维护*/
	@Excel(name = "账户类型 1-人民币账户 2-美元账户 3-菲币账户 在枚举中维护", width = 15)
    @ApiModelProperty(value = "账户类型 1-人民币账户 2-美元账户 3-菲币账户 在枚举中维护")
	private Integer transferAccountType;
	/**转出账户编号*/
	@Excel(name = "转出账户编号", width = 15)
    @ApiModelProperty(value = "转出账户编号")
	private String transferAccountNo;
	/**转入用户类型 1-个人 2-企业*/
	@Excel(name = "转入用户类型 1-个人 2-企业", width = 15)
    @ApiModelProperty(value = "转入用户类型 1-个人 2-企业")
	private Integer transfeeUserType;
	/**转入用户编号*/
	@Excel(name = "转入用户编号", width = 15)
    @ApiModelProperty(value = "转入用户编号")
	private String transfeeUserNo;
	/**账户类型 1-人民币账户 2-美元账户 3-菲币账户 在枚举中维护*/
	@Excel(name = "账户类型 1-人民币账户 2-美元账户 3-菲币账户 在枚举中维护", width = 15)
    @ApiModelProperty(value = "账户类型 1-人民币账户 2-美元账户 3-菲币账户 在枚举中维护")
	private Integer transfeeAccountType;
	/**转入账户编号*/
	@Excel(name = "转入账户编号", width = 15)
    @ApiModelProperty(value = "转入账户编号")
	private String transfeeAccountNo;
	/**子状态（1：转账受理 2：转账中  99 转账失败 100：成功）*/
	@Excel(name = "子状态（1：转账受理 2：转账中  99 转账失败 100：成功）", width = 15)
    @ApiModelProperty(value = "子状态（1：转账受理 2：转账中  99 转账失败 100：成功）")
	private Integer subStatus;
	/**转账金额*/
	@Excel(name = "转账金额", width = 15)
    @ApiModelProperty(value = "转账金额")
	private Integer amount;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**更新时间*/
    @ApiModelProperty(value = "更新时间")
	private Date updateTime;
}
