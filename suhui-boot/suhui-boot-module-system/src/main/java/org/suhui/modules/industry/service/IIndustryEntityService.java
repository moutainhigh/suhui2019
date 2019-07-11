package org.suhui.modules.industry.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.suhui.modules.industry.entity.IndustryEntity;

import java.util.List;

/**
 * @Description: 行业查询接口
 * @Author: jeecg-boot
 * @Date:   2019-07-02
 * @Version: V1.0
 */
public interface IIndustryEntityService extends IService<IndustryEntity> {

    List<IndustryEntity> selectById(String typeId, String id);

}
