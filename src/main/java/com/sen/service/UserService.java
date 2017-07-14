package com.sen.service;

import com.sen.config.DataSource;
import com.sen.config.DataSourceConfig;
import com.sen.config.DataSourceContextHolder;
import com.sen.dao.UserDao;
import com.sen.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HuangJS
 * @date 2017/6/21 20:36.
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public boolean insertTestOne(User user) {
        return userDao.insert(user)==1;
    }

    @DataSource(DataSourceConfig.DS_TWO)
    public boolean insertTestTwo(User user) {
        return userDao.insert(user)==1;
    }

    @DataSource(DataSourceConfig.DS_THREE)
    public boolean insertTestThree(User user) {
        return userDao.insert(user)==1;
    }


    public boolean insert(User user) {
        // 插入数据库test1
        if(userDao.insert(user)!=1){
            return false;
        }

        //
        // 切换数据源、向数据库test2插入数据
        // 修改age为28、以便观察数据
        //
        user.setAge(28);
        DataSourceContextHolder.setDataSource(DataSourceConfig.DS_TWO);
        if(userDao.insert(user)!=1){
            return false;
        }

        //
        // 切换数据源、向数据库test3插入数据
        // 修改age为38、以便观察数据
        //
        user.setAge(38);
        DataSourceContextHolder.setDataSource(DataSourceConfig.DS_THREE);
        return userDao.insert(user)==1;
    }
}
