package org.suhui.modules.publish.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.suhui.modules.publish.entity.PublishPositionEntity;

import java.util.List;
import java.util.Map;

/**
 * @Description: 职位增删改查
 * @Author: jeecg-boot
 * @Date:   2019-07-03
 * @Version: V1.0
 */
public interface PublishPositionEntityMapper extends BaseMapper<PublishPositionEntity> {

    int save(PublishPositionEntity publishPositionEntity);

    int update(Map publishPositionEntity);

    List<PublishPositionEntity> find(Map publishPositionEntity);

}