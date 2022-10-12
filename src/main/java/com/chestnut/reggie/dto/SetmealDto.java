package com.chestnut.reggie.dto;

import com.chestnut.reggie.entity.Setmeal;
import com.chestnut.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes; // 套餐菜品

    private String categoryName;  // 分类名称
}
