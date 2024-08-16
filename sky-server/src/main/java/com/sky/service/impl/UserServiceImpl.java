package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/16
 * @description 用户相关服务层实现类
 **/

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final WeChatProperties weChatProperties;
	private final UserMapper userMapper;
	
	private static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
	
	/**
	 * 微信登录
	 *
	 * @param userLoginDTO 登录信息
	 * @return 用户信息
	 */
	@Override
	public User wxLogin(UserLoginDTO userLoginDTO) {
		String openid = getOpenid(userLoginDTO.getCode());
		
		// 判断openid是否为空，如果为空表示登录失败，抛出业务异常
		if (openid == null || openid.isEmpty()) {
			throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
		}
		
		// 判断当前用户是否为新用户
		User user = userMapper.getByOpenid(openid);
		
		// 如果是新用户，自动完成注册
		if (user == null) {
			user = User.builder()
					.openid(openid)
					.createTime(LocalDateTime.now())
					.build();
			
			userMapper.insert(user);
		}
		
		// 返回这个用户对象
		return user;
	}
	
	/**
	 * 调用微信接口服务，获取微信用户的openid
	 *
	 * @param code 用户状态码
	 * @return openid
	 */
	private String getOpenid(String code) {
		// 调用微信接口服务，获得当前微信用户的openid
		Map<String, String> map = new HashMap<>();
		map.put("appid", weChatProperties.getAppid());
		map.put("secret", weChatProperties.getSecret());
		map.put("js_code", code);
		map.put("grant_type", "authorization_code");
		String json = HttpClientUtil.doGet(WX_LOGIN, map);
		
		JSONObject jsonObject = JSON.parseObject(json);
		
		return jsonObject.getString("openid");
	}
}