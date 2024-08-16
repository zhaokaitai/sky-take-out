package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
	 *
	 * @param dishIds 菜品id
	 * @return 套餐id
	 */
	List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
	
	/**
	 * 批量保存套餐和菜品的关联关系
	 *
	 * @param setmealDishes
	 */
	void insertBatch(List<SetmealDish> setmealDishes);
	
	/**
	 * 根据套餐id删除套餐和菜品的关联关系
	 *
	 * @param setmealId
	 */
	@Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
	void deleteBySetmealId(Long setmealId);
	
	/**
	 * 根据套餐id查询套餐和菜品的关联关系
	 *
	 * @param setmealId
	 * @return
	 */
	@Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
	List<SetmealDish> getBySetmealId(Long setmealId);
}
