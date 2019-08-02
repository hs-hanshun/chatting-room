package cn.chat.room.site.service.impl;

import cn.chat.room.site.form.UserForm;
import cn.chat.room.site.mapper.UserMapper;
import cn.chat.room.site.pojo.User;
import cn.chat.room.site.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserByName(UserForm form) {
        return userMapper.findUserByName(form);
    }
}
