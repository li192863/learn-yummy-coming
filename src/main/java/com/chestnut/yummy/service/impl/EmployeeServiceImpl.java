package com.chestnut.yummy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chestnut.yummy.entity.Employee;
import com.chestnut.yummy.mapper.EmployeeMapper;
import com.chestnut.yummy.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
