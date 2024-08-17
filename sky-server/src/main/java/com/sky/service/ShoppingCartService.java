package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/17
 * @description 购物车服务层
 **/
public interface ShoppingCartService {
	
	/**
	 * 添加购物车
	 * @param shoppingCartDTO 添加的数据
	 */
	void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
	
	/**
	 * 查看购物车
	 * @return 购物车数据
	 */
	List<ShoppingCart> showShoppingCart();
}
