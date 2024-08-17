package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/17
 * @description 购物车服务层实现类
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
	
	private final ShoppingCartMapper shoppingCartMapper;
	private final DishMapper dishMapper;
	private final SetmealMapper setmealMapper;
	
	/**
	 * 添加购物车
	 *
	 * @param shoppingCartDTO 添加的数据
	 */
	@Override
	public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
		// 判断当前加入到购物车中的商品是否已经存在了
		ShoppingCart shoppingCart = new ShoppingCart();
		BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
		Long userId = BaseContext.getCurrentId();
		shoppingCart.setUserId(userId);
		
		List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
		
		// 如果已经存在了，只需要将数量加一
		if (list != null && !list.isEmpty()) {
			ShoppingCart cart = list.get(0);
			cart.setNumber(cart.getNumber() + 1);
			shoppingCartMapper.updateNumberById(cart);
		} else {
			// 如果不存在，需要插入一条购物车数据
			
			// 判断本次添加到购物车的是菜品还是套餐
			Long dishId = shoppingCartDTO.getDishId();
			if (dishId != null) {
				// 本次添加到购物车的是菜品
				Dish dish = dishMapper.getById(dishId);
				shoppingCart.setName(dish.getName());
				shoppingCart.setImage(dish.getImage());
				shoppingCart.setAmount(dish.getPrice());
			} else {
				// 本次添加到购物车的是套餐
				Long setmealId = shoppingCartDTO.getSetmealId();
				Setmeal setmeal = setmealMapper.getById(setmealId);
				
				shoppingCart.setName(setmeal.getName());
				shoppingCart.setImage(setmeal.getImage());
				shoppingCart.setAmount(setmeal.getPrice());
			}
			shoppingCart.setNumber(1);
			shoppingCart.setCreateTime(LocalDateTime.now());
			
			shoppingCartMapper.insert(shoppingCart);
		}
	}
}