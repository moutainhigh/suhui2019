package org.suhui.modules.region.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.suhui.modules.region.entity.RegionEntity;

import java.util.List;

/**
 * @Description: 省市区查询接口
 * @Author: jeecg-boot
 * @Date: 2019-07-02
 * @Version: V1.0
 */
public interface IRegionEntityService extends IService<RegionEntity> {
    List<RegionEntity> selectById(String typeId, String id);
}
