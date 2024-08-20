package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/20
 * @description 数据统计相关服务层
 **/
public interface ReportService {
	
	/**
	 * 统计指定时间区间内的营业额数据
	 *
	 * @param begin 开始时间
	 * @param end   结束时间
	 * @return 营业额数据
	 */
	TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
	
	/**
	 * 统计指定时间区间内的用户数据
	 *
	 * @param begin 开始时间
	 * @param end   结束时间
	 * @return 用户统计数据
	 */
	UserReportVO getUserStatistics(LocalDate begin, LocalDate end);
	
	/**
	 * 统计指定时间区间内的订单数据
	 *
	 * @param begin 开始时间
	 * @param end   结束时间
	 * @return 订单统计数据
	 */
	OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);
	
	/**
	 * 统计指定时间区间内的销量排名前10
	 *
	 * @param begin 开始时间
	 * @param end   结束时间
	 * @return 销量排名前10
	 */
	SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);
}
