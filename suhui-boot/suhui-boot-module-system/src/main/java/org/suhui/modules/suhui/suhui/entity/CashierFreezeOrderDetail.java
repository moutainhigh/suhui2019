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
 * @Description: 冻结详情表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("cashier_freeze_order_detail")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="cashier_freeze_order_detail对象", description="冻结详情表")
public class CashierFreezeOrderDetail {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**由支付系统生成的唯一流水号*/
	@Excel(name = "由支付系统生成的唯一流水号", width = 15)
    @ApiModelProperty(value = "由支付系统生成的唯一流水号")
	private String bizFreezeNo;
	/**用户编号*/
	@Excel(name = "用户编号", width = 15)
    @ApiModelProperty(value = "用户编号")
	private String userNo;
	/**用户类型（1-乘客 2-企业）*/
	@Excel(name = "用户类型（1-乘客 2-企业）", width = 15)
    @ApiModelProperty(value = "用户类型（1-乘客 2-企业）")
	private Integer userType;
	/**支付账户类型（1：渠道账户    2：本金账户    3：赠额账户    4：授信账户)*/
	@Excel(name = "支付账户类型（1：渠道账户    2：本金账户    3：赠额账户    4：授信账户)", width = 15)
    @ApiModelProperty(value = "支付账户类型（1：渠道账户    2：本金账户    3：赠额账户    4：授信账户)")
	private Integer payAccountType;
	/**冻结账户账号*/
	@Excel(name = "冻结账户账号", width = 15)
    @ApiModelProperty(value = "冻结账户账号")
	private String payAccount;
	/**冻结金额*/
	@Excel(name = "冻结金额", width = 15)
    @ApiModelProperty(value = "冻结金额")
	private Integer freezeAmount;
	/**解冻时间*/
    @ApiModelProperty(value = "解冻时间")
	private Date unfreezeTime;
	/**子状态（1-冻结状态  2-解冻状态）*/
	@Excel(name = "子状态（1-冻结状态  2-解冻状态）", width = 15)
    @ApiModelProperty(value = "子状态（1-冻结状态  2-解冻状态）")
	private Integer subStatus;
	/**创建时间*/
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**更新时间*/
    @ApiModelProperty(value = "更新时间")
	private Date updateTime;
}
