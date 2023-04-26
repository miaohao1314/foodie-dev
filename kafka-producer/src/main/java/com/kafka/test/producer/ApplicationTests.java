package com.kafka.test.producer;


import com.kafka.producer.KafkaProducerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 第三个：
 * 发送消息测试类,往miaohao-topic里面去发送消息
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Test
    public void send() throws InterruptedException {

        String topic = "miaohao-topic";
        for(int i=0; i < 10; i ++) {
            kafkaProducerService.sendMessage(topic, "hello kafka123_miaohao" + i);
            Thread.sleep(5);
        }
        Thread.sleep(Integer.MAX_VALUE);
    }
}
