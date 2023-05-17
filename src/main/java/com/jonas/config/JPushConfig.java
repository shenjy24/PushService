package com.jonas.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class JPushConfig {
    /**
     * 极光推送的用户名
     */
    @Value("${jpush.appKey}")
    private String appkey;
    /**
     * 极光推送的密码
     */
    @Value("${jpush.masterSecret}")
    private String masterSecret;
    /**
     * 极光推送设置过期时间
     */
    @Value("${jpush.liveTime}")
    private Long liveTime;

}
