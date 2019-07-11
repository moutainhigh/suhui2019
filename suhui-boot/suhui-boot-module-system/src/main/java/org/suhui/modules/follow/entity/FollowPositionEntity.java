package org.suhui.modules.follow.entity;

import java.util.Date;

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
 * @Description: 关注职位
 * @Author: jeecg-boot
 * @Date:   2019-07-04
 * @Version: V1.0
 */
@Data
@TableName("zp_follow_position")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="zp_follow_position对象", description="关注职位")
public class FollowPositionEntity {
    
	/**用户ID*/
	@Excel(name = "用户ID", width = 15)
    @ApiModelProperty(value = "用户ID")
	private String userId;
	/**职位ID*/
	@Excel(name = "职位ID", width = 15)
    @ApiModelProperty(value = "职位ID")
	private Integer positionId;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
}
