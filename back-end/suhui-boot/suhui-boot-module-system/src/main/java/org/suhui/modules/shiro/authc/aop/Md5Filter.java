package org.suhui.modules.shiro.authc.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 类说明：
 *
 * @author: 蔡珊珊
 * @create: 2020-04-16 17:25
 */
@Slf4j
@Aspect
@Component
public class Md5Filter {
    /**
     * 方法执行前的通知
     */
    @Before("execution(* org.suhui.modules.toB.controller.OrderMainController.createPaymentOrder(..))")
    public void beforeInvoke(){
        log.debug("方法执行前");
    }

    /**
     * 方法执行后的通知
     */
    @After("execution(* org.suhui.modules.toB.controller.OrderMainController.createPaymentOrder(..))")
    public void afterInvoke(){
        log.debug("方法执行后");
    }

    /**
     * 方法执行返回后的通知
     */
    @AfterReturning("execution(* org.suhui.modules.toB.controller.OrderMainController.createPaymentOrder(..))")
    public void afterReturning(){
        log.debug("==================方法执行完成");
    }

    /**
     * 方法抛出异常的通知
     */
    @AfterThrowing("execution(* org.suhui.modules.toB.controller.OrderMainController.createPaymentOrder(..))")
    public void afterThrowing(){
        log.debug("==================方法执行报错");
    }
}
