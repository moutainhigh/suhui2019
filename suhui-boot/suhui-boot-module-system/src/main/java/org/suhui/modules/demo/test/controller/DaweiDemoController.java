package org.suhui.modules.demo.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.suhui.common.api.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/daweiDemo")
@Slf4j
public class DaweiDemoController {
    /**
     * hello world
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/hello")
    public Result<String> hello() {
        Result<String> result = new Result<String>();
        result.setResult("Hello World!,终于他妈可以了");
        result.setSuccess(true);
        return result;
    }
}
