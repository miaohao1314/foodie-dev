package com.bfxy.collector.util;

import org.jboss.logging.MDC;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class InputMDC implements EnvironmentAware {

	// Environment: 环境变量
	private static Environment environment;
	
	@Override
	public void setEnvironment(Environment environment) {
		InputMDC.environment = environment;
	}

	//	请求过来时被put到MDC中，

	public static void putMDC() {
		MDC.put("hostName", NetUtil.getLocalHostName());
		MDC.put("ip", NetUtil.getLocalIp());
		MDC.put("applicationName", environment.getProperty("spring.application.name"));
	}

}
