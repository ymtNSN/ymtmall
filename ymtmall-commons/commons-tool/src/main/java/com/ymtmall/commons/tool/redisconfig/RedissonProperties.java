package com.ymtmall.commons.tool.redisconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by @author yangmingtian on 2019/9/9
 */
@ConfigurationProperties(prefix = "spring.redisson",ignoreUnknownFields = false)
@Data
public class RedissonProperties {

    //连接地址
    private String address;

    private int database;

    /**
     * 等待节点回复命令的时间。该时间从命令发送成功时开始计时
     */
    private int timeout;

    private String password;

    private RedissonPoolProperties pool;
}
