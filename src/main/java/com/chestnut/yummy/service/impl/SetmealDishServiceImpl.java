package com.chestnut.yummy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chestnut.yummy.entity.SetmealDish;
import com.chestnut.yummy.mapper.SetmealDishMapper;
import com.chestnut.yummy.service.SetmealDishService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
