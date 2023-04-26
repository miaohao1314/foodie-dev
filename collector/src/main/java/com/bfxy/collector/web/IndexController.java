package com.bfxy.collector.web;


import com.bfxy.collector.util.InputMDC;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IndexController {

    /**
     * 过程： 先把日志通过filebeat收集起来，然后在扔到kafka，最后logstash通过log4j2.xml格式进行解析
     *
     *  * [%d{yyyy-MM-dd'T'HH:mm:ss.SSSZZ}]
     * 	 * [%level{length=5}]
     * 	 * [%thread-%tid]
     * 	 * [%logger]
     * 	 * [%X{hostName}]
     * 	 * [%X{ip}]
     * 	 * [%X{applicationName}]       - --  x： 自定义的日志输出格式
     * 	 * [%F,%L,%C,%M]
     * 	 * [%m] ## '%ex'%n
     * 	 * -----------------------------------------------
     * 	 * [2019-09-18T14:42:51.451+08:00]
     * 	 * [INFO]
     * 	 * [main-1]
     * 	 * [org.springframework.boot.web.embedded.tomcat.TomcatWebServer]
     * 	 * []
     * 	 * []
     * 	 * []
     * 	 * [TomcatWebServer.java,90,org.springframework.boot.web.embedded.tomcat.TomcatWebServer,initialize]
     * 	 * [Tomcat initialized with port(s): 8001 (http)] ## ''
     * 	 *
     * 	 * ["message",
     * 	 * "\[%{NOTSPACE:currentDateTime}\]
     * 	 *  \[%{NOTSPACE:level}\]
     * 	 *  \[%{NOTSPACE:thread-id}\]
     * 	 *  \[%{NOTSPACE:class}\]
     * 	 *  \[%{DATA:hostName}\]
     * 	 *  \[%{DATA:ip}\]
     * 	 *  \[%{DATA:applicationName}\]
     * 	 *  \[%{DATA:location}\]
     * 	 *  \[%{DATA:messageInfo}\]
     * 	 *  ## (\'\'|%{QUOTEDSTRING:throwable})"]
     * @return
     */

    @RequestMapping("/index")
    public  String index(){

        InputMDC.putMDC();

        log.info("我是一条info日志");

        log.warn("我是一条warn日志");

        log.error("我是一条error日志");

        return  "idx";
    }


}
