package com.chestnut.yummy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chestnut.yummy.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
