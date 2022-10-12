package com.chestnut.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chestnut.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
