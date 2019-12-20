package com.ymtmall.commons.lock;

import com.ymtmall.commons.lock.extension.annotation.LockSpi;

import java.util.concurrent.TimeUnit;

/**
 * @author yangmingtian
 */
@LockSpi("redis")
public interface DistributedLock {

    void lock(String key) throws DistributedLockException;

    boolean tryLock(String key) throws DistributedLockException;

    void lock(String lockKey, TimeUnit unit, int timeout) throws DistributedLockException;

    boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) throws DistributedLockException;

    void unlock(String lockKey) throws DistributedLockException;

}
