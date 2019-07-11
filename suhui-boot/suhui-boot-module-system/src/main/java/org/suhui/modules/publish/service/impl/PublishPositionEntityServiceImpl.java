package org.suhui.modules.publish.service.impl;

import org.suhui.modules.publish.entity.PublishPositionEntity;
import org.suhui.modules.publish.mapper.PublishPositionEntityMapper;
import org.suhui.modules.publish.service.IPublishPositionEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 职位增删改查
 * @Author: jeecg-boot
 * @Date:   2019-07-03
 * @Version: V1.0
 */
@Service
public class PublishPositionEntityServiceImpl implements IPublishPositionEntityService {

    @Autowired
    private PublishPositionEntityMapper publishPositionEntityMapper;

    /**
     * 添加
     * @param publishPositionEntity
     */
    public int save(PublishPositionEntity publishPositionEntity){
        return publishPositionEntityMapper.save(publishPositionEntity);
    }

    /**
     * 修改
     * @param publishPositionEntity
     */
    public int update(Map publishPositionEntity){
        return publishPositionEntityMapper.update(publishPositionEntity);
    }

    /**
     * 查询
     * @param publishPositionEntity
     */
    public List<PublishPositionEntity> find(Map publishPositionEntity){
        List<PublishPositionEntity> publish = publishPositionEntityMapper.find(publishPositionEntity);
        return publish;
    }
}
