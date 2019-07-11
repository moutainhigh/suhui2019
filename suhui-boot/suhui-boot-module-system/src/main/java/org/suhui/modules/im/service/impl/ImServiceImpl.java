package org.suhui.modules.im.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.suhui.modules.im.entity.ImEntity;
import org.suhui.modules.im.mapper.ImMapper;
import org.suhui.modules.im.service.ImService;
import org.springframework.stereotype.Service;

@Service
public class ImServiceImpl extends ServiceImpl<ImMapper, ImEntity> implements ImService {

//    @Autowired
//    private ImMapper imDao;
//
//    /**
//     * 添加
//     */
//    public int save(ImEntity imEntity) {
//        return imDao.save(imEntity);
//    }
//
//	@Override
//	public void update(ImEntity imEntity) {
//		  imDao.update(imEntity);
//	}
}