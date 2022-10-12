package com.chestnut.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chestnut.reggie.entity.Orders;

public interface OrderService extends IService<Orders> {
    void submit(Orders orders);
}
