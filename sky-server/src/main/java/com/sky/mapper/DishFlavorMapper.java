package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

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
	 * @param flavors 需要插入的口味数据
	 */
	void insertBatch(List<DishFlavor> flavors);
}
