package com.chestnut.yummy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chestnut.yummy.common.R;
import com.chestnut.yummy.entity.Category;
import com.chestnut.yummy.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询分类信息
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        // 构造分页构造器
        Page<Category> categoryPage = new Page<>(page, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        // 分页查询
        categoryService.page(categoryPage, lqw);
        return R.success(categoryPage) ;
    }

    /**
     * 条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        // 构造条件构造器
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(category.getType() != null, Category::getType, category.getType());
        lqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        // 查询分类
        List<Category> list = categoryService.list(lqw);
        return R.success(list);
    }

    /**
     * 添加分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        // 保存分类
        categoryService.save(category);
        return R.success("添加分类成功");
    }

    /**
     * 更新分类
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        // 更新菜品信息
        categoryService.updateById(category);
        return R.success("更新分类成功");
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long id) {
        // 删除分类
        categoryService.remove(id);
        return R.success("删除分类成功");
    }
}
