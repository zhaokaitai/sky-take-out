package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/19
 * @description 定时任务
 **/

@Component
@Slf4j
public class MyTask {
	
	/**
	 * 定时任务  每隔5秒触发一次
	 */
	// @Scheduled(cron = "0/5 * * * * ?")
	public void executeTask() {
		log.info("定时任务开始执行：{}", new Date());
	}
}