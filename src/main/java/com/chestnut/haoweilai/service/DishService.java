package com.chestnut.haoweilai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chestnut.haoweilai.dto.DishDto;
import com.chestnut.haoweilai.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);

    DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);

    void updateStatus(List<Long> ids, int status);

    void removeWithFlavor(List<Long> ids);
}
