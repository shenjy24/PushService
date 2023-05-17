package com.jonas.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PushServiceFactory {
    @Resource
    private Map<String, PushService> pushServiceMap = new ConcurrentHashMap<>(2);

    public PushService getPushService(String name) {
        return pushServiceMap.get(name);
    }
}
