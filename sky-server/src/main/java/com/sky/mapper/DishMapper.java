package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author zkt
 */
@Mapper
public interface DishMapper {
	
	/**
	 * 根据分类id查询菜品数量
	 *
	 * @param categoryId 分类id
	 * @return 菜品数量
	 */
	@Select("select count(id) from dish where category_id = #{categoryId}")
	Integer countByCategoryId(Long categoryId);
	
	/**
	 * 插入菜品数据
	 *
	 * @param dish 菜品数据
	 */
	@AutoFill(OperationType.INSERT)
	void insert(Dish dish);
	
	/**
	 * 菜品分页查询
	 *
	 * @param dishPageQueryDTO 查询条件
	 * @return 查询结果
	 */
	Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);
	
	/**
	 * 根据id查询菜品
	 *
	 * @param id 需要查询的菜品id
	 * @return 查询结果
	 */
	@Select("select * from dish where id = #{id}")
	Dish getById(Long id);
	
	/**
	 * 根据id删除菜品
	 *
	 * @param id 需要删除的菜品id
	 */
	@Delete("delete from dish where id = #{id}")
	void deleteById(Long id);
}
