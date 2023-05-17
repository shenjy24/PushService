package com.jonas;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.jonas.service.JPushService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
public class PushServiceApplicationTest {
    @Autowired
    private JPushService jPushService;

    @Test
    public void testSend() throws APIConnectionException, APIRequestException {
        jPushService.sendNotification("极光", "极光", new HashMap<>());
    }
}
