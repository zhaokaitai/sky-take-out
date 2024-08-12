package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author zkt
 */

@Mapper
public interface EmployeeMapper {
	
	/**
	 * 根据用户名查询员工
	 *
	 * @param username 员工姓名
	 * @return 员工对象
	 */
	@Select("select * from employee where username = #{username}")
	Employee getByUsername(String username);
	
	/**
	 * 插入员工数据
	 *
	 * @param employee 单个员工数据
	 */
	@Insert("insert into employee (name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, " +
			"update_user) values (#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime}," +
			"#{createUser},#{updateUser})")
	void insert(Employee employee);
	
	/**
	 * 分页查询
	 * @param employeePageQueryDTO 查询条件
	 * @return 查询结果
	 */
	Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);
	
	/**
	 * 根据主键动态修改属性
	 * @param employee 需要修改的实体类
	 */
	void update(Employee employee);
}
