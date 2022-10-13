package com.chestnut.yummy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chestnut.yummy.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
