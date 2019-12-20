package com.ymtmall.commons.lock.impl.zk;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by @author yangmingtian on 2019/12/20
 */
public class ZkMutexDistributedLockFactory {

    private static Logger logger = LoggerFactory.getLogger(ZkMutexDistributedLockFactory.class);

    protected final static String lockPath = "/ymtcommons_lock/curator_recipes_lock/";

    protected static String projectName;

    static CuratorFramework client = null;

    static synchronized InterProcessMutex getInterProcessMutex(String lockKey) {
        if (client == null) {
            init();
        }
        return new InterProcessMutex(client, lockPath + projectName + lockKey);
    }

    static synchronized CuratorFramework getCuratorClient() {
        if (client == null) {
            init();
        }
        return client;
    }

    private static void init() {
        if (client == null) {
            //TODO zk地址
            String IPAndPort = "";
            //TODO 项目名
            String projectName = "";
            if (StringUtils.isEmpty(IPAndPort) || StringUtils.isEmpty(projectName)) {
                logger.error("zk锁启动失败缺少配置--IP和端口号/项目名");
                throw new RuntimeException("zk锁启动异常--缺少配置--IP和端口号/项目名");
            }
            ZkMutexDistributedLockFactory.projectName = projectName + "/";

            client = CuratorFrameworkFactory.builder()
                    .connectString(IPAndPort)
                    .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                    .build();
            client.start();

            LockBackGroundThread lockBackGroundThread = new LockBackGroundThread(client);
            lockBackGroundThread.start();
        }
    }

}
