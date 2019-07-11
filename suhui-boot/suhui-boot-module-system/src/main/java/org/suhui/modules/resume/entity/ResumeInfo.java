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
 * @Description: 简历详情
 * @Author: jeecg-boot
 * @Date:   2019-07-03
 * @Version: V1.0
 */
@Data
@TableName("zp_resume_base")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="zp_resume_base对象", description="简历详情")
public class ResumeInfo implements Serializable{
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
	private String name;
	/**性别*/
	@Excel(name = "性别", width = 15)
    @ApiModelProperty(value = "性别")
	private String sex;
	/**生日*/
	@Excel(name = "生日", width = 15)
    @ApiModelProperty(value = "生日")
	private String birthday;
	/**电话*/
	@Excel(name = "电话", width = 15)
    @ApiModelProperty(value = "电话")
	private String phone;
	/**求职状态*/
	@Excel(name = "求职状态", width = 15)
    @ApiModelProperty(value = "求职状态")
	private String jobHutState;
	/**开始工作时间*/
	@Excel(name = "开始工作时间", width = 15)
    @ApiModelProperty(value = "开始工作时间")
	private String startJobTime;
	/**微信号*/
	@Excel(name = "微信号", width = 15)
    @ApiModelProperty(value = "微信号")
	private String wxNumber;
	/**邮箱*/
	@Excel(name = "邮箱", width = 15)
    @ApiModelProperty(value = "邮箱")
	private String email;
	/**个人优势*/
	@Excel(name = "个人优势", width = 15)
    @ApiModelProperty(value = "个人优势")
	private String personalAdvantage;
}
