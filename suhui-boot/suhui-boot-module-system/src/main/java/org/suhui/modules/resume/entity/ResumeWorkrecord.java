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
@TableName("zp_resume_workrecord")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="zp_resume_workrecord对象", description="志愿者经历")
public class ResumeWorkrecord implements Serializable{
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	/**基础信息id*/
	@Excel(name = "基础信息id", width = 15)
    @ApiModelProperty(value = "基础信息id")
	private String baseId;
	/**公司名称*/
	@Excel(name = "公司名称", width = 15)
    @ApiModelProperty(value = "公司名称")
	private String company;
	/**所属行业*/
	@Excel(name = "所属行业", width = 15)
    @ApiModelProperty(value = "所属行业")
	private String industry;
	/**部门*/
	@Excel(name = "部门", width = 15)
    @ApiModelProperty(value = "部门")
	private String department;
	/**职位名称*/
	@Excel(name = "职位名称", width = 15)
    @ApiModelProperty(value = "职位名称")
	private String positionName;
	/**职位类型*/
	@Excel(name = "职位类型", width = 15)
    @ApiModelProperty(value = "职位类型")
	private String positionType;
	/**在职时间_开始*/
	@Excel(name = "在职时间_开始", width = 15)
    @ApiModelProperty(value = "在职时间_开始")
	private String onJobTimeStart;
	/**在职时间_结束*/
	@Excel(name = "在职时间_结束", width = 15)
    @ApiModelProperty(value = "在职时间_结束")
	private String onJobTimeEnd;
	/**技能标签*/
	@Excel(name = "技能标签", width = 15)
    @ApiModelProperty(value = "技能标签")
	private String skillLabel;
	/**工作内容*/
	@Excel(name = "工作内容", width = 15)
    @ApiModelProperty(value = "工作内容")
	private String jobContent;
	/**工作业绩*/
	@Excel(name = "工作业绩", width = 15)
    @ApiModelProperty(value = "工作业绩")
	private String performance;
	/**是否对公司隐藏简历*/
	@Excel(name = "是否对公司隐藏简历", width = 15)
    @ApiModelProperty(value = "是否对公司隐藏简历")
	private String hidenSf;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
}
