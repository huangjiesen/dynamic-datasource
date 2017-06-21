package com.sen.dao;

import com.sen.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author HuangJS
 * @date 2017/6/21 15:46.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {
    @Autowired
    private UserDao dao;
    @Test
    public void test(){
        User user=new User();
        user.setId(1);
        user.setName("zhang shan");
        user.setAge(18);
        user.setSex(1);

        dao.insert(user);
    }
}
