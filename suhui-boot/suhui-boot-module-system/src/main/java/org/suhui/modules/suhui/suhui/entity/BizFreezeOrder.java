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
 * @Description: 冻结记录表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("biz_freeze_order")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="biz_freeze_order对象", description="冻结记录表")
public class BizFreezeOrder {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**业务交易流水号(各业务保持唯一)*/
	@Excel(name = "业务交易流水号(各业务保持唯一)", width = 15)
    @ApiModelProperty(value = "业务交易流水号(各业务保持唯一)")
	private String tradeNo;
	/**由支付系统生成的唯一流水号*/
	@Excel(name = "由支付系统生成的唯一流水号", width = 15)
    @ApiModelProperty(value = "由支付系统生成的唯一流水号")
	private String bizFreezeNo;
	/**总冻结金额*/
	@Excel(name = "总冻结金额", width = 15)
    @ApiModelProperty(value = "总冻结金额")
	private Integer totalFreezeAmount;
	/**冻结类型，枚举维护，1-提现冻结*/
	@Excel(name = "冻结类型，枚举维护，1-提现冻结", width = 15)
    @ApiModelProperty(value = "冻结类型，枚举维护，1-提现冻结")
	private String freezeType;
	/**冻结时间*/
    @ApiModelProperty(value = "冻结时间")
	private Date freezeTime;
	/**解冻时间*/
    @ApiModelProperty(value = "解冻时间")
	private Date unfreezeTime;
	/**状态（1：冻结 2：解冻）*/
	@Excel(name = "状态（1：冻结 2：解冻）", width = 15)
    @ApiModelProperty(value = "状态（1：冻结 2：解冻）")
	private Integer status;
	/**冻结备注信息*/
	@Excel(name = "冻结备注信息", width = 15)
    @ApiModelProperty(value = "冻结备注信息")
	private String remark;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**更新时间*/
    @ApiModelProperty(value = "更新时间")
	private Date updateTime;
	/**批次编号*/
	@Excel(name = "批次编号", width = 15)
    @ApiModelProperty(value = "批次编号")
	private String batchNo;
	/**是否批量冻结*/
	@Excel(name = "是否批量冻结", width = 15)
    @ApiModelProperty(value = "是否批量冻结")
	private Integer isBatch;
}
