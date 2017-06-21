package com.sen.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author HuangJS
 * @date 2017/6/21 16:16.
 */
//http://codecloud.net/20734.html
//https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE%E5%B1%9E%E6%80%A7%E5%88%97%E8%A1%A8
@Configuration
@EnableTransactionManagement
public class DataSourceConfig {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);
    /**  数据源1(库名：test1) */
    public static final String DS_ONE="dataSourceOne";
    /**  数据源2(库名：test2) */
    public static final String DS_TWO="dataSourceTwo";
    /**  数据源3(库名：test3) */
    public static final String DS_THREE ="dataSourceThree";

    /**
     * 使用@Primary注解为默认数据源
     * @return
     */
    @Primary
    @Bean(name = DS_ONE)
    @ConfigurationProperties(prefix = "db.source.db1")
    public DataSource dataSourceOne() {
        return new DruidDataSource();
    }

    @Bean(name = DS_TWO)
    @ConfigurationProperties(prefix = "db.source.db2")
    public DataSource dataSourceTwo() {
        return new DruidDataSource();
    }

    @Bean(name = DS_THREE)
    @ConfigurationProperties(prefix = "db.source.db3")
    public DataSource dataSourceThree() {
        return new DruidDataSource();
    }

    @Bean(name = "dynamicDataSource")
    public AbstractRoutingDataSource dynamicDataSource(
            @Qualifier(DS_ONE) DataSource dsOne,
            @Qualifier(DS_TWO) DataSource dsTwo,
            @Qualifier(DS_THREE) DataSource dsThree)
    {
        // 检查数据源、并输出日志
        logger.debug("dynamicDataSource check begin...");
        Assert.notNull(dsOne, "database(test1) datasource is null");
        Assert.notNull(dsTwo, "database(test2) datasource is null");
        Assert.notNull(dsThree, "database(test3) datasource is null");
        logger.debug("dynamicDataSource check end,status:OK");

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DS_ONE, dsOne);
        targetDataSources.put(DS_TWO, dsTwo);
        targetDataSources.put(DS_THREE, dsThree);

        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources);    //数据源
        dataSource.setDefaultTargetDataSource(dsOne);          //默认数据源
        return dataSource;
    }

    @Bean(name = "transactionManager")
    @ConditionalOnMissingBean
    public PlatformTransactionManager transactionManager(AbstractRoutingDataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }

    @Bean(name = "sqlSessionFactory")
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(AbstractRoutingDataSource dynamicDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dynamicDataSource);
        sessionFactory.setTypeAliasesPackage("com.sen.model,com.sen.dto");

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath*:mappers/**/*.xml"));
        // 目前没有用户mybatis-config.xml
        //sessionFactory.setConfigLocation(new DefaultResourceLoader().getResource("classpath:mybatis-config.xml"));

        Properties properties = new Properties();
        properties.put("dialect", "mysql");
        sessionFactory.setConfigurationProperties(properties);
        return sessionFactory.getObject();
    }
}
