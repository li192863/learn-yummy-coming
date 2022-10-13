package com.chestnut.haoweilai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chestnut.haoweilai.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
