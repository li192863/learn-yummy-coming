package com.chestnut.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chestnut.reggie.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
