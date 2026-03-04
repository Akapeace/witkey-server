package com.witkey.web.controller;


import com.witkey.common.aspect.ApiOperationLog;
import com.witkey.common.utils.Response;
import com.witkey.web.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author peace
 * @date 2026/3/3 18:15
 * @description:
 */
@RestController
@Slf4j
public class TestController {

    @PostMapping("/test")
    @ApiOperationLog(description = "测试接口")
    public Response test(@RequestBody @Validated User user) {
        // 主动定义一个运行时异常，分母不能为零
        int i = 1 / 0;
        return Response.success();
    }
}
