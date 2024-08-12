package com.sky.config;

import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 配置类，注册web层相关组件
 *
 * @author zkt
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
	
	@Resource
	private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;
	
	/**
	 * 注册自定义拦截器
	 *
	 * @param registry 拦截器注册
	 */
	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		log.info("开始注册自定义拦截器...");
		registry.addInterceptor(jwtTokenAdminInterceptor)
				.addPathPatterns("/admin/**")
				.excludePathPatterns("/admin/employee/login");
	}
	
	/**
	 * 通过knife4j生成接口文档
	 *
	 * @return Docket
	 */
	@Bean
	public Docket docket() {
		log.info("准备生成接口文档...");
		ApiInfo apiInfo = new ApiInfoBuilder()
				.title("苍穹外卖项目接口文档")
				.version("2.0")
				.description("苍穹外卖项目接口文档")
				.build();
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.sky.controller"))
				.paths(PathSelectors.any())
				.build();
	}
	
	/**
	 * 设置静态资源映射
	 *
	 * @param registry 拦截器注册
	 */
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		log.info("开始设置静态资源映射...");
		registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
	
	/*
	 * 解决druid 日志报错：discard long time none received connection:xxx
	 * */
	@PostConstruct
	public void setProperties() {
		System.setProperty("druid.mysql.usePingMethod", "false");
	}
	
	/**
	 * 扩展Spring MVC框架的消息转换器
	 *
	 * @param converters the list of configured converters to extend
	 */
	@Override
	protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		log.info("扩展消息转换器...");
		
		// 创建一个小希转换器对象
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		
		// 为消息转换器设置一个对象转换器，对象转换器可以将Java对象序列化为json数据
		converter.setObjectMapper(new JacksonObjectMapper());
		
		// 将自己的消息转换器加入到容器中
		converters.add(0, converter);
	}
}
