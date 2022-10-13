package com.chestnut.yummy.dto;

import com.chestnut.yummy.entity.Dish;
import com.chestnut.yummy.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜品及口味
 */
@Data
public class DishDto extends Dish {  // DishDto继承自Dish，即拥有Dish的所有属性

    private List<DishFlavor> flavors = new ArrayList<>();  // 口味

    private String categoryName;  // 分类名称

    private Integer copies;
}
