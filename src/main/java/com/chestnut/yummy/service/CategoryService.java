package com.chestnut.yummy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chestnut.yummy.entity.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
