package org.suhui.modules.demo.test.service;

import org.suhui.common.system.base.service.JeecgService;
import org.suhui.modules.demo.test.entity.JeecgDemo;

/**
 * @Description: jeecg 测试demo
 * @Author: jeecg-boot
 * @Date:  2018-12-29
 * @Version: V1.0
 */
public interface IJeecgDemoService extends JeecgService<JeecgDemo> {
	
	public void testTran();
	
	public JeecgDemo getByIdCacheable(String id);
}
