package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/18
 * @description 订单相关服务层实现类
 **/
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	
	private final OrderMapper orderMapper;
	private final OrderDetailMapper orderDetailMapper;
	private final AddressBookMapper addressBookMapper;
	private final ShoppingCartMapper shoppingCartMapper;
	
	/**
	 * 用户下单
	 *
	 * @param ordersSubmitDTO 订单信息
	 * @return 订单信息
	 */
	@Override
	@Transactional
	public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
		// 处理各种业务异常（地址簿为空、购物车数据为空）
		AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
		if (addressBook == null) {
			// 抛出业务异常
			throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
		}
		
		// 查询当前用户的购物车数据
		Long userId = BaseContext.getCurrentId();
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setUserId(userId);
		List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
		if (shoppingCartList == null || shoppingCartList.isEmpty()) {
			throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
		}
		
		// 向订单表插入一条数据
		Orders orders = new Orders();
		BeanUtils.copyProperties(ordersSubmitDTO, orders);
		orders.setOrderTime(LocalDateTime.now());
		orders.setPayStatus(Orders.UN_PAID);
		orders.setStatus(Orders.PENDING_PAYMENT);
		orders.setNumber(String.valueOf(System.currentTimeMillis()));
		orders.setPhone(addressBook.getPhone());
		orders.setConsignee(addressBook.getConsignee());
		orders.setUserId(userId);
		
		orderMapper.insert(orders);
		
		List<OrderDetail> orderDetailList = new ArrayList<>();
		// 向订单明细表插入n条数据
		for (ShoppingCart cart : shoppingCartList) {
			OrderDetail orderDetail = new OrderDetail();
			BeanUtils.copyProperties(cart, orderDetail);
			orderDetail.setOrderId(orders.getId());
			orderDetailList.add(orderDetail);
		}
		
		orderDetailMapper.insertBatch(orderDetailList);
		
		// 清空用户的购物车
		shoppingCartMapper.deleteByUserId(userId);
		
		// 封装VO返回结果
		return OrderSubmitVO.builder()
				.id(orders.getId())
				.orderTime(orders.getOrderTime())
				.orderNumber(orders.getNumber())
				.orderAmount(orders.getAmount())
				.build();
	}
}