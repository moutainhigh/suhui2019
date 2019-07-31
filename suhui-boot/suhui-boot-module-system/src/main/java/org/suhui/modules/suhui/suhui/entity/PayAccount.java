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
 * @Description: 账户表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("pay_account")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="pay_account对象", description="账户表")
public class PayAccount {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**账户编号*/
	@Excel(name = "账户编号", width = 15)
    @ApiModelProperty(value = "账户编号")
	private String accountNo;
	/**账户类型编码*/
	@Excel(name = "账户类型编码", width = 15)
    @ApiModelProperty(value = "账户类型编码")
	private Integer accountTypeCode;
	/**账户名称*/
	@Excel(name = "账户名称", width = 15)
    @ApiModelProperty(value = "账户名称")
	private String accountName;
	/**身份编号*/
	@Excel(name = "身份编号", width = 15)
    @ApiModelProperty(value = "身份编号")
	private Integer identityNo;
	/**身份类型 1-个人身份 2-企业身份*/
	@Excel(name = "身份类型 1-个人身份 2-企业身份", width = 15)
    @ApiModelProperty(value = "身份类型 1-个人身份 2-企业身份")
	private Integer identityType;
	/**状态 0-默认 1-有效 2-无效*/
	@Excel(name = "状态 0-默认 1-有效 2-无效", width = 15)
    @ApiModelProperty(value = "状态 0-默认 1-有效 2-无效")
	private Integer status;
	/**是否允许充值*/
	@Excel(name = "是否允许充值", width = 15)
    @ApiModelProperty(value = "是否允许充值")
	private Integer isAllowRecharge;
	/**是否允许提现*/
	@Excel(name = "是否允许提现", width = 15)
    @ApiModelProperty(value = "是否允许提现")
	private Integer isAllowWithdraw;
	/**是否允许透支*/
	@Excel(name = "是否允许透支", width = 15)
    @ApiModelProperty(value = "是否允许透支")
	private Integer isAllowOverdraft;
	/**是否允许转账转入*/
	@Excel(name = "是否允许转账转入", width = 15)
    @ApiModelProperty(value = "是否允许转账转入")
	private Integer isAllowTransferIn;
	/**是否允许转账转出*/
	@Excel(name = "是否允许转账转出", width = 15)
    @ApiModelProperty(value = "是否允许转账转出")
	private Integer isAllowTransferOut;
	/**是否冻结，表示冻结账户*/
	@Excel(name = "是否冻结，表示冻结账户", width = 15)
    @ApiModelProperty(value = "是否冻结，表示冻结账户")
	private Integer isFrozen;
	/**remark*/
	@Excel(name = "remark", width = 15)
    @ApiModelProperty(value = "remark")
	private String remark;
	/**createTime*/
    @ApiModelProperty(value = "createTime")
	private Date createTime;
	/**updateTime*/
    @ApiModelProperty(value = "updateTime")
	private Date updateTime;
}
