package org.suhui.modules.demo.test.controller;

import org.suhui.common.api.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping(value = "/test")
    public Result<String> hello() {
        Result<String> result = new Result<String>();
        result.setResult("Hello World!,终于他爸可以了");
        result.setSuccess(true);
        return result;
    }
}
