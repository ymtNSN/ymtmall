package com.ymtmall.commons.lock.aspect;

import com.ymtmall.commons.lock.DistributedLock;
import com.ymtmall.commons.lock.annotation.CustomerLock;
import com.ymtmall.commons.lock.extension.ExtensionLoader;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by @author yangmingtian on 2019/12/19
 */
@Component
@Aspect
@EnableAspectJAutoProxy
public class DistributedLockAspect {

    public static final Logger LOGGER = LoggerFactory.getLogger(DistributedLockAspect.class);

    @Pointcut("@annotation(com.ymtmall.commons.lock.annotation.CustomerLock)")
    public void distributedLockPointCut() {

    }

    @Around(value = "distributedLockPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        final String lockKey = getLockKey(method, pjp.getArgs());
        return startLock(lockKey, pjp, method);
    }

    private Object startLock(String lockKey, ProceedingJoinPoint pjp, Method method) throws Throwable {
        CustomerLock annotation = method.getAnnotation(CustomerLock.class);
        boolean tryLock = annotation.tryLock();
        if (tryLock) {
            return tryLock(pjp, annotation, lockKey);
        } else {
            return lock(pjp, annotation, lockKey);
        }

    }

    private Object lock(ProceedingJoinPoint pjp, CustomerLock annotation, String lockKey) throws Throwable {
        int leaseTime = annotation.leaseTime();
        TimeUnit timeUnit = annotation.timeUnit();
        String type = annotation.lockType();
        DistributedLock distributedLock = getByType(type);
        try {
            distributedLock.lock(lockKey, timeUnit, leaseTime);
            return pjp.proceed();
        } finally {
            distributedLock.unlock(lockKey);
        }
    }

    private DistributedLock getByType(String type) {
        return (DistributedLock) ExtensionLoader.getExtensionLoader(DistributedLock.class).getExtension(type);
    }

    private Object tryLock(ProceedingJoinPoint pjp, CustomerLock customerLock, String lockKey) throws Throwable {
        int leaseTime = customerLock.leaseTime();
        int waitTime = customerLock.waitTime();
        TimeUnit timeUnit = customerLock.timeUnit();
        String type = customerLock.lockType();
        DistributedLock distributedLock = getByType(type);

        try {
            //如果等待时间为0，则redis如果获得不到锁就直接返回false
            if (waitTime == 0) {
                if (distributedLock.tryLock(lockKey)) {
                    return pjp.proceed();
                }
            } else {
                //如果等待时间不等于0，则redis最长的等待时间为waitTime
                // 如果没有获得锁则放回false
                //如果获得锁则放回true，让后上锁后自动释放锁时间leaseTime
                distributedLock.tryLock(lockKey, timeUnit, waitTime, leaseTime);
                return pjp.proceed();
            }
        } finally {
            distributedLock.unlock(lockKey);
        }
        return null;
    }

    /**
     * 描述: 分布式锁key
     *
     * @param method method
     * @param args   args
     * @return {@link String}
     * @author yangmingtian
     */
    private String getLockKey(Method method, Object[] args) {
        Objects.requireNonNull(method);
        CustomerLock annotation = method.getAnnotation(CustomerLock.class);
        String lockKey = parse(annotation.lockKey(), method, args),
                separator = annotation.separator(),
                prefix = annotation.lockPrefix(),
                suffix = annotation.lockSuffix();
        if (StringUtils.isBlank(lockKey)) {
            throw new IllegalArgumentException(String.format("lock [%s] is error", lockKey));
        }
        StringBuilder keyGenerator = new StringBuilder();
        if (StringUtils.isNotBlank(prefix)) {
            keyGenerator.append(prefix).append(separator);
        }
        keyGenerator.append(lockKey.trim());
        if (StringUtils.isNotBlank(suffix)) {
            keyGenerator.append(separator).append(suffix);
        }
        lockKey = keyGenerator.toString().trim();
        // key不允许为空
        if (StringUtils.isBlank(lockKey)) {
            throw new IllegalArgumentException("Can't get or generate lock accurately!");
        }
        LOGGER.info("generator lock_key [" + lockKey + "]");
        return lockKey;
    }

    private String parse(String lockKey, Method method, Object[] args) {

        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = u.getParameterNames(method);

        // 使用SPEL进行key解析
        SpelExpressionParser parser = new SpelExpressionParser();
        // SPEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return parser.parseExpression(lockKey).getValue(context, String.class);
    }

}
