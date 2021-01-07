package com.example.auth.service.impl;

import com.example.auth.entity.User;
import com.example.auth.mapper.UserMapper;
import com.example.auth.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cdh
 * @since 2021-01-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
