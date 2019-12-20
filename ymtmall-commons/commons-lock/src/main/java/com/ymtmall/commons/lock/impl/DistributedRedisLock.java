package com.ymtmall.commons.lock.impl;

import com.ymtmall.commons.lock.ApplicationContextUtils;
import com.ymtmall.commons.lock.DistributedLock;
import com.ymtmall.commons.lock.DistributedLockException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by @author yangmingtian on 2019/12/19
 */
public class DistributedRedisLock implements DistributedLock {

    private RedissonClient redissonClient;

    public DistributedRedisLock() {
        this.redissonClient = ApplicationContextUtils.getClass(RedissonClient.class);
    }

    @Override
    public void lock(String key) throws DistributedLockException {
        RLock lock = redissonClient.getLock(key);
        lock.lock();
    }

    @Override
    public boolean tryLock(String key) throws DistributedLockException {
        RLock lock = redissonClient.getLock(key);
        return lock.tryLock();
    }

    @Override
    public void lock(String lockKey, TimeUnit unit, int timeout) throws DistributedLockException {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
    }

    @Override
    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) throws DistributedLockException {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            throw new DistributedLockException("redis加锁异常: ", e);
        }
    }

    @Override
    public void unlock(String lockKey) throws DistributedLockException {
        RLock lock = redissonClient.getLock(lockKey);
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}
