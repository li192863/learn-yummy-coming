package com.chestnut.haoweilai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chestnut.haoweilai.common.R;
import com.chestnut.haoweilai.dto.DishDto;
import com.chestnut.haoweilai.entity.Category;
import com.chestnut.haoweilai.entity.Dish;
import com.chestnut.haoweilai.entity.DishFlavor;
import com.chestnut.haoweilai.service.CategoryService;
import com.chestnut.haoweilai.service.DishFlavorService;
import com.chestnut.haoweilai.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询菜品信息
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        // 构造分页构造器
        Page<Dish> dishPage = new Page<>(page, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        lqw.orderByDesc(Dish::getUpdateTime);
        // 分页查询
        dishService.page(dishPage, lqw);

        // 复制dishPage除记录外信息
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
        // 处理dishPage记录信息
        List<Dish> records = dishPage.getRecords();
        List<DishDto> dishDtoList = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);  // 复制dish基本信息
            Category category = categoryService.getById(item.getCategoryId());
            dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList());
        // 复制dishPage处理后记录信息
        dishDtoPage.setRecords(dishDtoList);
        return R.success(dishDtoPage);
    }

    /**
     * 条件查询菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value = "dishCache", key = "#dish.categoryId")
    public R<List<DishDto>> list(Dish dish) {
        // 构造条件构造器
        LambdaQueryWrapper<Dish> dishLqw = new LambdaQueryWrapper<>();
        dishLqw.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        dishLqw.eq(Dish::getStatus, 1);
        dishLqw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        // 查询分类
        List<Dish> list = dishService.list(dishLqw);

        // 查询口味
        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);  // 复制dish基本信息
            // 设置种类
            Category category = categoryService.getById(item.getCategoryId());
            dishDto.setCategoryName(category.getName());
            // 设置口味
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLqw = new LambdaQueryWrapper<>();
            dishFlavorLqw.eq(dishId != null, DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorLqw);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }

    /**
     * 根据id获取菜品信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 添加菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "dishCache", allEntries = true)
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("添加菜品成功");
    }

    /**
     * 起售/停售菜品
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    @CacheEvict(value = "dishCache", allEntries = true)
    public R<String> updateStatus(@RequestParam List<Long> ids, @PathVariable int status) {
        dishService.updateStatus(ids, status);
        return R.success("更新菜品成功");
    }

    /**
     * 更新菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    @CacheEvict(value = "dishCache", allEntries = true)
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("更新菜品成功");
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(value = "dishCache", allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids) {
        dishService.removeWithFlavor(ids);
        return R.success("删除菜品成功");
    }
}
