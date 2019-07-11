package org.suhui.modules.position.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.suhui.modules.position.entity.PositionEntity;

/**
 * @Description: 职位查询接口
 * @Author: jeecg-boot
 * @Date:   2019-07-02
 * @Version: V1.0
 */
public interface PositionEntityMapper extends BaseMapper<PositionEntity> {

    List<PositionEntity> getPositionList();

    List<PositionEntity> getPositionListById(@Param("id") String id);

}
