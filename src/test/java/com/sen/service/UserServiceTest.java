package com.sen.service;

import com.sen.model.User;
import org.junit.Assert;
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
public class UserServiceTest {
    @Autowired
    private UserService service;

    @Test
    public void test(){
        User user=new User();
        user.setId(1);
        user.setName("zhang shan");
        user.setAge(18);
        user.setSex(1);

        service.insertTestOne(user);
        service.insertTestTwo(user);
        service.insertTestThree(user);
    }

    @Test
    public void insert() {
        User user=new User();
        user.setId(2);
        user.setName("zhang shan");
        user.setAge(18);

        Assert.assertTrue(service.insert(user));
    }
}
