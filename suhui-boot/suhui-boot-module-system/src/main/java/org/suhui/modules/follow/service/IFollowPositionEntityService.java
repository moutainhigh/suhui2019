package org.suhui.modules.follow.service;

import org.suhui.modules.follow.entity.FollowPositionEntity;

import java.util.List;

/**
 * @Description: 关注职位
 * @Author: jeecg-boot
 * @Date:   2019-07-04
 * @Version: V1.0
 */
public interface IFollowPositionEntityService {

    /**
     * 添加
     * @param followPositionEntity
     */
    boolean save(FollowPositionEntity followPositionEntity);

    /**
     * 删除
     * @param followPositionEntity
     */
    boolean delete(FollowPositionEntity followPositionEntity);

    /**
     * 查询
     */
    List<FollowPositionEntity> find(FollowPositionEntity followPositionEntity);

}