package com.imooc.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 调用提供者的服务
 */

@RestController
@Slf4j
public class EurekaconsumerController {

    @Autowired
    private LoadBalancerClient client;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 访问get请求
     * @return
     */
    @GetMapping("getHello")
    public    String   getHello(){
        // 去注册拿到服务提供者的实例(可用的)
        ServiceInstance  instance = client.choose("eureka-client");
        if(instance == null){
            return "no available instances";
        }

        // 拿到实例之后发起对服务的调用
        String target=String.format("http://%s:%s/sayHello",
                instance.getHost(),instance.getPort());
                log.info("url  is "+target);

                // 访问一个get请求
        return restTemplate.getForObject(target,String.class);
    }


    /**
     * 访问post请求
     * @return
     */
    @PostMapping("getHello")
    public    Friend   getPostHello(){
        // 去注册拿到服务提供者的实例(可用的)
        ServiceInstance  instance = client.choose("eureka-client");
        if(instance == null){
            return  null;
        }

        // 拿到实例之后发起对服务的调用
        String target=String.format("http://%s:%s/sayHello",
                instance.getHost(),instance.getPort());
        log.info("url  is "+target);

        // 访问一个get请求
        Friend friend=new Friend();
        // 这里将friend中的name值传入到提供者方法中
        friend.setName("eureka consumer");
        return restTemplate.postForObject(target,friend,Friend.class);
    }
}
