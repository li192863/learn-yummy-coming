package com.chestnut.haoweilai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chestnut.haoweilai.common.R;
import com.chestnut.haoweilai.entity.Employee;
import com.chestnut.haoweilai.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // 密码作md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        // 用户名查询数据库
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(lqw);

        // 若没有查询到则登录失败
        if (emp == null) {
            return R.error("用户名不存在");
        }
        // 若密码错误则登录失败
        if (!emp.getPassword().equals(password)) {
            return R.error("用户名或密码错误");
        }
        // 若员工状态异常则登录失败
        if (emp.getStatus() == 0) {
            return R.error("用户名已禁用");
        }
        // 登录成功，id存入session中
//        request.getSession().setAttribute("employee", emp.getId());
        redisTemplate.opsForValue().set("employee", emp.getId(), 1, TimeUnit.DAYS);

        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        // 清除session中的id
//        request.getSession().removeAttribute("employee");
        redisTemplate.delete("employee");
        return R.success("员工退出成功");
    }

    /**
     * 分页查询员工信息
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        // 构造分页构造器
        Page<Employee> employeePage = new Page<>(page, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        lqw.orderByDesc(Employee::getUpdateTime);
        // 分页查询
        employeeService.page(employeePage, lqw);
        return R.success(employeePage);
    }

    /**
     * 根据id获取员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }

    /**
     * 添加员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Employee employee) {
        // 设置初始密码为身份证后6位
        String password = DigestUtils.md5DigestAsHex(employee.getIdNumber().substring(12).getBytes());
        employee.setPassword(password);
        // 保存员工
        employeeService.save(employee);
        return R.success("添加员工成功");
    }

    /**
     * 更新员工
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee) {
        // 更新员工信息
        employeeService.updateById(employee);
        return R.success("更新员工成功");
    }
}
