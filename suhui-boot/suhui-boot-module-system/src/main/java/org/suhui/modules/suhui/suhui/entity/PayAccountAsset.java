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
 * @Description: 账户资产表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("pay_account_asset")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="pay_account_asset对象", description="账户资产表")
public class PayAccountAsset {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**身份编号*/
	@Excel(name = "身份编号", width = 15)
    @ApiModelProperty(value = "身份编号")
	private String identityNo;
	/**账户编号*/
	@Excel(name = "账户编号", width = 15)
    @ApiModelProperty(value = "账户编号")
	private String accountNo;
	/**账户类型编码*/
	@Excel(name = "账户类型编码", width = 15)
    @ApiModelProperty(value = "账户类型编码")
	private Integer accountTypeCode;
	/**可用金额*/
	@Excel(name = "可用金额", width = 15)
    @ApiModelProperty(value = "可用金额")
	private Integer availableAmount;
	/**冻结金额*/
	@Excel(name = "冻结金额", width = 15)
    @ApiModelProperty(value = "冻结金额")
	private Integer frozenAmount;
	/**货币单位 1-人民币*/
	@Excel(name = "货币单位 1-人民币", width = 15)
    @ApiModelProperty(value = "货币单位 1-人民币")
	private Integer currencyCode;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**更新时间*/
    @ApiModelProperty(value = "更新时间")
	private Date updateTime;
}
