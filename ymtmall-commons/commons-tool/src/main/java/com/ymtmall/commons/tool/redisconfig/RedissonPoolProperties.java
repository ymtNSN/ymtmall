package com.ymtmall.commons.tool.redisconfig;

import lombok.Data;

/**
 * Created by @author yangmingtian on 2019/9/9
 */
@Data
public class RedissonPoolProperties {

    /**
     * 连接池中的最大空闲连接
     **/
    private int maxIdle;

    /**
     * 最小连接数
     **/
    private int minIdle;

    /**
     * 连接池最大连接数
     **/
    private int maxActive;

    /**
     * 连接池最大阻塞等待时间
     **/
    private int maxWait;
}
