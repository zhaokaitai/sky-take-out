package com.sky.controller.user;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/18
 * @description 订单相关接口
 **/
@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "C端订单相关接口")
@Slf4j
public class OrderController {
	
	@Resource
	private OrderService orderService;
	
	/**
	 * 用户下单
	 *
	 * @param ordersSubmitDTO 订单信息
	 * @return 订单信息
	 */
	@PostMapping("/submit")
	@ApiOperation("用户下单")
	public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
		log.info("用户下单，参数为：{}", ordersSubmitDTO);
		OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
		return Result.success(orderSubmitVO);
	}
	
	/**
	 * 订单支付
	 *
	 * @param ordersPaymentDTO 订单支付信息
	 * @return 预支付交易单
	 */
	@PutMapping("/payment")
	@ApiOperation("订单支付")
	public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
		log.info("订单支付：{}", ordersPaymentDTO);
		OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
		log.info("生成预支付交易单：{}", orderPaymentVO);
		return Result.success(orderPaymentVO);
	}
	
	/**
	 * 历史订单查询
	 *
	 * @param page
	 * @param pageSize
	 * @param status   订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
	 * @return
	 */
	@GetMapping("/historyOrders")
	@ApiOperation("历史订单查询")
	public Result<PageResult> page(int page, int pageSize, Integer status) {
		PageResult pageResult = orderService.pageQuery4User(page, pageSize, status);
		return Result.success(pageResult);
	}
	
	/**
	 * 查询订单详情
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/orderDetail/{id}")
	@ApiOperation("查询订单详情")
	public Result<OrderVO> details(@PathVariable("id") Long id) {
		OrderVO orderVO = orderService.details(id);
		return Result.success(orderVO);
	}
	
	/**
	 * 用户取消订单
	 *
	 * @return
	 */
	@PutMapping("/cancel/{id}")
	@ApiOperation("取消订单")
	public Result cancel(@PathVariable("id") Long id) throws Exception {
		orderService.userCancelById(id);
		return Result.success();
	}
	
	/**
	 * 再来一单
	 *
	 * @param id
	 * @return
	 */
	@PostMapping("/repetition/{id}")
	@ApiOperation("再来一单")
	public Result repetition(@PathVariable Long id) {
		orderService.repetition(id);
		return Result.success();
	}
	
	/**
	 * 客户催单
	 * @param id 订单id
	 * @return 响应结果
	 */
	@GetMapping("/reminder/{id}")
	@ApiOperation("客户催单")
	public Result reminder(@PathVariable Long id) {
		orderService.reminder(id);
		return Result.success();
	}
}