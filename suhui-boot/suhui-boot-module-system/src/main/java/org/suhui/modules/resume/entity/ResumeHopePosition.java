package org.suhui.modules.resume.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 期望职位
 * @Author: jeecg-boot
 * @Date:   2019-07-04
 * @Version: V1.0
 */
@Data
@TableName("zp_resume_hope_position")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="zp_resume_hope_position对象", description="期望职位")
public class ResumeHopePosition implements Serializable{
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	/**基础id*/
	@Excel(name = "基础id", width = 15)
    @ApiModelProperty(value = "基础id")
	private String baseId;
	/**期望职位*/
	@Excel(name = "期望职位", width = 15)
    @ApiModelProperty(value = "期望职位")
	private String hopePosition;
	/**薪资下限*/
	@Excel(name = "薪资下限", width = 15)
    @ApiModelProperty(value = "薪资下限")
	private String salaryLow;
	/**薪资上限*/
	@Excel(name = "薪资上限", width = 15)
    @ApiModelProperty(value = "薪资上限")
	private String salaryTop;
	/**行业*/
	@Excel(name = "行业", width = 15)
    @ApiModelProperty(value = "行业")
	private String industry;
	/**城市*/
	@Excel(name = "城市", width = 15)
    @ApiModelProperty(value = "城市")
	private String city;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
}
