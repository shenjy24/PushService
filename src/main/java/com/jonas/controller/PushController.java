package com.jonas.controller;

import cn.jpush.api.push.PushResult;
import com.jonas.service.JPushService;
import com.jonas.service.PushService;
import com.jonas.service.PushServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/push")
@RequiredArgsConstructor
public class PushController {

    private final JPushService pushService;

    private final PushServiceFactory pushServiceFactory;

    @RequestMapping("/send")
    public void send(@Value("${push.type}") String pushType, String title, String content) {
        PushService service = pushServiceFactory.getPushService(pushType);
        service.send(title, content);
    }

    @RequestMapping("/note")
    public void sendNote(String title, String content, String tag) {
        try {
            PushResult res = pushService.sendNotification(title, content, new HashMap<>(), tag);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/msg")
    public void sendMsg(String title, String content, String tag) {
        try {
            PushResult res = pushService.sendMessage(title, content, new HashMap<>(), tag);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
