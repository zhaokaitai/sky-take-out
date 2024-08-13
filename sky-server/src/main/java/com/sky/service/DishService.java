package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/13
 * @description 菜品服务层
 **/
public interface DishService {
	
	/**
	 * 新增菜品和对应的口味数据
	 *
	 * @param dishDTO 新增的菜品和对应口味的数据
	 */
	public void saveWithFlavor(DishDTO dishDTO);
	
	/**
	 * 菜品分页查询
	 *
	 * @param dishPageQueryDTO 查询条件
	 * @return 查询结果
	 */
	PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
	
	/**
	 * 菜品批量删除
	 * @param ids 需要删除的菜品id
	 */
	void deleteBatch(List<Long> ids);
}
