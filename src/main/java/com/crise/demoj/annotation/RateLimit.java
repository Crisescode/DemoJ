package com.crise.demoj.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    int count() default 10;
    long timeWindow() default 60;
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
