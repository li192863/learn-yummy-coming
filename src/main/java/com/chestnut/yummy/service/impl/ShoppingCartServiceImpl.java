package com.chestnut.yummy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chestnut.yummy.entity.ShoppingCart;
import com.chestnut.yummy.mapper.ShoppingCartMapper;
import com.chestnut.yummy.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
