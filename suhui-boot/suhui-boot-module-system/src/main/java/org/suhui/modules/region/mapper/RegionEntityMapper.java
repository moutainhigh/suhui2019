package org.suhui.modules.region.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.suhui.modules.region.entity.RegionEntity;

import java.util.List;

/**
 * @Description: 省市区查询接口
 * @Author: jeecg-boot
 * @Date: 2019-07-02
 * @Version: V1.0
 */
public interface RegionEntityMapper extends BaseMapper<RegionEntity> {

    List<RegionEntity> getRegionList();

    List<RegionEntity> getRegionListById(String id);
}
