package com.chestnut.yummy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chestnut.yummy.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
