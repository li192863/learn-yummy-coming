package com.chestnut.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chestnut.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
