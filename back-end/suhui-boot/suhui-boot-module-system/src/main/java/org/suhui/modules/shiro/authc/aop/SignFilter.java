package org.suhui.modules.shiro.authc.aop;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.suhui.common.util.MD5Util;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

/**
 * 类说明：MD5验签
 *
 * @author: 蔡珊珊
 * @create: 2020-04-16 17:25
 */
@Slf4j
@Aspect
@Component
public class SignFilter {

    @Value("${appSecret}")
    private String appSecret;

    @Autowired
    HttpServletRequest request; //这里可以获取到request

    /**
     * 方法执行前的通知
     */
    @Before("execution(* org.suhui.modules.toB.controller.OrderMainController.createPaymentOrder(..)) || execution(* org.suhui.modules.toB.controller.OrderMainController.queryByOrderNo(..)) || execution(* org.suhui.modules.toB.controller.OrderMainController.createWithdrawalOrder(..))")
    public void beforeInvoke() throws Exception {
        log.debug("方法执行前");
        boolean result = MD5Util.verify(appSecret, request);
        if (!result) {
            throw new Exception("验签失败");
        }

    }

    /**
     * 方法执行后的通知
     */
    @After("execution(* org.suhui.modules.toB.controller.OrderMainController.createPaymentOrder(..))")
    public void afterInvoke() {
        log.debug("方法执行后");
    }

    /**
     * 方法执行返回后的通知
     */
    @AfterReturning("execution(* org.suhui.modules.toB.controller.OrderMainController.createPaymentOrder(..))")
    public void afterReturning() {
        log.debug("==================方法执行完成");
    }

    /**
     * 方法抛出异常的通知
     */
    @AfterThrowing("execution(* org.suhui.modules.toB.controller.OrderMainController.createPaymentOrder(..))")
    public void afterThrowing() {
        log.debug("==================方法执行报错");
    }
}
