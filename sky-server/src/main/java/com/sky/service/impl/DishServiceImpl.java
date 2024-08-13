package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/13
 * @description 菜品服务层实现类
 **/

@Service
@Slf4j
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {
	
	private final DishMapper dishMapper;
	private final DishFlavorMapper dishFlavorMapper;
	
	/**
	 * 新增菜品和对应的口味数据
	 *
	 * @param dishDTO 新增的菜品和对应口味的数据
	 */
	@Override
	@Transactional
	public void saveWithFlavor(DishDTO dishDTO) {
		Dish dish = new Dish();
		BeanUtils.copyProperties(dishDTO, dish);
		
		// 向菜品表插入1条数据
		dishMapper.insert(dish);
		
		// 获取insert语句生成的主键值
		Long dishId = dish.getId();
		
		List<DishFlavor> flavors = dishDTO.getFlavors();
		if (flavors != null && flavors.size() > 0) {
			flavors.forEach(dishFlavor -> {
				dishFlavor.setDishId(dishId);
			});
			// 向口味表插入n条数据
			dishFlavorMapper.insertBatch(flavors);
		}
		
	}
}