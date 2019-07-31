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
 * @Description: 汇率记录表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("pay_currency_rate")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="pay_currency_rate对象", description="汇率记录表")
public class PayCurrencyRate {
    
	/**自增ID*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "自增ID")
	private Integer id;
	/**换汇编号，兑换以此为准*/
	@Excel(name = "换汇编号，兑换以此为准", width = 15)
    @ApiModelProperty(value = "换汇编号，兑换以此为准")
	private String rateCode;
	/**换汇名称*/
	@Excel(name = "换汇名称", width = 15)
    @ApiModelProperty(value = "换汇名称")
	private String rateName;
	/**当前汇率，用10000表示1*/
	@Excel(name = "当前汇率，用10000表示1", width = 15)
    @ApiModelProperty(value = "当前汇率，用10000表示1")
	private Integer rateNow;
	/**是否启用(0-停用 1-启用)*/
	@Excel(name = "是否启用(0-停用 1-启用)", width = 15)
    @ApiModelProperty(value = "是否启用(0-停用 1-启用)")
	private Integer status;
	/**源货币编号*/
	@Excel(name = "源货币编号", width = 15)
    @ApiModelProperty(value = "源货币编号")
	private String sourceCurrencyCode;
	/**目标货币编号*/
	@Excel(name = "目标货币编号", width = 15)
    @ApiModelProperty(value = "目标货币编号")
	private String targetCurrencyCode;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;
	/**汇率同步时间*/
    @ApiModelProperty(value = "汇率同步时间")
	private Date rateTime;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**更新时间*/
    @ApiModelProperty(value = "更新时间")
	private Date updateTime;
}
