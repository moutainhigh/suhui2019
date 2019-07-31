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
 * @Description: 转账记录表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("biz_transfer_order")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="biz_transfer_order对象", description="转账记录表")
public class BizTransferOrder {
    
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
	private String bizTransferNo;
	/**转出用户编号*/
	@Excel(name = "转出用户编号", width = 15)
    @ApiModelProperty(value = "转出用户编号")
	private String transferUserNo;
	/**转出用户类型*/
	@Excel(name = "转出用户类型", width = 15)
    @ApiModelProperty(value = "转出用户类型")
	private Integer transferUserType;
	/**转账总金额*/
	@Excel(name = "转账总金额", width = 15)
    @ApiModelProperty(value = "转账总金额")
	private Integer transferAmount;
	/**转账时间*/
    @ApiModelProperty(value = "转账时间")
	private Date transferTime;
	/**状态（1-发起转账  2-转账中  99-转账失败 100-转账成功）*/
	@Excel(name = "状态（1-发起转账  2-转账中  99-转账失败 100-转账成功）", width = 15)
    @ApiModelProperty(value = "状态（1-发起转账  2-转账中  99-转账失败 100-转账成功）")
	private Integer status;
	/**转账说明*/
	@Excel(name = "转账说明", width = 15)
    @ApiModelProperty(value = "转账说明")
	private String remark;
	/**createTime*/
    @ApiModelProperty(value = "createTime")
	private Date createTime;
	/**updateTime*/
    @ApiModelProperty(value = "updateTime")
	private Date updateTime;
}
