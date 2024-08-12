package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

/**
 * @author zkt
 */
public interface EmployeeService {
	
	/**
	 * 员工登录
	 *
	 * @param employeeLoginDTO 前端登录数据
	 * @return 员工信息
	 */
	Employee login(EmployeeLoginDTO employeeLoginDTO);
	
	/**
	 * 新增员工
	 *
	 * @param employeeDTO 前端数据
	 */
	void save(EmployeeDTO employeeDTO);
	
	/**
	 * 分页查询
	 *
	 * @param employeePageQueryDTO 前端数据
	 * @return 响应数据
	 */
	PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);
	
	/**
	 * 启用禁用员工账号
	 *
	 * @param status 需要设置的账号状态
	 * @param id     需要设置的账号id
	 */
	void startOrStop(Integer status, Long id);
}
