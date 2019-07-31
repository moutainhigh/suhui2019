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
 * @Description: 提现订单表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("biz_withdraw_order")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="biz_withdraw_order对象", description="提现订单表")
public class BizWithDrawOrder {
    
	/**自增ID*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "自增ID")
	private Integer id;
	/**业务交易流水号(各业务保持唯一)*/
	@Excel(name = "业务交易流水号(各业务保持唯一)", width = 15)
    @ApiModelProperty(value = "业务交易流水号(各业务保持唯一)")
	private String tradeNo;
	/**由支付系统生成的唯一流水号*/
	@Excel(name = "由支付系统生成的唯一流水号", width = 15)
    @ApiModelProperty(value = "由支付系统生成的唯一流水号")
	private String bizWithdrawNo;
	/**用户编号*/
	@Excel(name = "用户编号", width = 15)
    @ApiModelProperty(value = "用户编号")
	private String userNo;
	/**用户类型（1-个人 2-企业）*/
	@Excel(name = "用户类型（1-个人 2-企业）", width = 15)
    @ApiModelProperty(value = "用户类型（1-个人 2-企业）")
	private Integer userType;
	/**账户类型*/
	@Excel(name = "账户类型", width = 15)
    @ApiModelProperty(value = "账户类型")
	private Integer accountType;
	/**提现渠道类型*/
	@Excel(name = "提现渠道类型", width = 15)
    @ApiModelProperty(value = "提现渠道类型")
	private Integer withdrawChannelType;
	/**提现金额*/
	@Excel(name = "提现金额", width = 15)
    @ApiModelProperty(value = "提现金额")
	private Integer amount;
	/**状态：1-冻结成功 2-提现中 100-提现成功 99-提现失败*/
	@Excel(name = "状态：1-冻结成功 2-提现中 100-提现成功 99-提现失败", width = 15)
    @ApiModelProperty(value = "状态：1-冻结成功 2-提现中 100-提现成功 99-提现失败")
	private Integer status;
	/**用户提现渠道账户信息，json格式*/
	@Excel(name = "用户提现渠道账户信息，json格式", width = 15)
    @ApiModelProperty(value = "用户提现渠道账户信息，json格式")
	private String userChannelInfo;
	/**提现结果信息,一般保存错误编码,json格式*/
	@Excel(name = "提现结果信息,一般保存错误编码,json格式", width = 15)
    @ApiModelProperty(value = "提现结果信息,一般保存错误编码,json格式")
	private String resultInfo;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;
	/**提现时间*/
    @ApiModelProperty(value = "提现时间")
	private Date withdrawTime;
	/**确认时间*/
    @ApiModelProperty(value = "确认时间")
	private Date affirmTime;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**更新时间*/
    @ApiModelProperty(value = "更新时间")
	private Date updateTime;
}
