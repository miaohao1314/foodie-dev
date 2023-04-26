package com.bfxy.collector.entity;

import lombok.Data;

/**
 * 日志告警实体类
 */
@Data
public class AccurateWatcherMessage {
	
	private String title;
	
	private String executionTime;
	
	private String applicationName;
	
	private String level;
	
	private String body;

}
