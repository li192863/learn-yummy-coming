package com.chestnut.yummy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chestnut.yummy.common.BaseContext;
import com.chestnut.yummy.common.R;
import com.chestnut.yummy.dto.OrdersDto;
import com.chestnut.yummy.entity.OrderDetail;
import com.chestnut.yummy.entity.Orders;
import com.chestnut.yummy.service.OrderDetailService;
import com.chestnut.yummy.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 分页查询订单信息
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number, String beginTime, String endTime) {
        // 构造分页构造器
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotEmpty(number), Orders::getNumber, number);
        lqw.ge(beginTime != null, Orders::getOrderTime, beginTime);
        lqw.le(endTime != null, Orders::getOrderTime, endTime);
        // 分页查询
        orderService.page(ordersPage, lqw);
        return R.success(ordersPage);
    }

    /**
     * 前台分页查询订单信息
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize) {
        // 获得当前用户id
        Long userId = BaseContext.getCurrentId();
        // 构造分页构造器
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Orders> ordersLqw = new LambdaQueryWrapper<>();
        ordersLqw.eq(userId != null, Orders::getUserId, userId);
        ordersLqw.orderByDesc(Orders::getCheckoutTime);
        // 分页查询
        orderService.page(ordersPage, ordersLqw);

        // 复制orderPage除记录外信息
        Page<OrdersDto> ordersDtoPage = new Page<>(page, pageSize);
        BeanUtils.copyProperties(ordersPage, ordersDtoPage, "records");
        // 处理orderPage记录信息
        List<Orders> records = ordersPage.getRecords();
        List<OrdersDto> ordersDtoList = records.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);  // 复制order基本信息
            // 查询订单orderDetail
            Long orderId = item.getId();
            LambdaQueryWrapper<OrderDetail> orderDetailLqw = new LambdaQueryWrapper<>();
            orderDetailLqw.eq(orderId != null, OrderDetail::getOrderId, orderId);
            List<OrderDetail> orderDetails = orderDetailService.list(orderDetailLqw);
            ordersDto.setOrderDetails(orderDetails);
            return ordersDto;
        }).collect(Collectors.toList());
        ordersDtoPage.setRecords(ordersDtoList);
        return R.success(ordersDtoPage);
    }

    /**
     * 条件查询订单数据
     * @return
     */
    @GetMapping("/list")
    public R<List<Orders>> list() {
        // 构造条件构造器
        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(Orders::getCheckoutTime);
        // 查询分类
        List<Orders> list = orderService.list(lqw);
        return R.success(list);
    }

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        orderService.submit(orders);
        return R.success("用户下单成功");
    }

    /**
     * 派送套餐
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> updateStatus(@RequestBody Orders orders) {
        orderService.updateById(orders);
//        String[] statusTable = {"待付款", "2待派送", "3已派送", "4已完成", "已取消"};
        return R.success("派送成功");
    }
}
