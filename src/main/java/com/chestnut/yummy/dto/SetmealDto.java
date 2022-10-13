package com.chestnut.yummy.dto;

import com.chestnut.yummy.entity.Setmeal;
import com.chestnut.yummy.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes; // 套餐菜品

    private String categoryName;  // 分类名称
}
