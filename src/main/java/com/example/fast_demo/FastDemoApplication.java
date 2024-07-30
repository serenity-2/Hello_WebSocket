package com.example.fast_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableAsync
@EnableWebSocket
@EnableScheduling
@SpringBootApplication
public class FastDemoApplication {


    public static void main(String[] args) {
        SpringApplication.run(FastDemoApplication.class, args);
    }


    /**
     * 渠道接入平台
     * 主要功能有保险服务的投保信息录入，核保（责任链），承保、出单，保单查询，退保（退还保费试算），保费试算，理赔（结案通知书），电子保单下载，更新健康告知与异常补充，保单状态回传（同步到第三方平台），第三方平台对接（银保通，蚂蚁，水滴等）等功能
     * 投保：包括销售信息，客户信息，险种信息 ，联系人信息，投保单主信息，附件信息等
     * 核保：先验签和参数校验，产品可售校验，责任链
     * 责任链 核保：职业等级校验，生成投保单号，保费计算（险种，保费，保额，被保人年龄，健康状况等和计算公式）、
     * 健康告知核保、自动核保、反洗钱校验、公安身份认证校验、内外勤挂靠校验、身份重复校验
     * 承保：异步承保（主要逻辑是根据投保信息和健康告知的内容来判断是否承保，ruo）
     *
     */
}



//Runtime.getRuntime().availableProcessors(); 获取当前jvm所在计算机的可用处理器核心数量，用于线程的核心线程数

//公安部身份认证批量接口：有多个需要认证的身份信息，为了提高效率，使用线程池和CountDownLatch并发执行认证任务，提高效率
