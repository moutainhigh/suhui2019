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
 * @Description: 用户身份表
 * @Author: jeecg-boot
 * @Date:   2019-07-30
 * @Version: V1.0
 */
@Data
@TableName("pay_identity_info")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="pay_identity_info对象", description="用户身份表")
public class PayIdentityInfo {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**身份编号*/
	@Excel(name = "身份编号", width = 15)
    @ApiModelProperty(value = "身份编号")
	private Integer identityNo;
	/**身份类型 0-默认 1-个人身份 2-企业身份*/
	@Excel(name = "身份类型 0-默认 1-个人身份 2-企业身份", width = 15)
    @ApiModelProperty(value = "身份类型 0-默认 1-个人身份 2-企业身份")
	private Integer identityType;
	/**用户编号*/
	@Excel(name = "用户编号", width = 15)
    @ApiModelProperty(value = "用户编号")
	private String userNo;
	/**用户类型 0-默认 1-个人 2-企业*/
	@Excel(name = "用户类型 0-默认 1-个人 2-企业", width = 15)
    @ApiModelProperty(value = "用户类型 0-默认 1-个人 2-企业")
	private Integer userType;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private String userName;
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
