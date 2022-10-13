package com.chestnut.haoweilai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chestnut.haoweilai.entity.OrderDetail;
import com.chestnut.haoweilai.mapper.OrderDetailMapper;
import com.chestnut.haoweilai.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
