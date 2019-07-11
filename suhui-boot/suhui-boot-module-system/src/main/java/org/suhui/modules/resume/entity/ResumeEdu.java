package org.suhui.modules.resume.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 简历学历
 * @Author: jeecg-boot
 * @Date:   2019-07-04
 * @Version: V1.0
 */
@Data
@TableName("zp_resume_edu")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="zp_resume_edu对象", description="简历学历")
public class ResumeEdu implements Serializable{
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	/**基础信息id*/
	@Excel(name = "基础信息id", width = 15)
    @ApiModelProperty(value = "基础信息id")
	private String baseId;
	/**学校名称*/
	@Excel(name = "学校名称", width = 15)
    @ApiModelProperty(value = "学校名称")
	private String school;
	/**学历*/
	@Excel(name = "学历", width = 15)
    @ApiModelProperty(value = "学历")
	private String education;
	/**专业名称*/
	@Excel(name = "专业名称", width = 15)
    @ApiModelProperty(value = "专业名称")
	private String professional;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15)
    @ApiModelProperty(value = "开始时间")
	private String eduTimeStart;
	/**结束时间*/
	@Excel(name = "结束时间", width = 15)
    @ApiModelProperty(value = "结束时间")
	private String eduTimeEnd;
	/**在校经历*/
	@Excel(name = "在校经历", width = 15)
    @ApiModelProperty(value = "在校经历")
	private String association;
}
