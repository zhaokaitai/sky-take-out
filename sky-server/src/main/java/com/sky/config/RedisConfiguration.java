package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/13
 * @description Redis配置类
 **/
@Configuration
@Slf4j
public class RedisConfiguration {
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		// 创建RedisTemplate对象
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		// 设置连接工厂
		template.setConnectionFactory(connectionFactory);
		// 创建JSON序列化工具
		GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
		// 设置Key的序列化
		template.setKeySerializer(RedisSerializer.string());
		template.setHashKeySerializer(RedisSerializer.string());
		// 设置Value的序列化
		template.setValueSerializer(jsonRedisSerializer);
		template.setHashValueSerializer(jsonRedisSerializer);
		// 返回
		return template;
	}
}