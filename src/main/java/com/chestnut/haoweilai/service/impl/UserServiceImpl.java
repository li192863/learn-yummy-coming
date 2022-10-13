package com.chestnut.haoweilai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chestnut.haoweilai.entity.User;
import com.chestnut.haoweilai.mapper.UserMapper;
import com.chestnut.haoweilai.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
