package com.sky.controller.admin;

import com.sky.constant.RedisConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/13
 * @description 菜品管理
 **/

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
	
	@Resource
	private DishService dishService;
	
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	
	/**
	 * 新增菜品
	 *
	 * @param dishDTO 新增菜品的信息
	 * @return 响应结果
	 */
	@PostMapping
	@ApiOperation("新增菜品")
	public Result save(@RequestBody DishDTO dishDTO) {
		log.info("新增菜品：{}", dishDTO);
		dishService.saveWithFlavor(dishDTO);
		
		cleanCache(RedisConstant.DISH_CATEGORY_KEY + dishDTO.getCategoryId());
		return Result.success();
	}
	
	/**
	 * 菜品分页查询
	 *
	 * @param dishPageQueryDTO 查询条件
	 * @return 查询结果
	 */
	@GetMapping("/page")
	@ApiOperation("菜品分页查询")
	public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
		log.info("菜品分页查询：{}", dishPageQueryDTO);
		PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
		return Result.success(pageResult);
	}
	
	/**
	 * 菜品批量删除
	 *
	 * @param ids 需要删除的菜品id
	 * @return 响应结果
	 */
	@DeleteMapping
	@ApiOperation("菜品批量删除")
	public Result delete(@RequestParam List<Long> ids) {
		log.info("菜品批量删除：{}", ids);
		dishService.deleteBatch(ids);
		
		cleanCache(RedisConstant.DISH_CATEGORY_KEY + "*");
		return Result.success();
	}
	
	/**
	 * 根据id查询菜品
	 *
	 * @param id 菜品id
	 * @return 查询结果
	 */
	@GetMapping("/{id}")
	@ApiOperation("根据id查询菜品")
	public Result<DishVO> getById(@PathVariable Long id) {
		log.info("根据id查询菜品：{}", id);
		DishVO dishVO = dishService.getByIdWithFlavor(id);
		return Result.success(dishVO);
	}
	
	/**
	 * 修改菜品
	 *
	 * @param dishDTO 菜品信息
	 * @return 响应结果
	 */
	@PutMapping
	@ApiOperation("修改菜品")
	public Result update(@RequestBody DishDTO dishDTO) {
		log.info("修改菜品：{}", dishDTO);
		dishService.updateWithFlavor(dishDTO);
		
		cleanCache(RedisConstant.DISH_CATEGORY_KEY + "*");
		return Result.success();
	}
	
	/**
	 * 菜品起售停售
	 *
	 * @param status 菜品状态
	 * @param id     菜品id
	 * @return 响应结果
	 */
	@PostMapping("/status/{status}")
	@ApiOperation("菜品起售停售")
	public Result<String> startOrStop(@PathVariable Integer status, Long id) {
		dishService.startOrStop(status, id);
		
		cleanCache(RedisConstant.DISH_CATEGORY_KEY + "*");
		return Result.success();
	}
	
	/**
	 * 根据分类id查询菜品
	 *
	 * @param categoryId
	 * @return
	 */
	@GetMapping("/list")
	@ApiOperation("根据分类id查询菜品")
	public Result<List<Dish>> list(Long categoryId) {
		List<Dish> list = dishService.list(categoryId);
		return Result.success(list);
	}
	
	private void cleanCache(String pattern) {
		// 将所有的菜品缓存数据清理掉
		Set<String> keys = stringRedisTemplate.keys(pattern);
		if (keys != null) {
			stringRedisTemplate.delete(keys);
		}
	}
}