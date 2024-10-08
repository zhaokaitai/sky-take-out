package com.sky.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.RedisConstant;
import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zkt
 */
@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
	@Resource
	private DishService dishService;
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	
	/**
	 * 根据分类id查询菜品
	 *
	 * @param categoryId 分类id
	 * @return 菜品列表
	 */
	@GetMapping("/list")
	@ApiOperation("根据分类id查询菜品")
	public Result<List<DishVO>> list(Long categoryId) {
		// 构造redis中的key，规则：dish_分类id
		String key = RedisConstant.DISH_CATEGORY_KEY + categoryId;
		
		// 查询redis中是否存在菜品数据
		String redisList = stringRedisTemplate.opsForValue().get(key);
		List<DishVO> list;
		
		if (redisList != null && !redisList.isEmpty()) {
			// 如果存在，直接返回，无须查询数据库
			list = (List<DishVO>) JSONObject.parse(redisList);
			return Result.success(list);
		}
		
		Dish dish = new Dish();
		dish.setCategoryId(categoryId);
		
		// 查询起售中的菜品
		dish.setStatus(StatusConstant.ENABLE);
		
		// 如果不存在，查询数据库，将查询到的数据放入redis中
		list = dishService.listWithFlavor(dish);
		stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(list));
		
		return Result.success(list);
	}
	
}
