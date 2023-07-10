package com.chestnut.haoweilai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chestnut.haoweilai.exception.CustomException;
import com.chestnut.haoweilai.dto.SetmealDto;
import com.chestnut.haoweilai.entity.Dish;
import com.chestnut.haoweilai.entity.Setmeal;
import com.chestnut.haoweilai.entity.SetmealDish;
import com.chestnut.haoweilai.mapper.SetmealMapper;
import com.chestnut.haoweilai.service.DishService;
import com.chestnut.haoweilai.service.SetmealDishService;
import com.chestnut.haoweilai.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private DishService dishService;

    /**
     * 添加套餐，同时保存对应菜品数据
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存setmeal基本信息至菜品表（setmeal）
        this.save(setmealDto);
        // 保存setmeal菜品信息至菜品口味表（setmeal_dish）
        Long setmealId = setmealDto.getId();
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();
        dishes = dishes.stream().map((item) -> {
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(dishes);
    }

    /**
     * 根据id查询套餐，同时查询对应菜品数据
     * @param id
     * @return
     */
    @Override
    public SetmealDto getByIdWithDish(Long id) {
        // 查询setmeal基本信息
        Setmeal setmeal = this.getById(id);
        // 查询setmeal_dish菜品信息
        Long setmealId = setmeal.getId();
        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(setmealId != null, SetmealDish::getSetmealId, setmealId);
        List<SetmealDish> dishes = setmealDishService.list(lqw);
        // 复制属性
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        setmealDto.setSetmealDishes(dishes);
        return setmealDto;
    }

    /**
     * 更新套餐，同时更新对应菜品数据
     * @param setmealDto
     */
    @Override
    @Transactional
    public void updateWithDish(SetmealDto setmealDto) {
        // 更新setmeal基本信息
        this.updateById(setmealDto);
        // 清理setmeal_dish菜品信息
        Long setmealId = setmealDto.getId();
        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(setmealId != null, SetmealDish::getSetmealId, setmealId);
        setmealDishService.remove(lqw);
        // 更新setmeal_dish口味信息
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();
        dishes = dishes.stream().map((item) -> {
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(dishes);
    }

    /**
     * 删除套餐，同时删除对应菜品数据
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        // 查询当前套餐是否正在售卖中
        LambdaQueryWrapper<Setmeal> setmealLqw = new LambdaQueryWrapper<>();
        setmealLqw.in(Setmeal::getId, ids);
        setmealLqw.eq(Setmeal::getStatus, 1);
        int count = this.count(setmealLqw);
        if (count > 0) {
            // 抛出异常
            throw new CustomException("操作失败，当前套餐正在售卖中");
        }
        // 删除setmeal基本信息
        this.removeByIds(ids);
        // 删除setmeal_dish基本信息
        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        lqw.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lqw);
    }

    /**
     * 更新套餐状态（起售/停售）
     * @param ids
     * @param status
     */
    @Override
    public void updateStatus(List<Long> ids, int status) {
        if (status == 1) { // 启售某套餐时
            // 查询当前套餐关联菜品
            LambdaQueryWrapper<SetmealDish> setmealDishLqw = new LambdaQueryWrapper<>();
            setmealDishLqw.in(SetmealDish::getSetmealId, ids);
            List<SetmealDish> setmealDishes = setmealDishService.list(setmealDishLqw);
            for (SetmealDish setmealDish: setmealDishes) {
                Long dishId = setmealDish.getDishId();
                LambdaQueryWrapper<Dish> dishLqw = new LambdaQueryWrapper<>();
                dishLqw.eq(dishId != null, Dish::getId, dishId);
                Dish dish = dishService.getOne(dishLqw);
                if (dish.getStatus() == 0) {
                    // 抛出异常
                    throw new CustomException("操作失败，当前套餐关联菜品正在停售中");
                }
            }
        }

        SetmealDto setmealDto = new SetmealDto();
        setmealDto.setStatus(status);
        for (Long id: ids) {
            // 设置id
            setmealDto.setId(id);
            this.updateById(setmealDto);
        }
    }
}
