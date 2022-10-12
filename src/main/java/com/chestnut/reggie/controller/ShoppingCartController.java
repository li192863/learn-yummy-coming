package com.chestnut.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chestnut.reggie.common.BaseContext;
import com.chestnut.reggie.common.R;
import com.chestnut.reggie.entity.ShoppingCart;
import com.chestnut.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, userId);
        lqw.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(lqw);
        return R.success(list);
    }

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        // 设置用户id
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        // 查询当前菜品/套餐是否已在购物车
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(userId != null, ShoppingCart::getUserId, userId);
        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {  // 当前选项为菜品
            lqw.eq(ShoppingCart::getDishId, dishId);
        } else {  // 当前选项为套餐
            lqw.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart currentShoppingCart = shoppingCartService.getOne(lqw);
        // 处理购物车数量
        if (currentShoppingCart != null) {  // 若存在则数量加1
            currentShoppingCart.setNumber(currentShoppingCart.getNumber() + 1);
            shoppingCartService.updateById(currentShoppingCart);
        } else {  // 若不存在则添加
            shoppingCart.setNumber(1);  // 设置数量
            shoppingCart.setCreateTime(LocalDateTime.now());  // 设置时间
            shoppingCartService.save(shoppingCart);
            currentShoppingCart = shoppingCart;
        }
        return R.success(currentShoppingCart);
    }

    /**
     * 移除购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        // 设置用户id
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        // 查询当前菜品/套餐数量
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(userId != null, ShoppingCart::getUserId, userId);
        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {  // 当前选项为菜品
            lqw.eq(ShoppingCart::getDishId, dishId);
        } else {  // 当前选项为套餐
            lqw.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart currentShoppingCart = shoppingCartService.getOne(lqw);
        Integer number = currentShoppingCart.getNumber();
        // 处理购物车数量
        if (number > 1) {  // 若数量大于1则减1
            currentShoppingCart.setNumber(currentShoppingCart.getNumber() - 1);
            shoppingCartService.updateById(currentShoppingCart);
        } else {  // 若数量小于等于1则删除
            shoppingCartService.removeById(currentShoppingCart.getId());
        }
        return R.success(currentShoppingCart);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, userId);
        shoppingCartService.remove(lqw);
        return R.success("清空购物车成功");
    }
}
