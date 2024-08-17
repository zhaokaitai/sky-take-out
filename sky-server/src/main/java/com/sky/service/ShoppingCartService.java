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
	 *
	 * @param shoppingCartDTO 添加的数据
	 */
	void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
	
	/**
	 * 查看购物车
	 *
	 * @return 购物车数据
	 */
	List<ShoppingCart> showShoppingCart();
	
	/**
	 * 清空购物车
	 */
	void cleanShoppingCart();
	
	/**
	 * 删除购物车中的一个商品
	 * @param shoppingCartDTO 需要删除的商品
	 */
	void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
