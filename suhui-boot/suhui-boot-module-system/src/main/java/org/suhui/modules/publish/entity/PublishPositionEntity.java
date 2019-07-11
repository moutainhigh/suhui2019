package org.suhui.modules.publish.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 职位增删改查
 * @Author: jeecg-boot
 * @Date:   2019-07-03
 * @Version: V1.0
 */
@Data
@TableName("zp_publish_position")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="zp_publish_position对象", description="职位增删改查")
public class PublishPositionEntity {
    
	/**职位ID*/
	@Excel(name = "职位ID", width = 15)
    @ApiModelProperty(value = "职位ID")
	private Integer positionId;
	/**职位名称*/
	@Excel(name = "职位名称", width = 15)
    @ApiModelProperty(value = "职位名称")
	private String positionName;
	/**经验要求*/
	@Excel(name = "经验要求", width = 15)
    @ApiModelProperty(value = "经验要求")
	private Integer experience;
	/**学历*/
	@Excel(name = "学历", width = 15)
    @ApiModelProperty(value = "学历")
	private Integer education;
	/**薪资要求最低*/
	@Excel(name = "薪资要求最低", width = 15)
    @ApiModelProperty(value = "薪资要求最低")
	private Integer salaryLow;
	/**薪资要求最高*/
	@Excel(name = "薪资要求最高", width = 15)
    @ApiModelProperty(value = "薪资要求最高")
	private Integer salaryTall;
	/**月份*/
	@Excel(name = "月份", width = 15)
    @ApiModelProperty(value = "月份")
	private Integer month;
	/**奖金绩效*/
	@Excel(name = "奖金绩效", width = 15)
    @ApiModelProperty(value = "奖金绩效")
	private Object performance;
	/**职位描述*/
	@Excel(name = "职位描述", width = 15)
    @ApiModelProperty(value = "职位描述")
	private Object positionDesc;
	/**工作地点*/
	@Excel(name = "工作地点", width = 15)
    @ApiModelProperty(value = "工作地点")
	private String workPlace;
}
