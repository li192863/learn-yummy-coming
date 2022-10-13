package com.chestnut.haoweilai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chestnut.haoweilai.entity.Orders;

public interface OrderService extends IService<Orders> {
    void submit(Orders orders);
}
