package com.sky.mapper;

import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author zkt
 */

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username 员工姓名
     * @return 员工对象
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

}
