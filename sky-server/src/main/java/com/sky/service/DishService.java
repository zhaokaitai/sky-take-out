package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

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
	void saveWithFlavor(DishDTO dishDTO);
	
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
	
	/**
	 * 根据id查询菜品和对应的口味数据
	 * @param id 菜品id
	 * @return 查询结果
	 */
	DishVO getByIdWithFlavor(Long id);
	
	/**
	 * 根据id修改菜品基本信息和对应的口味信息
	 * @param dishDTO 菜品和口味信息
	 */
	void updateWithFlavor(DishDTO dishDTO);
	
	/**
	 * 根据分类id查询菜品
	 * @param categoryId
	 * @return
	 */
	public List<Dish> list(Long categoryId);
	
	/**
	 * 条件查询菜品和口味
	 * @param dish 查询条件
	 * @return 查询结果
	 */
	List<DishVO> listWithFlavor(Dish dish);
	
	/**
	 * 设置菜品起售停售
	 * @param status 菜品状态
	 * @param id 菜品id
	 */
	void startOrStop(Integer status, Long id);
}
