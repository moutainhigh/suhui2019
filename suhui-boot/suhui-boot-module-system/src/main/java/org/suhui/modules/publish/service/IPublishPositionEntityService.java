package org.suhui.modules.publish.service;

import org.suhui.modules.publish.entity.PublishPositionEntity;

import java.util.List;
import java.util.Map;

/**
 * @Description: 职位增删改查
 * @Author: jeecg-boot
 * @Date:   2019-07-03
 * @Version: V1.0
 */
public interface IPublishPositionEntityService {

    /**
     * 添加
     * @param publishPositionEntity
     */
    int save(PublishPositionEntity publishPositionEntity);

    /**
     * 修改
     * @param publishPositionEntity
     */
    int update(Map publishPositionEntity);

    /**
     * 查询
     */
    List<PublishPositionEntity> find(Map publishPositionEntity);

}