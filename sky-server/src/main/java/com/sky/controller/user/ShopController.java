package com.sky.controller.user;

import com.sky.constant.RedisConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/13
 * @description 店铺相关接口
 **/

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {
	
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	
	/**
	 * 获取店铺营业状态
	 *
	 * @return 店铺的营业状态
	 */
	@GetMapping("/status")
	@ApiOperation("获取店铺营业状态")
	public Result<Integer> getStatus() {
		Integer status = Integer.valueOf(stringRedisTemplate.opsForValue().get(RedisConstant.SHOP_STATUS_KEY));
		log.info("获取到店铺的营业状态为：{}", status == 1 ? "营业中" : "打烊中");
		return Result.success(status);
	}
}