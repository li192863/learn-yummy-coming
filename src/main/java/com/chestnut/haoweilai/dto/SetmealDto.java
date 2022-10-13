package com.chestnut.haoweilai.dto;

import com.chestnut.haoweilai.entity.Setmeal;
import com.chestnut.haoweilai.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes; // 套餐菜品

    private String categoryName;  // 分类名称
}
