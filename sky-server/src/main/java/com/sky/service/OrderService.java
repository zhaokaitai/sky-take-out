package com.sky.service;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderPaymentVO;
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
	
	/**
	 * 订单支付
	 * @param ordersPaymentDTO
	 * @return
	 */
	OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;
	
	/**
	 * 支付成功，修改订单状态
	 * @param outTradeNo
	 */
	void paySuccess(String outTradeNo);
}
