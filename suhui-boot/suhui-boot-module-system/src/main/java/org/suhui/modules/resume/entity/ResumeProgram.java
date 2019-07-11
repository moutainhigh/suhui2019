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
 * @Description: 项目经验
 * @Author: jeecg-boot
 * @Date:   2019-07-04
 * @Version: V1.0
 */
@Data
@TableName("zp_resume_program")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="zp_resume_program对象", description="项目经验")
public class ResumeProgram implements Serializable{
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	/**基础信息id*/
	@Excel(name = "基础信息id", width = 15)
    @ApiModelProperty(value = "基础信息id")
	private String baseId;
	/**programName*/
	@Excel(name = "programName", width = 15)
    @ApiModelProperty(value = "programName")
	private String programName;
	/**项目角色*/
	@Excel(name = "项目角色", width = 15)
    @ApiModelProperty(value = "项目角色")
	private String programRole;
	/**项目类型*/
	@Excel(name = "项目类型", width = 15)
    @ApiModelProperty(value = "项目类型")
	private String programLink;
	/**项目开始时间*/
	@Excel(name = "项目开始时间", width = 15)
    @ApiModelProperty(value = "项目开始时间")
	private String programTimeStart;
	/**项目结束时间*/
	@Excel(name = "项目结束时间", width = 15)
    @ApiModelProperty(value = "项目结束时间")
	private String programTimeStop;
	/**项目描述*/
	@Excel(name = "项目描述", width = 15)
    @ApiModelProperty(value = "项目描述")
	private String programDesc;
	/**工作业绩*/
	@Excel(name = "工作业绩", width = 15)
    @ApiModelProperty(value = "工作业绩")
	private String performance;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
	private Date createTime;
}
