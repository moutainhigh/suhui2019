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
 * @Description: 货币类型表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("pay_currency_type")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="pay_currency_type对象", description="货币类型表")
public class PayCurrencyType {
    
	/**自增ID*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "自增ID")
	private Integer id;
	/**货币编号*/
	@Excel(name = "货币编号", width = 15)
    @ApiModelProperty(value = "货币编号")
	private String currencyCode;
	/**货币名字*/
	@Excel(name = "货币名字", width = 15)
    @ApiModelProperty(value = "货币名字")
	private String currencyName;
	/**货币符号*/
	@Excel(name = "货币符号", width = 15)
    @ApiModelProperty(value = "货币符号")
	private String currencySymbol;
	/**货币单位*/
	@Excel(name = "货币单位", width = 15)
    @ApiModelProperty(value = "货币单位")
	private String currencyUnit;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;
	/**是否启用(0-停用 1-启用)*/
	@Excel(name = "是否启用(0-停用 1-启用)", width = 15)
    @ApiModelProperty(value = "是否启用(0-停用 1-启用)")
	private Integer status;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**更新时间*/
    @ApiModelProperty(value = "更新时间")
	private Date updateTime;
}
