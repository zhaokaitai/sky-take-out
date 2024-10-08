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
	
	/**
	 * 查看购物车
	 *
	 * @return 购物车数据
	 */
	@Override
	public List<ShoppingCart> showShoppingCart() {
		// 获取当前用户的id
		Long userId = BaseContext.getCurrentId();
		ShoppingCart shoppingCart = ShoppingCart.builder()
				.userId(userId)
				.build();
		
		return shoppingCartMapper.list(shoppingCart);
	}
	
	/**
	 * 清空购物车
	 */
	@Override
	public void cleanShoppingCart() {
		// 获取当前用户的id
		Long userId = BaseContext.getCurrentId();
		
		shoppingCartMapper.deleteByUserId(userId);
	}
	
	/**
	 * 删除购物车中的一个商品
	 *
	 * @param shoppingCartDTO 需要删除的商品
	 */
	@Override
	public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
		// 设置查询条件
		ShoppingCart shoppingCart = new ShoppingCart();
		BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
		shoppingCart.setUserId(BaseContext.getCurrentId());
		
		// 查询当前商品的数据
		List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
		
		if (list != null && !list.isEmpty()) {
			shoppingCart = list.get(0);
			
			if (shoppingCart.getNumber() == 1) {
				// 该商品数量等于1，直接删除该数据
				shoppingCartMapper.deleteById(shoppingCart.getId());
			} else {
				// 该商品数量大于1，将该商品数量减1
				shoppingCart.setNumber(shoppingCart.getNumber() - 1);
				shoppingCartMapper.updateNumberById(shoppingCart);
			}
		}
	}
}