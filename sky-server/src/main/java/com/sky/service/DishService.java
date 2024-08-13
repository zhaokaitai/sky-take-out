package com.sky.service;

import com.sky.dto.DishDTO;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/13
 * @description 菜品服务层
 **/
public interface DishService {
	
	/**
	 * 新增菜品和对应的口味数据
	 * @param dishDTO 新增的菜品和对应口味的数据
	 */
	public void saveWithFlavor(DishDTO dishDTO);
}
