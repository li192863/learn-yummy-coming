package com.chestnut.haoweilai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chestnut.haoweilai.entity.DishFlavor;
import com.chestnut.haoweilai.mapper.DishFlavorMapper;
import com.chestnut.haoweilai.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
