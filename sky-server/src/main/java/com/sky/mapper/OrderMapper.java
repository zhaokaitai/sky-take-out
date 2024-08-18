package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
	 * @param orders 订单数据
	 */
	void insert(Orders orders);
	
	/**
	 * 根据订单号查询订单
	 * @param orderNumber
	 */
	@Select("select * from orders where number = #{orderNumber}")
	Orders getByNumber(String orderNumber);
	
	/**
	 * 修改订单信息
	 * @param orders
	 */
	void update(Orders orders);
}
