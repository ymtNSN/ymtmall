package com.ymtmall.commons.lock.impl.zk;

/**
 * Description: 分布式锁---后台线程配置中心
 * * 分布式锁是基于zk的锁，加锁时会在zk创建目录，在释放锁后，此目录并不会删掉。
 * * 因此需要定时任务，定时清理空目录。
 * Created by @author yangmingtian on 2019/12/20
 */
public class LockBckGroundConf {
    /**
     * 执行频率, 默认一小时一次, 单位秒
     */
    private Long frequency = 60 * 60L;
    /**
     * 删除几天前的数据, 默认1天前的数据, 单位秒
     */
    private Long beforeTime = 24 * 60 * 60L;

    public Long getFrequency() {
        return frequency;
    }

    public void setFrequency(Long frequency) {
        this.frequency = frequency;
    }

    public Long getBeforeTime() {
        return beforeTime;
    }

    public void setBeforeTime(Long beforeTime) {
        this.beforeTime = beforeTime;
    }
}
