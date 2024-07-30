package com.example.fast_demo.task;

import com.example.fast_demo.websocket.EchoServer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ScheduledTasks {
    private final EchoServer echoServer;

    public ScheduledTasks(EchoServer echoServer) {
        this.echoServer = echoServer;
    }

    @Async
    @Scheduled(fixedRate = 10000) // 10秒钟执行一次
    public void updateData() {
        echoServer.sendMessage("数据有变动，这是最新数据【" + LocalDateTime.now() + "】");
    }
}
