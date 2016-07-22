package com.mawujun.controller.spring.mvc.jackson;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/scheduling/annotation/EnableScheduling.html
 * 参考spring的 EnableScheduling 配置信息,里面有多种配置方式
 * 要实现SchedulingConfigurer接口，不然在启动的时候会爆  类没定义的异常
 * @author mawujun qq:16064988 mawujun1234@163.com
 *
 */
@Configuration
@EnableScheduling
// @ComponentScan(basePackages="com.myco.tasks")
public class ScheduleConfig {//implements SchedulingConfigurer {

}
