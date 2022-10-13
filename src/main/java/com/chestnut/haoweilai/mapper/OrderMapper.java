package com.chestnut.haoweilai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chestnut.haoweilai.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
