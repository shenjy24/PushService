package com.jonas.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("upush")
public class UPushService implements PushService {
    @Override
    public void send(String title, String content) {
        log.info("调用UPush,title={},content={}", title, content);
    }
}
