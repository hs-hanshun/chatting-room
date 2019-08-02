package cn.chat.room.site.service;

import cn.chat.room.site.form.UserForm;
import cn.chat.room.site.pojo.User;

public interface UserService {
    User findUserByName(UserForm form);
}
