package com.jonas.service;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.jonas.config.JPushConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <a href="https://docs.jiguang.cn/jpush/server/push/rest_api_v3_push">极光推送</a>
 */
@Slf4j
@Service("jpush")
public class JPushService implements PushService {
    @Resource
    private JPushConfig jpushConfig;

    /**
     * 发送通知消息
     *
     * @param title     App通知栏标题
     * @param content   App通知栏内容
     * @param extrasMap 额外推送信息（不会显示在通知栏，传递数据用）
     * @param tags      标签数组，设定哪些用户手机能接收信息（为空则所有用户都推送）
     */
    public PushResult sendNotification(String title, String content, Map<String, String> extrasMap, String... alias) throws APIConnectionException, APIRequestException {

        log.info("配置：" + jpushConfig);

        ClientConfig clientConfig = ClientConfig.getInstance();
        clientConfig.setTimeToLive(jpushConfig.getLiveTime());
        JPushClient jpushClient = new JPushClient(jpushConfig.getMasterSecret(), jpushConfig.getAppkey(), null, clientConfig);
        // 设置为消息推送方式为仅推送消息，不创建通知栏提醒
        PushPayload payload = buildNotificationPayload(title, content, extrasMap, alias);
        PushResult result = jpushClient.sendPush(payload);
        return result;
    }

    /**
     * 发送自定义消息，由APP端拦截信息后再决定是否创建通知(目前APP用此种方式)
     *
     * @param title     App通知栏标题
     * @param content   App通知栏内容（为了单行显示全，尽量保持在22个汉字以下）
     * @param extrasMap 额外推送信息（不会显示在通知栏，传递数据用）
     * @param alias     别名数组，设定哪些用户手机能接收信息（为空则所有用户都推送）
     * @return PushResult
     */
    public PushResult sendMessage(String title, String content, Map<String, String> extrasMap, String... alias) throws Exception {
        ClientConfig clientConfig = ClientConfig.getInstance();
        clientConfig.setTimeToLive(jpushConfig.getLiveTime());
        // 使用NativeHttpClient网络客户端，连接网络的方式，不提供回调函数
        JPushClient jpushClient = new JPushClient(jpushConfig.getMasterSecret(), jpushConfig.getAppkey(), null,
                clientConfig);
        // 设置为消息推送方式为仅推送消息，不创建通知栏提醒
        PushPayload payload = buildMessagePayload(title, content, extrasMap, alias);
        PushResult result = jpushClient.sendPush(payload);
        return result;
    }

    /**
     * 构建Android和IOS的自定义消息的推送消息对象
     *
     * @return PushPayload
     */
    private PushPayload buildMessagePayload(String title, String content, Map<String, String> extrasMap, String... tags) {
        return PushPayload.newBuilder().setPlatform(Platform.android_ios())
                .setAudience(Audience.registrationId(tags))
                .setNotification(Notification.newBuilder().setAlert(content)
                        .addPlatformNotification(AndroidNotification.newBuilder().setTitle(title).addExtras(extrasMap).build()).build())
                .setMessage(Message.newBuilder().setTitle(title).setMsgContent(content).addExtras(extrasMap).build())
                .build();
    }

    private PushPayload buildNotificationPayload(String title, String content, Map<String, String> extrasMap, String... tags) {
        if (extrasMap == null || extrasMap.isEmpty()) {
            extrasMap = new HashMap<>();
        }
        return PushPayload.newBuilder().setPlatform(Platform.android_ios())
                .setAudience(Audience.registrationId(tags))
                .setNotification(Notification.newBuilder()
                        .setAlert(content)
                        // 安卓平台
                        .addPlatformNotification(
                                AndroidNotification.newBuilder().setTitle(title).addExtras(extrasMap).build())
                        // IOS平台
                        .addPlatformNotification(
                                IosNotification.newBuilder().incrBadge(1).addExtras(extrasMap).build())
                        .build())
                .build();

    }

    @Override
    public void send(String title, String content) {
        log.info("调用JPush,title={},content={}", title, content);
    }
}
