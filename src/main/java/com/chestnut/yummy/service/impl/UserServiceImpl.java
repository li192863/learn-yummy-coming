package com.chestnut.yummy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chestnut.yummy.entity.User;
import com.chestnut.yummy.mapper.UserMapper;
import com.chestnut.yummy.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
