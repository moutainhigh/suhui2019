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
 * @Description: 志愿者经历
 * @Author: jeecg-boot
 * @Date:   2019-07-04
 * @Version: V1.0
 */
@Data
@TableName("zp_resume_volunteer")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="zp_resume_volunteer对象", description="志愿者经历")
public class ResumeVolunteer implements Serializable{
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	/**基础信息id*/
	@Excel(name = "基础信息id", width = 15)
    @ApiModelProperty(value = "基础信息id")
	private String baseId;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
	private String programName;
	/**服务器总时长*/
	@Excel(name = "服务器总时长", width = 15)
    @ApiModelProperty(value = "服务器总时长")
	private String servertime;
	/**项目开始时间*/
	@Excel(name = "项目开始时间", width = 15)
    @ApiModelProperty(value = "项目开始时间")
	private String programTimeStart;
	/**项目结束时间*/
	@Excel(name = "项目结束时间", width = 15)
    @ApiModelProperty(value = "项目结束时间")
	private String programTimeEnd;
	/**项目描述*/
	@Excel(name = "项目描述", width = 15)
    @ApiModelProperty(value = "项目描述")
	private String programDesc;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
	private Date createTime;
}
