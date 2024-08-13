package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/13
 * @description 菜品与套餐关系表查询
 **/
@Mapper
public interface SetmealDishMapper {
	
	/**
	 * 根据菜品id查询对应的套餐id
	 * @param dishIds 菜品id
	 * @return 套餐id
	 */
	List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}
