package com.ymtmall.commons.lock;

/**
 * Created by @author yangmingtian on 2019/12/19
 */
public class DistributedLockException extends Exception {

    private static final long serialVersionUID = -5968143950274054380L;

    public DistributedLockException() {
        super();
    }

    public DistributedLockException(String message) {
        super(message);
    }

    public DistributedLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public DistributedLockException(Throwable cause) {
        super(cause);
    }

    protected DistributedLockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
