package com.ymtmall.commons.lock.impl.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by @author yangmingtian on 2019/12/20
 */
public class LockBackGroundThread extends Thread {

    private Logger logger = LoggerFactory.getLogger(getClass());

    CuratorFramework client;

    public LockBackGroundThread(CuratorFramework client) {
        this.client = client;
        this.setDaemon(true);
        this.setName("ZkMutexDistributedLock---background");
    }

    @Override
    public synchronized void run() {
        super.run();
        try {
            while (true) {
                LockBckGroundConf conf = new LockBckGroundConf();
                deleteInvalidNode(conf);
                Thread.currentThread().wait(conf.getFrequency() * 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteInvalidNode(LockBckGroundConf conf) throws Exception {
        String projectDir = ZkMutexDistributedLockFactory.lockPath + ZkMutexDistributedLockFactory.projectName;
        Stat exitDir = client.checkExists().forPath(projectDir);
        if (exitDir == null) {
            logger.error("根目录尚未创建，本次清理结束--" + projectDir);
            return;
        }
        List<String> paths = client.getChildren().forPath(projectDir);
        Date date = new Date();
        paths.forEach(currPath -> {
            try {
                Stat stat = new Stat();
                if (stat.getMtime() < (date.getTime() - (conf.getBeforeTime() * 1000)) && stat.getNumChildren() == 0) {
                    client.delete().forPath(projectDir + currPath);
                    logger.info("删除路径: " + projectDir + currPath);
                }
            } catch (Exception e) {
                logger.error("删除节点失败：", e);
            }
        });
    }
}
