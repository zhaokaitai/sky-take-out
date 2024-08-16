package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/16
 * @description 用户相关服务层
 **/
public interface UserService {
	
	/**
	 * 微信登录
	 *
	 * @param userLoginDTO 登录信息
	 * @return 用户信息
	 */
	User wxLogin(UserLoginDTO userLoginDTO);
}
