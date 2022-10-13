package com.chestnut.haoweilai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chestnut.haoweilai.entity.ShoppingCart;
import com.chestnut.haoweilai.mapper.ShoppingCartMapper;
import com.chestnut.haoweilai.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
