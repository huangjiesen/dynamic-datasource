package com.sen.dao;

import com.sen.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author HuangJS
 * @date 2017/6/21 15:40.
 */
@Mapper
public interface UserDao {
    int insert(User user);
    int update(User user);

    User get(int id);
    int delete(int id);
}
