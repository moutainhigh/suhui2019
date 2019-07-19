package org.suhui.modules.exchange.entity;

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

/**
 * @Description: 用户换汇预约记录表
 * @Author: jeecg-boot
 * @Date:   2019-07-17
 * @Version: V1.0
 */
@Data
@TableName("user_exchange_order")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="user_exchange_order对象", description="用户换汇预约记录表")
public class UserExchangeOrder {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**换汇数量*/
	@Excel(name = "换汇数量", width = 15)
    @ApiModelProperty(value = "换汇数量")
	private java.math.BigDecimal amount;
	/**源货币*/
	@Excel(name = "源货币", width = 15)
    @ApiModelProperty(value = "源货币")
	private java.lang.Integer sourceCurrency;
	/**目标货币*/
	@Excel(name = "目标货币", width = 15)
    @ApiModelProperty(value = "目标货币")
	private java.lang.Integer targetCurrency;
	/**预约处理时间*/
	@Excel(name = "预约处理时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "预约处理时间")
	private java.util.Date orderTime;
	/**预约状态  0:客户经理未联系   1:客户经理已联系  2:处理中  3:已完成
*/
	@Excel(name = "预约状态  0:客户经理未联系   1:客户经理已联系  2:处理中  3:已完成 ", width = 15)
    @ApiModelProperty(value = "预约状态  0:客户经理未联系   1:客户经理已联系  2:处理中  3:已完成 ")
	private java.lang.Integer orderState;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**处理人id*/
	@Excel(name = "处理人id", width = 15)
    @ApiModelProperty(value = "处理人id")
	private java.lang.String handleId;
	/**换汇地址*/
	@Excel(name = "换汇地址", width = 15)
    @ApiModelProperty(value = "换汇地址")
	private java.lang.String address;
	/**第二联系电话*/
	@Excel(name = "第二联系电话", width = 15)
    @ApiModelProperty(value = "第二联系电话")
	private java.lang.String secondPhone;
	/**处理预约换汇管理员id*/
	@Excel(name = "处理预约换汇管理员id", width = 15)
    @ApiModelProperty(value = "处理预约换汇管理员id")
	private java.lang.String supervisorId;
}
