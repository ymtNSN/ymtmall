package com.ymtmall.commons.lock.extension.annotation;

import java.lang.annotation.*;

/**
 * @author yangmingtian
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface LockSpi {

    String value() default "";

}
