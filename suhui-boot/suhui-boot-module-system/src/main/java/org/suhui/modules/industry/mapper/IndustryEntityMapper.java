package org.suhui.modules.industry.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.suhui.modules.industry.entity.IndustryEntity;

import java.util.List;

/**
 * @Description: 行业查询接口
 * @Author: jeecg-boot
 * @Date:   2019-07-02
 * @Version: V1.0
 */
public interface IndustryEntityMapper extends BaseMapper<IndustryEntity> {

    List<IndustryEntity> getIndustryList();

    List<IndustryEntity> getIndustryListById(@Param("id") String id);
}
