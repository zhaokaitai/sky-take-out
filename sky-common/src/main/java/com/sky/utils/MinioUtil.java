package com.sky.utils;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.exception.UploadFailedException;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/13
 * @description 上传文件工具类
 **/

@Data
@AllArgsConstructor
@Slf4j
public class MinioUtil {
	
	private String endpoint;
	private String accessKey;
	private String secretKey;
	private String bucket;
	
	/**
	 * 文件上传
	 *
	 * @param file 文件
	 * @return 文件路径
	 */
	public String upload(MultipartFile file) {
		// 创建MinioClient实例
		MinioClient minioClient = MinioClient.builder()
				.endpoint(endpoint)
				.credentials(accessKey, secretKey)
				.build();
		// 上传
		if (file == null || file.isEmpty()) {
			return null;
		}
		// 文件名，使用 UUID 随机
		String path = UUID.randomUUID() + "-" + BaseContext.getCurrentId() + "-" + file.getOriginalFilename();
		try {
			minioClient.putObject(PutObjectArgs.builder()
					.bucket(bucket)
					.object(path)
					.stream(file.getInputStream(), file.getSize(), -1)
					.contentType(file.getContentType())
					.build());
		} catch (Exception e) {
			log.error("文件上传失败：{}", e.getMessage());
			throw new UploadFailedException(MessageConstant.UPLOAD_FAILED);
		}
		// 拼接路径
		return String.format("%s/%s/%s", endpoint, bucket, path);
	}
}