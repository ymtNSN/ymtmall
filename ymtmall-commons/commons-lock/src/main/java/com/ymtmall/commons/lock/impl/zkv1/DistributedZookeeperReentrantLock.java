package com.ymtmall.commons.lock.impl.zkv1;

import com.ymtmall.commons.lock.ApplicationContextUtils;
import com.ymtmall.commons.lock.DistributedLock;
import com.ymtmall.commons.lock.DistributedLockException;
import com.ymtmall.commons.tool.zookeeperconfig.CuratorFrameworkClient;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * * 分布式锁的第二种实现
 * * @author: jerry
 * * @createDate: 20190814
 * * @description:集成Zookeeper分布式锁之可重入互斥锁,在redis 分布式提供的扩展方式上增加zk实现
 * * 使用配置方式:
 * * lock:
 * *   zookeeper:
 * *     zkHosts: zookeeper://zk.gpmall.com:2181,zookeeper://zk.gpmall.com:2181
 * *     sessionTimeout: 30000;
 * *     connectionTimeout: 30000;
 * *     ## 共享一个zk链接
 * *     singleton: true;
 * *     ## 全局path前缀,常用来区分不同的应用
 * *     namespace: zkLock
 * Created by @author yangmingtian on 2019/12/20
 */
public class DistributedZookeeperReentrantLock implements DistributedLock {


    public CuratorFrameworkClient curatorFrameworkClient;

    public ConcurrentHashMap<Thread, InterProcessMutex> locksMap = new ConcurrentHashMap<>();

    public DistributedZookeeperReentrantLock(CuratorFrameworkClient curatorFrameworkClient) {
        this.curatorFrameworkClient = ApplicationContextUtils.getClass(CuratorFrameworkClient.class);
    }


    @Override
    public void lock(String path) throws DistributedLockException {
        if (locksMap.get(Thread.currentThread()) == null) {
            //互斥可重入锁，个人理解interProcessMutex 经济是一个锁的节点，path 对应的节点才是一个唯一的锁对象
            InterProcessMutex interProcessMutex = new InterProcessMutex(curatorFrameworkClient.getZkClient(), path);
            //一致等待获得锁,会在path 下创建一个临时有序接点
            try {
                interProcessMutex.acquire();
            } catch (Exception e) {
                throw new DistributedLockException("zk-acquire加锁异常: ", e);
            }
            locksMap.put(Thread.currentThread(), interProcessMutex);
        } else {
            InterProcessMutex interProcessMutex = locksMap.get(Thread.currentThread());
            //一致等待获得锁
            try {
                interProcessMutex.acquire();
            } catch (Exception e) {
                throw new DistributedLockException("zk-acquire加锁异常: ", e);
            }
        }

    }

    @Override
    public boolean tryLock(String path) throws DistributedLockException {
        boolean theGetLock = tryLock(path, null, -1, -1);
        return theGetLock;
    }

    @Override
    public void lock(String path, TimeUnit unit, int waitTime) throws DistributedLockException {
        boolean theGetLock = tryLock(path, unit, waitTime, 0);
    }

    @Override
    public boolean tryLock(String path, TimeUnit unit, int waitTime, int leaseTime) throws DistributedLockException {
        if (locksMap.get(Thread.currentThread()) == null) {
            //互斥可重入锁，个人理解interProcessMutex 经济是一个锁的节点，path 对应的节点才是一个唯一的锁对象
            InterProcessMutex interProcessMutex = new InterProcessMutex(curatorFrameworkClient.getZkClient(), path);
            //等待一个事件，如果没有获得锁会删除节点并且放回fasle,会在path 下创建一个临时有序接点
            try {
                boolean theGetLock = interProcessMutex.acquire(waitTime, unit);
                if (theGetLock) {
                    locksMap.put(Thread.currentThread(), interProcessMutex);
                } else {
                    interProcessMutex = null;
                }
                return theGetLock;
            } catch (Exception e) {
                throw new DistributedLockException("zk-acquire(waitTime,unit)加锁异常: ", e);
            }
        } else {
            InterProcessMutex interProcessMutex = locksMap.get(Thread.currentThread());
            //一致等待获得锁
            try {
                return interProcessMutex.acquire(waitTime, unit);
            } catch (Exception e) {
                throw new DistributedLockException("zk-acquire(waitTime,unit)加锁异常: ", e);
            }

        }
    }

    @Override
    public void unlock(String lockKey) throws DistributedLockException {
        InterProcessMutex interProcessMutex = locksMap.get(Thread.currentThread());
        if (interProcessMutex != null) {
            try {
                interProcessMutex.release();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                locksMap.remove(Thread.currentThread());
            }
        }
    }
}
