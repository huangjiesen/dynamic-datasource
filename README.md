### 1. 多数据源的使用
#### 1. 数据源定义
数据源都定义在DataSourceConfig类中, 数据源的配置在application-datasource.yml里

#### 2. 使用DataSource注解切换数据源
用法:

```java
@DataSource(DataSourceConfig.DS_TWO)
public User get(int id){
    return userDao.get(id);
}
```

1. `@DataSource`配置的数据源有效范围在本方法内, 就是本方法内无论调用多少次DAO查询都会使用该数据源.

2. `@DataSource`注解只能加在Service方法上, 加在DAO上是无效的.

3. 在Service方法内调用本实例的其他方法, 将导致其他方法的`@DataSource`配置失效. 例如:

```java
@DataSource(DataSourceConfig.DS_TWO)
public User method1(User user){
    //此处使用数据源DS_TWO
    userDao.insert(user);
    //调用method2不会使用数据源DS_THREE
    return method2(user.getId());
}

@DataSource(DataSourceConfig.DS_THREE)
public User method2(int id){
    return userDao.get(id);
}
```

因为在method1方法内部调用method2, 导致AOP失效, `@DataSource`注解不会被读取, 所以method2执行的时候依然使用DS_TWO数据源.
这种情况需要用到下面的手动切换一次性的数据源.

#### 3. 手动切换一次性数据源
用法:

```java
DataSourceContextHolder.setDataSource(DataSourceConfig.DS_THREE);
```

上面的例子中可以使用`DataSourceContextHolder.setDataSource(DataSourceConfig.DS_THREE);`方法完成数据源的切换.
示例代码:

```java
@DataSource(DataSourceConfig.DS_TWO)
public User method1(User user){
    //此处使用数据源DS_TWO
    userDao.insert(user);
    //此处手动设置数据源为DS_THREE
    DataSourceContextHolder.setDataSource(DataSourceConfig.DS_THREE);
    return method2(user.getId());
}

@DataSource(DataSourceConfig.DS_THREE)
public User method2(int id){
    return userDao.get(id);
}
```

`DataSourceContextHolder.setDataSource`设置数据源只能用于一次DAO查询, 例如上面的代码, method2()在执行完查询之后,
DS_THREE数据源就失效了, 下一次再查询的时候依然会使用DS_TWO数据源.

#### 4.@Transactional注解对数据源的影响
如果Service方法使用了`@Transactional`注解, 后续手动设置的数据源以及其他方法使用`@DataSource`配置的数据源都将不会生效.
如果方法内要操作多个数据源, 就不能使用`@Transactional`注解.

