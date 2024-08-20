package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/20
 * @description 数据统计相关接口
 **/

@RestController
@RequestMapping("/admin/report")
@Api(tags = "数据统计相关接口")
@Slf4j
public class ReportController {
	
	@Resource
	private ReportService reportService;
	
	/**
	 * 营业额统计
	 *
	 * @param begin 开始日期
	 * @param end   结束日期
	 * @return 统计结果
	 */
	@GetMapping("/turnoverStatistics")
	@ApiOperation("营业额统计")
	public Result<TurnoverReportVO> turnoverStatistics(
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
		log.info("营业额数据统计：{}，{}", begin, end);
		return Result.success(reportService.getTurnoverStatistics(begin, end));
	}
}