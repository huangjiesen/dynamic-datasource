package com.sen.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author HuangJS
 * @date 2017/6/21 17:02.
 */
public class DataSourceContextHolder {
    // 线程本地环境
    private static final ThreadLocal<DataSourceContainer> contextHolder =  ThreadLocal.withInitial(DataSourceContainer::new);

    public static void setDataSource(String dataSource) {
        contextHolder.get().setServiceDataSource(dataSource);
    }

    public static String getDataSource() {
        return contextHolder.get().getDataSource();
    }

    public static void removeDataSource(String dataSource) {
        contextHolder.get().removeServiceDataSource(dataSource);
    }

    /**
     * 切换一次性的数据源
     * <pre>如果Service方法使用了<code>@Transactional</code>注解, 后续本方法设置的数据源以及其他方法使用<code>@DataSource</code>配置的数据源都将不会生效. 如果方法内要操作多个数据源, 就不能使用<code>@Transactional</code>注解.</pre>
     * @param dataSource
     */
    public static void setDisposable(String dataSource) {
        contextHolder.get().setDisposableDataSource(dataSource);
    }

    private static class DataSourceContainer{
        private static final Logger logger = LoggerFactory.getLogger(DataSourceContainer.class);
        private String serviceDataSource;
        private String disposableDataSource;

        void setServiceDataSource(String dataSource) {
            this.serviceDataSource = dataSource;
        }
        void removeServiceDataSource(String datasource) {
            if(serviceDataSource!=null && serviceDataSource.equals(datasource)) {
                serviceDataSource = null;
            }
        }

        void setDisposableDataSource(String dataSource) {
            this.disposableDataSource = dataSource;
        }


        /**
         * 调用这个方法, 如果当前有手动设置了一次性的数据源, 使用并删除
         * 否则使用@DataSource注解配置的数据源
         * @return
         */
        String getDataSource() {
            logger.info("请求数据库之前ThreadLocal中的数据源信息: {}",this.toString());

            //如果手动设置了一次性的数据源, 首先使用该数据源并删除
            if(disposableDataSource!=null){
                String temp = disposableDataSource;
                disposableDataSource = null;
                return temp;
            }
            //此时有通过@DataSource注解配置的数据源, 直接使用
            return serviceDataSource;
        }

        @Override
        public String toString() {
            return String.format("DataSourceContainer{serviceDataSource=%s,disposableDataSource=%s}", serviceDataSource, disposableDataSource);
        }
    }

}
