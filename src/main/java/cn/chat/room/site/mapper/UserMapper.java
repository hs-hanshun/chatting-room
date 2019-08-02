package cn.chat.room.site.mapper;

import cn.chat.room.site.form.UserForm;
import cn.chat.room.site.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User findUserByName(UserForm form);
}