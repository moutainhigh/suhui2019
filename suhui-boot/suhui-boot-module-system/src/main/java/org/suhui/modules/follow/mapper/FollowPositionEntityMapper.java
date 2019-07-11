package org.suhui.modules.follow.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.suhui.modules.follow.entity.FollowPositionEntity;

/**
 * @Description: 关注职位
 * @Author: jeecg-boot
 * @Date:   2019-07-04
 * @Version: V1.0
 */
public interface FollowPositionEntityMapper extends BaseMapper<FollowPositionEntity> {

    int save(FollowPositionEntity followPositionEntity);

    int delete(FollowPositionEntity followPositionEntity);

    List<FollowPositionEntity> find(FollowPositionEntity followPositionEntity);

}