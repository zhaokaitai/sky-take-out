package com.sky.properties;

import com.sky.factory.ConfigPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/18
 * @description 百度相关属性文件
 **/

@Component
@ConfigurationProperties(prefix = "sky.baidu")
@PropertySource(value = "classpath:config/application-config.yml", factory = ConfigPropertySourceFactory.class)
@Data
public class BaiduProperties {
	private String ak;
}