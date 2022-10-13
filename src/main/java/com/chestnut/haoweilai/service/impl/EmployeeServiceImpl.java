package com.chestnut.haoweilai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chestnut.haoweilai.entity.Employee;
import com.chestnut.haoweilai.mapper.EmployeeMapper;
import com.chestnut.haoweilai.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
