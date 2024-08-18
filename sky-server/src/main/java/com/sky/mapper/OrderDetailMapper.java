package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/18
 * @description 订单明细相关mapper
 **/
@Mapper
public interface OrderDetailMapper {
	/**
	 * 批量插入订单明细数据
	 *
	 * @param orderDetailList 订单明细数据集合
	 */
	void insertBatch(List<OrderDetail> orderDetailList);
	
	/**
	 * 根据订单id查询订单明细
	 * @param orderId
	 * @return
	 */
	@Select("select * from order_detail where order_id = #{orderId}")
	List<OrderDetail> getByOrderId(Long orderId);
}
