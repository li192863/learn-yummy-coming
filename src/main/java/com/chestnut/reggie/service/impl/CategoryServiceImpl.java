package com.chestnut.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chestnut.reggie.common.CustomException;
import com.chestnut.reggie.entity.Category;
import com.chestnut.reggie.entity.Dish;
import com.chestnut.reggie.entity.Setmeal;
import com.chestnut.reggie.mapper.CategoryMapper;
import com.chestnut.reggie.service.CategoryService;
import com.chestnut.reggie.service.DishService;
import com.chestnut.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public void remove(Long id) {
        // 查询当前分类是否关联菜品
        LambdaQueryWrapper<Dish> dishLwq = new LambdaQueryWrapper<>();
        dishLwq.eq(Dish::getCategoryId, id);
        int dishCount = dishService.count(dishLwq);
        if (dishCount > 0) {
            // 抛出异常
            throw new CustomException("操作失败，当前分类下关联了菜品");
        }
        // 查询当前分类是否关联套餐
        LambdaQueryWrapper<Setmeal> setmealLwq = new LambdaQueryWrapper<>();
        setmealLwq.eq(Setmeal::getCategoryId, id);
        int setmealCount = setmealService.count(setmealLwq);
        if (setmealCount > 0) {
            // 抛出异常
            throw new CustomException("操作失败，当前分类下关联了套餐");
        }
        // 删除分类
        super.removeById(id);
    }
}
