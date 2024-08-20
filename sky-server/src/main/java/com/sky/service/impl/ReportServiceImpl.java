package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/20
 * @description 数据统计相关服务层实现类
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
	
	private final OrderMapper orderMapper;
	private final UserMapper userMapper;
	
	/**
	 * 统计指定时间区间内的营业额数据
	 *
	 * @param begin 开始时间
	 * @param end   结束时间
	 * @return 营业额数据
	 */
	@Override
	public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
		// 当前集合用于存放从begin到end范围内的每天的日期
		List<LocalDate> dateList = new ArrayList<>();
		
		dateList.add(begin);
		while (!begin.equals(end)) {
			// 日期计算，计算指定日期的后一天对应的日期
			begin = begin.plusDays(1);
			dateList.add(begin);
		}
		
		// 存放每天的营业额
		List<Double> turnoverList = new ArrayList<>();
		
		for (LocalDate date : dateList) {
			// 查询date日期对应的营业额数据，营业额：状态为“已完成”的订单金额合计
			LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
			LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
			
			Map map = new HashMap();
			map.put("begin", beginTime);
			map.put("end", endTime);
			map.put("status", Orders.COMPLETED);
			
			Double turnover = orderMapper.sumByMap(map);
			turnover = turnover == null ? 0.0 : turnover;
			turnoverList.add(turnover);
		}
		
		// 封装返回结果
		return TurnoverReportVO.builder()
				.dateList(StringUtils.join(dateList, ","))
				.turnoverList(StringUtils.join(turnoverList, ","))
				.build();
	}
	
	/**
	 * 统计指定时间区间内的用户数据
	 *
	 * @param begin 开始时间
	 * @param end   结束时间
	 * @return 用户统计数据
	 */
	@Override
	public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
		// 存放从begin到end之间的每天对应的日期
		List<LocalDate> dateList = new ArrayList<>();
		
		dateList.add(begin);
		while (!begin.equals(end)) {
			begin = begin.plusDays(1);
			dateList.add(begin);
		}
		
		// 存放每天的新增用户数量
		List<Integer> newUserList = new ArrayList<>();
		// 存放每天的总用户数量
		List<Integer> totalUserList = new ArrayList<>();
		
		for (LocalDate date : dateList) {
			LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
			LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
			
			Map map = new HashMap();
			map.put("end", endTime);
			
			// 总用户数量
			Integer totalUser = userMapper.countByMap(map);
			
			map.put("begin", beginTime);
			
			// 新增用户数量
			Integer newUser = userMapper.countByMap(map);
			
			totalUserList.add(totalUser);
			newUserList.add(newUser);
		}
		
		// 封装结果数据
		return UserReportVO.builder()
				.dateList(StringUtils.join(dateList, ","))
				.totalUserList(StringUtils.join(totalUserList, ","))
				.newUserList(StringUtils.join(newUserList, ","))
				.build();
	}
	
	/**
	 * 统计指定时间区间内的订单数据
	 *
	 * @param begin 开始时间
	 * @param end   结束时间
	 * @return 订单统计数据
	 */
	@Override
	public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
		// 存放从begin到end之间的每天对应的日期
		List<LocalDate> dateList = new ArrayList<>();
		
		dateList.add(begin);
		while (!begin.equals(end)) {
			begin = begin.plusDays(1);
			dateList.add(begin);
		}
		
		// 存放每天的订单总数
		List<Integer> orderCountList = new ArrayList<>();
		// 存放每天的有效订单数
		List<Integer> validOrderCountList = new ArrayList<>();
		
		// 遍历dateList结合，查询每天的有效订单数和订单总数
		for (LocalDate date : dateList) {
			// 查询每天的订单总数
			Integer orderCount = getOrderCount(date, null);
			
			// 查询每天的有效订单数
			Integer validOrderCount = getOrderCount(date, Orders.COMPLETED);
			
			orderCountList.add(orderCount);
			validOrderCountList.add(validOrderCount);
		}
		
		// 计算时间区间内的订单总数量
		Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
		
		// 计算时间区间内的有效订单数量
		Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();
		
		// 计算订单完成率
		Double orderCompletionRate = 0.0;
		if (totalOrderCount != 0) {
			orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
		}
		
		// 封装返回结果
		return OrderReportVO.builder()
				.dateList(StringUtils.join(dateList, ","))
				.orderCountList(StringUtils.join(orderCountList, ","))
				.validOrderCountList(StringUtils.join(validOrderCountList, ","))
				.totalOrderCount(totalOrderCount)
				.validOrderCount(validOrderCount)
				.orderCompletionRate(orderCompletionRate)
				.build();
	}
	
	/**
	 * 根据条件统计订单数量
	 *
	 * @param date   日期
	 * @param status 状态
	 * @return 统计结果
	 */
	private Integer getOrderCount(LocalDate date, Integer status) {
		LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
		LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
		
		Map map = new HashMap();
		map.put("begin", beginTime);
		map.put("end", endTime);
		map.put("status", status);
		
		return orderMapper.countByMap(map);
	}
}