package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/13
 * @description 口味相关mapper
 **/
@Mapper
public interface DishFlavorMapper {
	
	/**
	 * 批量插入口味数据
	 *
	 * @param flavors 需要插入的口味数据
	 */
	void insertBatch(List<DishFlavor> flavors);
	
	/**
	 * 根据菜品id删除对应口味数据
	 *
	 * @param dishId 菜品id
	 */
	@Delete("delete from dish_flavor where dish_id = #{dishId}")
	void deleteByDishId(Long dishId);
	
	/**
	 * 根据菜品id集合批量删除关联的口味数据
	 *
	 * @param dishIds 菜品id集合
	 */
	void deleteByDishIds(List<Long> dishIds);
	
	/**
	 * 根据菜品id查询对应的口味数据
	 *
	 * @param dishId 菜品id
	 * @return 查询结果
	 */
	@Select("select * from dish_flavor where dish_id = #{dishId}")
	List<DishFlavor> getByDishId(Long dishId);
}
