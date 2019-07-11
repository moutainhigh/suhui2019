package org.suhui.modules.region.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.suhui.modules.region.entity.RegionEntity;
import org.suhui.modules.region.mapper.RegionEntityMapper;
import org.suhui.modules.region.service.IRegionEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 省市区查询接口
 * @Author: jeecg-boot
 * @Date: 2019-07-02
 * @Version: V1.0
 */
@Service
public class RegionEntityServiceImpl extends ServiceImpl<RegionEntityMapper, RegionEntity> implements IRegionEntityService {
    @Autowired
    private RegionEntityMapper regionEntityMapper;

    @Override
    public List<RegionEntity> selectById(String typeId, String id) {
        int type = Integer.parseInt(typeId);
        if (type == 0) {//返回所有数据
            return this.regionEntityMapper.getRegionList();
        } else if (type == 1) {//返回所有省份
            return this.regionEntityMapper.getRegionListById("");
        } else if (type > 1) {
            return this.regionEntityMapper.getRegionListById(id);
        }
        return null;
    }
}
