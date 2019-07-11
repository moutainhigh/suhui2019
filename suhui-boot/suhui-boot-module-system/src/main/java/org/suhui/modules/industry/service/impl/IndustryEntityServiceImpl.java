package org.suhui.modules.industry.service.impl;

import org.suhui.modules.industry.entity.IndustryEntity;
import org.suhui.modules.industry.mapper.IndustryEntityMapper;
import org.suhui.modules.industry.service.IIndustryEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 行业查询接口
 * @Author: jeecg-boot
 * @Date:   2019-07-02
 * @Version: V1.0
 */
@Service
public class IndustryEntityServiceImpl extends ServiceImpl<IndustryEntityMapper, IndustryEntity> implements IIndustryEntityService {
    @Autowired
    private IndustryEntityMapper industryEntityMapper;

    @Override
    public List<IndustryEntity> selectById(String typeId, String id) {
        int type = Integer.parseInt(typeId);
        if(type == 0) {
            return this.industryEntityMapper.getIndustryList();
        } else if(type == 1) {
            return this.industryEntityMapper.getIndustryListById("");
        } else if(type > 1) {
            return this.industryEntityMapper.getIndustryListById(id);
        }
        return null;
    }
}
