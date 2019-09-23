package com.ymtmall.commons.tool.zookeeperconfig;

import org.apache.curator.framework.CuratorFrameworkFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by @author yangmingtian on 2019/9/19
 */
@Configuration
@EnableConfigurationProperties(value = ZookeeperClientProperties.class)
@ConditionalOnClass(value = CuratorFrameworkFactory.class)
public class ZookeeperAutoConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(CuratorFrameworkFactory.class);

    @Autowired
    ZookeeperClientProperties zookeeperClientProperties;

    @Bean
    public CuratorFrameworkClient createCuratorFrameworkClient(){
        LOGGER.info(zookeeperClientProperties.toString());
        return new CuratorFrameworkClient(zookeeperClientProperties);
    }
}
