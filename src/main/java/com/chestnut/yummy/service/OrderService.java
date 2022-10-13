package com.chestnut.yummy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chestnut.yummy.entity.Orders;

public interface OrderService extends IService<Orders> {
    void submit(Orders orders);
}
