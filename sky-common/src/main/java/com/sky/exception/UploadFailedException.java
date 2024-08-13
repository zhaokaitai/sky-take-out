package com.sky.exception;

/**
 * @author 赵开泰
 * @program sky-take-out
 * @date 2024/8/13
 * @description 文件上传异常
 **/
public class UploadFailedException extends BaseException {
	public UploadFailedException(String msg) {
		super(msg);
	}
}