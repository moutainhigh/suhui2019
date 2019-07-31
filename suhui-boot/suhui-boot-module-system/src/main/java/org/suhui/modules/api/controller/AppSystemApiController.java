package org.suhui.modules.api.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.suhui.common.api.vo.Result;
import org.suhui.common.constant.CommonConstant;
import org.suhui.modules.utils.SmsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/system")
@Api(tags="用户登录")
@Slf4j
public class AppSystemApiController {

    /**
     * 获取验证码
     * @param params
     * @return
     */
    @RequestMapping(value = "/sms" , method = RequestMethod.POST)
    public Result<JSONObject> sms(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params) {
        //用户退出逻辑
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();

        Random random = new Random();
        int x = random.nextInt(899999);
        x = x+100000;
        System.out.println(x+"");

        String phone = params.get("phone")+"" ;
        String areaCode = params.get("areaCode") + "" ;
        HttpSession session = request.getSession();
        session.setAttribute("smsCode_"+phone ,x);
        session.setMaxInactiveInterval(300);
        String rtn ="" ;
        //  areaCode 发送类型：1：国内  2：国际
        if(areaCode.equals("+86")){
            rtn = 	SmsUtil.sendSms(phone,x+"" , 1);
        }else{
            rtn = SmsUtil.sendSms(areaCode+phone,x+"" , 2);
        }
        //obj.put("smsCode", x+"");
        JSONObject object = JSONObject.parseObject(rtn) ;
        String status = (String)object.get("status") ;
        if(status.equals("success")){
            result.setResult(obj);
            result.success("发送验证码成功");
            result.setCode(CommonConstant.SC_OK_200);
            return result ;
        }else{
            result.setResult(obj);
            result.success("验证码发送失败");
            result.setCode(0);
            return result ;
        }

    }

}
