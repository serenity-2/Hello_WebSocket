package com.example.fast_demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FastDemoApplicationTests {

    @Autowired
    private AESUtilQN aesUtilQN;

    @Test
    void contextLoads() {
        System.out.println(System.currentTimeMillis());
        //https://ecloud test.tppension.cntaiping.com/bmp/query/file/downloadByEncryptFileNo?timestamp=1712799973523&nonce=1712799973523&sign=1&signature=018A7AA24C8B980427C2887A1C36B010F81BD41F&fileNo=BMP23122200000603094
    }



}
