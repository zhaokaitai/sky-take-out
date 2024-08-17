package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

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
}
