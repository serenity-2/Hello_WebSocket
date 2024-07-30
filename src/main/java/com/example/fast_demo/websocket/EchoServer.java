package com.example.fast_demo.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j //@Slf4j 注解会自动生成日志对象，而不需要手动调用 LoggerFactory.getLogger()
@Component
@ServerEndpoint("/echo") //将目前的类定义成一个websocket服务器端，注解的值将被用于监听用户连接的终端访问URL地址，客户端可以通过这个URL来连接到WebSocket服务器端
public class EchoServer {

    private Session session;

    //    private static final Logger log = LoggerFactory.getLogger(EchoServer.class);
    private static final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>(); // 用于存储所有连接的WebSocket会话


    /**
     * 当WebSocket建立连接成功后会触发这个注解修饰的方法
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        sessions.put(session.getId(), session);
        log.info("WebSocket 连接已经建立");
        sendMessage("服务端准备就绪");
    }

    /**
     * 向所有打开的会话发送指定的消息
     *
     * @param message 需要发送的消息内容
     */
    public void sendMessage(String message) {
        if (sessions.values().stream().anyMatch(s -> s.isOpen())) {
            sessions.values().stream()
                    .filter(Session::isOpen)
                    .forEach(s -> {
                        try {
                            s.getBasicRemote().sendText(message);
                            log.info("发送消息成功");
                        } catch (IOException e) {
                            log.error("发送消息失败", e);
                        }
                    });
        }
    }

    /**
     * 当客户端发送消息到服务端时，会触发这个注解修改的方法。
     *
     * @param message
     * @param session
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("收到客户端消息：" + message);
        sendMessage("这是原始数据");
    }

    /**
     * 当WebSocket建立的连接断开后会触发这个注解修饰的方法。
     */
    @OnClose
    public void onClose() {
        System.out.println("WebSocket 连接已经关闭。");
    }

    /**
     * 当WebSocket建立连接时出现异常会触发这个注解修饰的方法。
     *
     * @param t
     */
    @OnError
    public void onError(Throwable t) {
        System.out.println("WebSocket 连接出现错误：" + t.getMessage());
    }
}
