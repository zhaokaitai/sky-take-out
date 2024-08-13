package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
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
	private final SetmealDishMapper setmealDishMapper;
	
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
	
	/**
	 * 菜品分页查询
	 *
	 * @param dishPageQueryDTO 查询条件
	 * @return 查询结果
	 */
	@Override
	public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
		PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
		Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
		return new PageResult(page.getTotal(), page.getResult());
	}
	
	/**
	 * 菜品批量删除
	 *
	 * @param ids 需要删除的菜品id
	 */
	@Override
	@Transactional
	public void deleteBatch(List<Long> ids) {
		// 判断当前菜品是否能够删除---是否在起售中
		for (Long id : ids) {
			Dish dish = dishMapper.getById(id);
			if (dish.getStatus().equals(StatusConstant.ENABLE)) {
				// 当前菜品处于起售中，不能删除
				throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
			}
		}
		
		// 判断当前菜品是否能够删除---是否被套餐关联了
		List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
		if (setmealIds != null && !setmealIds.isEmpty()) {
			// 当前菜品被套餐关联了，不能删除
			throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
		}
		
		// 删除菜品表中的菜品数据
		for (Long id : ids) {
			dishMapper.deleteById(id);
			try {
				// 删除菜品关联的口味数据
				dishFlavorMapper.deleteByDishId(id);
			} catch (Exception e) {
				log.error("删除口味失败：{}", e.getMessage());
			}
		}
	}
}