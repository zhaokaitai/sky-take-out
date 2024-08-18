package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
	private final UserMapper userMapper;
	private final WeChatPayUtil weChatPayUtil;
	
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
	
	/**
	 * 订单支付
	 *
	 * @param ordersPaymentDTO
	 * @return
	 */
	@Override
	public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
		// 当前登录用户id
		Long userId = BaseContext.getCurrentId();
		User user = userMapper.getById(userId);
		
		//调用微信支付接口，生成预支付交易单
		JSONObject jsonObject = weChatPayUtil.pay(
				ordersPaymentDTO.getOrderNumber(), //商户订单号
				new BigDecimal(0.01), //支付金额，单位 元
				"苍穹外卖订单", //商品描述
				user.getOpenid() //微信用户的openid
		);
		
		if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
			throw new OrderBusinessException("该订单已支付");
		}
		
		OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
		vo.setPackageStr(jsonObject.getString("package"));
		
		return vo;
	}
	
	/**
	 * 支付成功，修改订单状态
	 *
	 * @param outTradeNo
	 */
	@Override
	public void paySuccess(String outTradeNo) {
		
		// 根据订单号查询订单
		Orders ordersDB = orderMapper.getByNumber(outTradeNo);
		
		// 根据订单id更新订单的状态、支付方式、支付状态、结账时间
		Orders orders = Orders.builder()
				.id(ordersDB.getId())
				.status(Orders.TO_BE_CONFIRMED)
				.payStatus(Orders.PAID)
				.checkoutTime(LocalDateTime.now())
				.build();
		
		orderMapper.update(orders);
	}
}