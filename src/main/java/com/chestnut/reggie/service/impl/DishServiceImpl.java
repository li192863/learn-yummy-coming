package com.chestnut.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chestnut.reggie.common.CustomException;
import com.chestnut.reggie.dto.DishDto;
import com.chestnut.reggie.entity.Dish;
import com.chestnut.reggie.entity.DishFlavor;
import com.chestnut.reggie.entity.Setmeal;
import com.chestnut.reggie.entity.SetmealDish;
import com.chestnut.reggie.mapper.DishMapper;
import com.chestnut.reggie.service.DishFlavorService;
import com.chestnut.reggie.service.DishService;
import com.chestnut.reggie.service.SetmealDishService;
import com.chestnut.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 添加菜品，同时保存对应口味数据
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        // 保存dish基本信息至菜品表（dish）
        this.save(dishDto);
        // 保存dish口味信息至菜品口味表（dish_flavor）
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id查询菜品，同时查询对应口味数据
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        // 查询dish基本信息
        Dish dish = this.getById(id);
        // 查询dish_flavor口味信息
        Long dishId = dish.getId();
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(dishId != null, DishFlavor::getDishId, dishId);
        List<DishFlavor> flavors = dishFlavorService.list(lqw);
        // 复制属性
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    /**
     * 更新菜品，同时更新对应口味数据
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // 更新dish基本信息
        this.updateById(dishDto);
        // 清理dish_flavor口味信息
        Long dishId = dishDto.getId();
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(dishId != null, DishFlavor::getDishId, dishId);
        dishFlavorService.remove(lqw);
        // 更新dish_flavor口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 删除菜品，同时删除对应口味数据
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithFlavor(List<Long> ids) {
        // 查询当前菜品是否正在售卖中
        LambdaQueryWrapper<Dish> dishLqw = new LambdaQueryWrapper<>();
        dishLqw.in(Dish::getId, ids);
        dishLqw.eq(Dish::getStatus, 1);
        int count = this.count(dishLqw);
        if (count > 0) {
            // 抛出异常
            throw new CustomException("操作失败，当前菜品正在售卖中");
        }
        // 删除dish基本信息
        this.removeByIds(ids);
        // 删除dish_flavor基本信息
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(lqw);
    }

    /**
     * 更新菜品状态（起售/停售）
     * @param ids
     * @param status
     */
    @Override
    public void updateStatus(List<Long> ids, int status) {
        if (status == 0) { // 停售某菜品时
            // 查询当前菜品关联套餐
            LambdaQueryWrapper<SetmealDish> setmealDishLqw = new LambdaQueryWrapper<>();
            setmealDishLqw.in(SetmealDish::getDishId, ids);
            List<SetmealDish> setmealDishes = setmealDishService.list(setmealDishLqw);
            for (SetmealDish setmealDish: setmealDishes) {
                Long setmealId = setmealDish.getSetmealId();
                LambdaQueryWrapper<Setmeal> setmealLqw = new LambdaQueryWrapper<>();
                setmealLqw.eq(setmealId != null, Setmeal::getId, setmealId);
                Setmeal setmeal = setmealService.getOne(setmealLqw);
                if (setmeal.getStatus() == 1) {
                    // 抛出异常
                    throw new CustomException("操作失败，当前菜品关联套餐正在售卖中");
                }
            }
        }

        DishDto dishDto = new DishDto();
        dishDto.setStatus(status);
        for (Long id: ids) {
            // 设置id
            dishDto.setId(id);
            this.updateById(dishDto);
        }
    }
}
