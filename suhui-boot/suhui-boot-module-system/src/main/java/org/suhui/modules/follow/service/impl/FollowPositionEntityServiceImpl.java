package org.suhui.modules.follow.service.impl;

import org.suhui.modules.follow.entity.FollowPositionEntity;
import org.suhui.modules.follow.mapper.FollowPositionEntityMapper;
import org.suhui.modules.follow.service.IFollowPositionEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 关注职位
 * @Author: jeecg-boot
 * @Date:   2019-07-04
 * @Version: V1.0
 */
@Service
public class FollowPositionEntityServiceImpl implements IFollowPositionEntityService {

    @Autowired
    private FollowPositionEntityMapper followPositionEntityMapper;

    /**
     * 添加
     * @param followPositionEntity
     */
    public boolean save(FollowPositionEntity followPositionEntity){
        boolean flag = false;
        int ok = followPositionEntityMapper.save(followPositionEntity);
        if(ok > 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 删除
     * @param followPositionEntity
     */
    public boolean delete(FollowPositionEntity followPositionEntity){
        boolean flag = false;
        int ok = followPositionEntityMapper.delete(followPositionEntity);
        if(ok > 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 查询
     * @param followPositionEntity
     */
    public List<FollowPositionEntity> find(FollowPositionEntity followPositionEntity){
        List<FollowPositionEntity> publish = followPositionEntityMapper.find(followPositionEntity);
        return publish;
    }
}