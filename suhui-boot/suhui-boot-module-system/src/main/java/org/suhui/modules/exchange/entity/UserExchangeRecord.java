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
 * @Description: 用户的换汇记录表
 * @Author: jeecg-boot
 * @Date:   2019-07-18
 * @Version: V1.0
 */
@Data
@TableName("user_exchange_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="user_exchange_record对象", description="用户的换汇记录表")
public class UserExchangeRecord {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**换汇数量*/
	@Excel(name = "换汇数量", width = 15)
    @ApiModelProperty(value = "换汇数量")
	private java.math.BigDecimal amount;
	/**换汇人id*/
	@Excel(name = "换汇人id", width = 15)
    @ApiModelProperty(value = "换汇人id")
	private java.lang.String userId;
	/**源货币*/
	@Excel(name = "源货币", width = 15)
    @ApiModelProperty(value = "源货币")
	private java.lang.Integer sourceCurrency;
	/**目标货币*/
	@Excel(name = "目标货币", width = 15)
    @ApiModelProperty(value = "目标货币")
	private java.lang.Integer targetCurrency;
	/**换汇时候的汇率*/
	@Excel(name = "换汇时候的汇率", width = 15)
    @ApiModelProperty(value = "换汇时候的汇率")
	private java.math.BigDecimal currentRate;
	/**换汇后的人民币账户余额*/
	@Excel(name = "换汇后的人民币账户余额", width = 15)
    @ApiModelProperty(value = "换汇后的人民币账户余额")
	private java.math.BigDecimal rmbAmount;
	/**换汇后的美元账户余额*/
	@Excel(name = "换汇后的美元账户余额", width = 15)
    @ApiModelProperty(value = "换汇后的美元账户余额")
	private java.math.BigDecimal usAmount;
	/**换汇后的菲律宾账户余额*/
	@Excel(name = "换汇后的菲律宾账户余额", width = 15)
    @ApiModelProperty(value = "换汇后的菲律宾账户余额")
	private java.math.BigDecimal phAmount;
	/**换汇状态  0:冻结中   1:成功  2:失败*/
	@Excel(name = "换汇状态  0:冻结中   1:成功  2:失败", width = 15)
    @ApiModelProperty(value = "换汇状态  0:冻结中   1:成功  2:失败")
	private java.lang.Integer exchangeState;
}
