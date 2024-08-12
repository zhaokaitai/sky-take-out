package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;

/**
 * @author zkt
 */
public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO 前端登录数据
     * @return 员工信息
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);
    
    /**
     * 新增员工
     * @param employeeDTO 前端数据
     */
    void save(EmployeeDTO employeeDTO);
}
