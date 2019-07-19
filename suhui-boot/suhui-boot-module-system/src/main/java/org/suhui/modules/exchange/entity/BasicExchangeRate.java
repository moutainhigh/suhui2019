package org.suhui.modules.exchange.entity;

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
 * @Description: 换汇产品
 * @Author: jeecg-boot
 * @Date:   2019-07-17
 * @Version: V1.0
 */
@Data
@TableName("basic_exchange_rate")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="basic_exchange_rate对象", description="换汇产品")
public class BasicExchangeRate {
    
	/**rateId*/
	@Excel(name = "rateId", width = 15)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**换汇的产品名字*/
	@Excel(name = "换汇的产品名字", width = 15)
    @ApiModelProperty(value = "换汇的产品名字")
	private java.lang.String rateName;
	/**源货币*/
	@Excel(name = "源货币", width = 15)
    @ApiModelProperty(value = "源货币")
	private java.lang.Integer sourceCurrency;
	/**目标货币*/
	@Excel(name = "目标货币", width = 15)
    @ApiModelProperty(value = "目标货币")
	private java.lang.Integer targetCurrency;
	/**当前汇率*/
	@Excel(name = "当前汇率", width = 15)
    @ApiModelProperty(value = "当前汇率")
	private java.math.BigDecimal liveRate;
	/**换汇备注*/
	@Excel(name = "换汇备注", width = 15)
    @ApiModelProperty(value = "换汇备注")
	private java.lang.String rateNote;
}
