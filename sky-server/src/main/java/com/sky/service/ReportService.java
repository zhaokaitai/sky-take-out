package com.sky.service;

import com.sky.vo.TurnoverReportVO;

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
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @return 营业额数据
	 */
	TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
}
