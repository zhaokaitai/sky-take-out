package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/18
 * @description 订单相关服务层
 **/
public interface OrderService {
	/**
	 * 用户下单
	 * @param ordersSubmitDTO 订单信息
	 * @return 订单信息
	 */
	OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
}
