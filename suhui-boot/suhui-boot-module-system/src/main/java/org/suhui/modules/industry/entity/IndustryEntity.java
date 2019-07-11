package org.suhui.modules.industry.entity;

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
 * @Description: 行业查询接口
 * @Author: jeecg-boot
 * @Date:   2019-07-02
 * @Version: V1.0
 */
@Data
@TableName("industry")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="industry对象", description="行业查询接口")
public class IndustryEntity {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	/**name*/
	@Excel(name = "name", width = 15)
    @ApiModelProperty(value = "name")
	private String name;
	/**parentId*/
	@Excel(name = "parentId", width = 15)
    @ApiModelProperty(value = "parentId")
	private String parentId;
}
