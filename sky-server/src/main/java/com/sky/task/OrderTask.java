package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/19
 * @description 定时任务类，定时处理订单状态
 **/

@Component
@Slf4j
public class OrderTask {
	
	@Resource
	private OrderMapper orderMapper;
	
	/**
	 * 处理超时订单
	 */
	@Scheduled(cron = "0 * * * * ? ")
	public void processTimeoutOrder() {
		log.info("定时处理超时订单：{}", LocalDateTime.now());
		
		LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
		// 查询待付款且超时的订单
		List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
		
		if (ordersList != null && !ordersList.isEmpty()) {
			for (Orders orders : ordersList) {
				orders.setStatus(Orders.CANCELLED);
				orders.setCancelReason("订单超时，自动取消");
				orders.setCancelTime(LocalDateTime.now());
				orderMapper.update(orders);
			}
		}
	}
	
	/**
	 * 处理一直处于派送中的订单
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void processDeliveryOrder() {
		log.info("定时处理处于派送中的订单：{}", LocalDateTime.now());
		
		LocalDateTime time = LocalDateTime.now().plusHours(-1);
		List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
		
		if (ordersList != null && !ordersList.isEmpty()) {
			for (Orders orders : ordersList) {
				orders.setStatus(Orders.COMPLETED);
				orderMapper.update(orders);
			}
		}
	}
}