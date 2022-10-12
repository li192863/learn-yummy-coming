package com.chestnut.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chestnut.reggie.dto.SetmealDto;
import com.chestnut.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    SetmealDto getByIdWithDish(Long id);

    void updateWithDish(SetmealDto setmealDto);

    void updateStatus(List<Long> ids, int status);

    void removeWithDish(List<Long> ids);
}
