package org.suhui;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import org.suhui.modules.demo.mock.MockController;
import org.suhui.modules.demo.test.entity.JeecgDemo;
import org.suhui.modules.demo.test.mapper.JeecgDemoMapper;
import org.suhui.modules.demo.test.service.IJeecgDemoService;
import org.suhui.modules.system.service.ISysDataLogService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class SampleTest {

//    @Resource
//    private JeecgDemoMapper jeecgDemoMapper;
//    @Resource
//    private IJeecgDemoService jeecgDemoService;
//    @Resource
//    private ISysDataLogService sysDataLogService;
//    @Resource
//    private MockController mock;

    @Test
    public void testSMS() {
        //初始化clnt,使用单例方式
        YunpianClient clnt = new YunpianClient("apikey").init();

        //发送短信API
        Map<String, String> param = clnt.newParam(2);
        param.put(YunpianClient.MOBILE, "15098899951");
        param.put(YunpianClient.TEXT, "【云片网】您的验证码是1234");
        Result<SmsSingleSend> r = clnt.sms().single_send(param);
        //获取返回结果，返回码:r.getCode(),返回码描述:r.getMsg(),API结果:r.getData(),其他说明:r.getDetail(),调用异常:r.getThrowable()

        //账户:clnt.user().* 签名:clnt.sign().* 模版:clnt.tpl().* 短信:clnt.sms().* 语音:clnt.voice().* 流量:clnt.flow().* 隐私通话:clnt.call().*

        //释放clnt
        clnt.close();
    }


}
