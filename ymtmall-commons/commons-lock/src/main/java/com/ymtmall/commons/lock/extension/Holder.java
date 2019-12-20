package com.ymtmall.commons.lock.extension;

/**
 * Created by @author yangmingtian on 2019/12/19
 */
public class Holder<T> {

    private volatile T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}
