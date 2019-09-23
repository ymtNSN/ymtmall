package com.ymtmall.commons.tool.zookeeperconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by @author yangmingtian on 2019/9/19
 */
@Data
@ConfigurationProperties(prefix = "lock.zookeeper",ignoreInvalidFields = true)
public class ZookeeperClientProperties {

    private String zkHosts;

    private int sessionTimeout = 30000;

    private int connectionTimeout = 30000;

    // 共享一个zk
    private boolean singleton = true;

    //全局path前缀，常用来区分不同的应用
    private String namespace;

    @Override
    public String toString(){
        return "zkHosts:"+zkHosts+",sessionTimeout:"+sessionTimeout+
                ",connectionTimeout:"+connectionTimeout+",singleton:"+
                singleton+",namespace"+namespace;
    }
}
