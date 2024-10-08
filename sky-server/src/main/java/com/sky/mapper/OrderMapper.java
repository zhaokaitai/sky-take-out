package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/18
 * @description 订单相关mapper
 **/
@Mapper
public interface OrderMapper {
	/**
	 * 插入订单数据
	 *
	 * @param orders 订单数据
	 */
	void insert(Orders orders);
	
	/**
	 * 根据订单号查询订单
	 *
	 * @param orderNumber
	 */
	@Select("select * from orders where number = #{orderNumber}")
	Orders getByNumber(String orderNumber);
	
	/**
	 * 修改订单信息
	 *
	 * @param orders
	 */
	void update(Orders orders);
	
	/**
	 * 分页条件查询并按下单时间排序
	 *
	 * @param ordersPageQueryDTO
	 */
	Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);
	
	/**
	 * 根据id查询订单
	 *
	 * @param id
	 */
	@Select("select * from orders where id=#{id}")
	Orders getById(Long id);
	
	/**
	 * 根据状态统计订单数量
	 *
	 * @param status
	 */
	@Select("select count(id) from orders where status = #{status}")
	Integer countStatus(Integer status);
	
	/**
	 * 根据订单状态和下单时间查询订单
	 *
	 * @param status    订单状态
	 * @param orderTime 下单时间
	 * @return 查询结果
	 */
	@Select("select * from orders where status = #{status} and order_time < #{orderTime}")
	List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);
	
	/**
	 * 根据动态条件统计营业额数据
	 *
	 * @param map 条件
	 * @return 营业额数据
	 */
	Double sumByMap(Map map);
	
	/**
	 * 根据动态条件统计订单数量
	 *
	 * @param map 条件
	 * @return 统计数量
	 */
	Integer countByMap(Map map);
	
	/**
	 * 统计指定时间区间内的销量排名前10
	 *
	 * @param begin 开始时间
	 * @param end   结束时间
	 * @return 销量排名前10数据
	 */
	List<GoodsSalesDTO> getsalesTop10(LocalDateTime begin, LocalDateTime end);
}
