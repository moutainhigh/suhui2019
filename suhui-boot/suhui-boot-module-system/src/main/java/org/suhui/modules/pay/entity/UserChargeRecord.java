package org.suhui.modules.pay.entity;

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
 * @Description: 充值
 * @Author: jeecg-boot
 * @Date:   2019-07-14
 * @Version: V1.0
 */
@Data
@TableName("user_charge_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="user_charge_record对象", description="充值")
public class UserChargeRecord {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.Integer id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
	private java.lang.Integer userId;
	/**充值时间*/
	@Excel(name = "充值时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "充值时间")
	private java.util.Date createTime;
	/**充值金额*/
	@Excel(name = "充值金额", width = 15)
    @ApiModelProperty(value = "充值金额")
	private java.math.BigDecimal amount;
	/**充值的货币类型*/
	@Excel(name = "充值的货币类型", width = 15)
    @ApiModelProperty(value = "充值的货币类型")
	private java.lang.Integer currencyType;
	/**充值类型 1:支付宝  2:银行卡...*/
	@Excel(name = "充值类型 1:支付宝  2:银行卡...", width = 15)
    @ApiModelProperty(value = "充值类型 1:支付宝  2:银行卡...")
	private java.lang.Integer chargeType;
	/**充值成功状态  0:充值中  1:成功  2:失败*/
	@Excel(name = "充值成功状态  0:充值中  1:成功  2:失败", width = 15)
    @ApiModelProperty(value = "充值成功状态  0:充值中  1:成功  2:失败")
	private java.lang.Integer chargeState;
	/**充值后的人民币账户余额*/
	@Excel(name = "充值后的人民币账户余额", width = 15)
    @ApiModelProperty(value = "充值后的人民币账户余额")
	private java.math.BigDecimal rmbAmount;
	/**充值后的美元账户余额*/
	@Excel(name = "充值后的美元账户余额", width = 15)
    @ApiModelProperty(value = "充值后的美元账户余额")
	private java.math.BigDecimal usAmount;
	/**充值后的菲律宾账户余额*/
	@Excel(name = "充值后的菲律宾账户余额", width = 15)
    @ApiModelProperty(value = "充值后的菲律宾账户余额")
	private java.math.BigDecimal phAmount;
}
