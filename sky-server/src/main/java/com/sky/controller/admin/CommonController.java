package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.MinioUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/13
 * @description 通用接口
 **/
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
	
	@Resource
	private MinioUtil minioUtil;
	
	/**
	 * 文件上传
	 *
	 * @param file 文件
	 * @return 文件路径
	 */
	@PostMapping("/upload")
	@ApiOperation("文件上传")
	public Result<String> upload(MultipartFile file) {
		log.info("文件上传：{}", file);
		String fileUrl = minioUtil.upload(file);
		return Result.success(fileUrl);
	}
}