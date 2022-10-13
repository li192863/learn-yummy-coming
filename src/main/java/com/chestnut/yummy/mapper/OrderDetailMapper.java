package com.chestnut.yummy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chestnut.yummy.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
