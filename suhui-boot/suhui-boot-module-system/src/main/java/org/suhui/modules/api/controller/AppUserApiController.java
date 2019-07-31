package org.suhui.modules.api.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.suhui.common.api.vo.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Api(tags="用户登录")
@Slf4j
public class AppUserApiController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result<JSONObject> register(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params ) {

    }
}
