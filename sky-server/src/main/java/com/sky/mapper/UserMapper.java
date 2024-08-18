package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/16
 * @description 用户相关mapper
 **/

@Mapper
public interface UserMapper {
	
	/**
	 * 根据openid查询用户
	 *
	 * @param openid openid
	 * @return 用户信息
	 */
	@Select("select * from user where openid = #{openid}")
	User getByOpenid(String openid);
	
	/**
	 * 插入数据
	 *
	 * @param user 用户信息
	 */
	void insert(User user);
	
	/**
	 * 根据id查询用户
	 *
	 * @param userId 用户id
	 * @return 用户信息
	 */
	@Select("select * from user where id = #{id}")
	User getById(Long userId);
}
