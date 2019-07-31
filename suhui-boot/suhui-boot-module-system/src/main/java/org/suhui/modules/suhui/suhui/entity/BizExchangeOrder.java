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
 * @Description: 换汇记录表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("biz_exchange_order")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="biz_exchange_order对象", description="换汇记录表")
public class BizExchangeOrder {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**业务交易流水号(各业务保持唯一)*/
	@Excel(name = "业务交易流水号(各业务保持唯一)", width = 15)
    @ApiModelProperty(value = "业务交易流水号(各业务保持唯一)")
	private String tradeNo;
	/**由支付系统生成的唯一流水号*/
	@Excel(name = "由支付系统生成的唯一流水号", width = 15)
    @ApiModelProperty(value = "由支付系统生成的唯一流水号")
	private String bizExchangeNo;
	/**用户编号*/
	@Excel(name = "用户编号", width = 15)
    @ApiModelProperty(value = "用户编号")
	private String userNo;
	/**用户类型（1-乘客 2-企业）*/
	@Excel(name = "用户类型（1-乘客 2-企业）", width = 15)
    @ApiModelProperty(value = "用户类型（1-乘客 2-企业）")
	private Integer userType;
	/**换汇账户类型*/
	@Excel(name = "换汇账户类型", width = 15)
    @ApiModelProperty(value = "换汇账户类型")
	private Integer accountType;
	/**源货币金额*/
	@Excel(name = "源货币金额", width = 15)
    @ApiModelProperty(value = "源货币金额")
	private Integer sourceCurrency;
	/**目标货币金额*/
	@Excel(name = "目标货币金额", width = 15)
    @ApiModelProperty(value = "目标货币金额")
	private Integer targetCurrency;
	/**换汇编号，兑换以此为准*/
	@Excel(name = "换汇编号，兑换以此为准", width = 15)
    @ApiModelProperty(value = "换汇编号，兑换以此为准")
	private String rateCode;
	/**状态：1-冻结成功 2-换汇中 100-换汇成功 99-换汇失败*/
	@Excel(name = "状态：1-冻结成功 2-换汇中 100-换汇成功 99-换汇失败", width = 15)
    @ApiModelProperty(value = "状态：1-冻结成功 2-换汇中 100-换汇成功 99-换汇失败")
	private Integer status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;
	/**汇率同步时间*/
    @ApiModelProperty(value = "汇率同步时间")
	private Date exchangeTime;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**更新时间*/
    @ApiModelProperty(value = "更新时间")
	private Date updateTime;
}
