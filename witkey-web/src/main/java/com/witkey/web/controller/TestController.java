package com.witkey.web.controller;


import com.witkey.common.aspect.ApiOperationLog;
import com.witkey.common.utils.JsonUtil;
import com.witkey.common.utils.Response;
import com.witkey.web.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author peace
 * @date 2026/3/3 18:15
 * @description:
 */
@RestController
@Slf4j
public class TestController {

    @PostMapping("/admin/test")
    @ApiOperationLog(description = "测试接口")
    public Response test(@RequestBody @Validated User user) {
        // 打印入参
        log.info(JsonUtil.toJsonString(user));

        // 设置三种日期字段值
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateDate(LocalDate.now());
        user.setTime(LocalTime.now());

        return Response.success(user);
    }
}
