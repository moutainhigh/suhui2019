package org.suhui.modules.position.service.impl;

import org.suhui.modules.position.entity.PositionEntity;
import org.suhui.modules.position.mapper.PositionEntityMapper;
import org.suhui.modules.position.service.IPositionEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 职位查询接口
 * @Author: jeecg-boot
 * @Date:   2019-07-02
 * @Version: V1.0
 */
@Service
public class PositionEntityServiceImpl extends ServiceImpl<PositionEntityMapper, PositionEntity> implements IPositionEntityService {

    @Autowired
    private PositionEntityMapper positionEntityMapper;

    @Override
    public List<PositionEntity> selectById(String typeId,String id) {
        int type = Integer.parseInt(typeId);
        if(type == 0) {
            return this.positionEntityMapper.getPositionList();
        } else if(type == 1) {
            return this.positionEntityMapper.getPositionListById("");
        } else if(type > 1) {
            return this.positionEntityMapper.getPositionListById(id);
        }
        return null;
    }
}