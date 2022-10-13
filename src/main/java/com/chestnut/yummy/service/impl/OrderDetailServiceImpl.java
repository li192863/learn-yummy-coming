package com.chestnut.yummy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chestnut.yummy.entity.OrderDetail;
import com.chestnut.yummy.mapper.OrderDetailMapper;
import com.chestnut.yummy.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
