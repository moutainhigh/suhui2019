package org.suhui.modules.region.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.suhui.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 省市区查询接口
 * @Author: jeecg-boot
 * @Date: 2019-07-02
 * @Version: V1.0
 */
@Data
@TableName("region")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "region对象", description = "省市区查询接口")
public class RegionEntity extends JeecgEntity {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 名称
     */
    @Excel(name = "名称", width = 15)
    private String name;
    /**
     * 父级id
     */
    @Excel(name = "父级id", width = 15)
    private String parentId;
}
