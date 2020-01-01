package org.suhui.modules.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.suhui.common.api.vo.Result;
import org.suhui.common.constant.CommonConstant;
import org.suhui.common.system.util.JwtUtil;
import org.suhui.common.system.vo.LoginUser;
import org.suhui.common.util.RedisUtil;
import org.suhui.common.util.oConvertUtils;
import org.suhui.modules.suhui.suhui.service.IPayUserInfoService;
import org.suhui.modules.suhui.suhui.service.IPayUserLoginService;
import org.suhui.modules.system.entity.SysUser;
import org.suhui.modules.system.service.ISysUserService;
import org.suhui.modules.utils.SmsUtil;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.StringWriter;

import java.util.Properties;
import java.util.Random;
import org.suhui.modules.suhui.suhui.entity.*;

@RestController
@RequestMapping("/api/login/system")
@Api(tags = "获取系统中的活动信息")
@Slf4j
public class ApiLoginSystemActivityController {

    /**
     * 
     *    这些信息用来解释给前端听：
          status    // 0: 未开始  1：正在进行 2：已经结束       活动目前是开始/正在进行/已经结束
          cardType  // currencyTicket:汇率券 , 其他类型待设计  活动里面发的卡的类型
          type      // 0: 汇率券抢券活动, 1: xx, 2: xxx       指活动的类型
     * 
     * 
     * 获取系统中的活动信息
       1）最新的系统祝贺信息（用户获得汇率券）
       2) 最近的活动信息（最近的几条活动信息）
     * @param
     * @return
     * 
     * 目前返回的是Demo数据。升级时，需要返回数据库中的活动信息     * 
     */
    @RequestMapping(value = "/getSystemActivity" , method = RequestMethod.POST)
    public Result<JSONObject>  getSystemActivity(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params) {

        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();

        final String startDate = params.get("startDate") + "";
        final String endDate = params.get("endDate") + "";
        obj.put("startDate", startDate);
        obj.put("endDate", endDate);

        List<String> congMsgs = new ArrayList<>();
        final String s1 = new String("恭喜********8432抢到5‰汇率券");
        final String s2 = new String("恭喜********2575抢到5‰汇率券");
        congMsgs.add(s1);
        congMsgs.add(s2);
        obj.put("congMsgs" , congMsgs);

        List<String> cardList = new ArrayList<>();
        JSONObject sampleCard = new JSONObject();
        sampleCard.put("name", "千分之五汇率券抢券活动");
        sampleCard.put("status", "2");   // 0: 未开始  1：正在进行 2：已经结束  
        sampleCard.put("type", 0);       // 0: 汇率券抢券活动, 1: xx, 2: xxx
        sampleCard.put("time", java.time.LocalDateTime.now().toString());
        Map<String, String> details = new HashMap<String,String>();
        details.put("cardType", "currencyTicket"); // currencyTicket:汇率券 ....
        details.put("rate", "0.005");
        sampleCard.put("cardDetails", details);

        cardList.add(sampleCard.toString());
        cardList.add(sampleCard.toString());
        cardList.add(sampleCard.toString());
        obj.put("cardList", cardList);

        result.setResult(obj);
        result.success("get getSystemActivity success");
        result.setCode(CommonConstant.SC_OK_200);
        return result;
    }

}

