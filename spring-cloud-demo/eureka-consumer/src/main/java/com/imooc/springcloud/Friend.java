package com.imooc.springcloud;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @Data: 可以将这个类中生成的变量生成get和set和toString的方法
 */


@Data
@Component
public class Friend {


    private  String name;
    private  String port;

}
