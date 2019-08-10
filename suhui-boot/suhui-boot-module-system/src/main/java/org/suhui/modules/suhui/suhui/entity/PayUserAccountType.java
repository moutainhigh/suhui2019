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
 * @Description: 用户类型与账户类型关联表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("pay_user_account_type")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="pay_user_account_type对象", description="用户类型与账户类型关联表")
public class PayUserAccountType {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**用户类型 1-个人 2-企业*/
	@Excel(name = "用户类型 1-个人 2-企业", width = 15)
    @ApiModelProperty(value = "用户类型 1-个人 2-企业")
	private Integer userType;
	/**账户类型编码*/
	@Excel(name = "账户类型编码", width = 15)
    @ApiModelProperty(value = "账户类型编码")
	private Integer accountTypeCode;
	/**账户类型名称*/
	@Excel(name = "账户类型名称", width = 15)
    @ApiModelProperty(value = "账户类型名称")
	private String accountTypeName;
	/**状态 0-默认 1-有效 2-无效*/
	@Excel(name = "状态 0-默认 1-有效 2-无效", width = 15)
    @ApiModelProperty(value = "状态 0-默认 1-有效 2-无效")
	private Integer status;
	/**remark*/
	@Excel(name = "remark", width = 15)
    @ApiModelProperty(value = "remark")
	private String remark;
	/**createTime*/
    @ApiModelProperty(value = "createTime")
	private Date createTime;
	/**updateTime*/
    @ApiModelProperty(value = "updateTime")
	private Date updateTime;
}
