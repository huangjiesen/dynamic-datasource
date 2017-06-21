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
        if(userDao.insert(user)!=1){
            return false;
        }
        user.setAge(28);
        DataSourceContextHolder.setDataSource(DataSourceConfig.DS_TWO);
        if(userDao.insert(user)!=1){
            return false;
        }
        user.setAge(38);
        DataSourceContextHolder.setDataSource(DataSourceConfig.DS_THREE);
        return userDao.insert(user)==1;
    }
}
