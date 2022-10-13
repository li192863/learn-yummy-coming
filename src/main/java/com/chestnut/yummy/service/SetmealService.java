package com.chestnut.yummy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chestnut.yummy.dto.SetmealDto;
import com.chestnut.yummy.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    SetmealDto getByIdWithDish(Long id);

    void updateWithDish(SetmealDto setmealDto);

    void updateStatus(List<Long> ids, int status);

    void removeWithDish(List<Long> ids);
}
