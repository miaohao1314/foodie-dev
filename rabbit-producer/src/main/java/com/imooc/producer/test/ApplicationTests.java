package com.imooc.producer.test;

import com.imooc.producer.RabbitSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {


	@Autowired
	private RabbitSender reRabbitSender;
	
	
	@Test
	public void testSender() throws Exception {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("attr1", "12345");
		properties.put("attr2", "abcde");
		reRabbitSender.send("hello rabbitmq miaohao!", properties);
		Thread.sleep(10000);
	}
}
