package com.witkey.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author peace
 * @date 2026/3/16 16:00
 * @description:
 */
@Configuration
@MapperScan("com.witkey.common.domain.mapper")
public class MybatisPlusConfig {
}
