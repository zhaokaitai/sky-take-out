package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
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
}