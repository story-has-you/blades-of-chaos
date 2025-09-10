package com.storyhasyou.kratos.handler.concurrencylock;

import com.storyhasyou.kratos.annotation.ConcurrencyLock;
import org.aspectj.lang.JoinPoint;

/**
 * @author fangxi created by 2023/10/18
 * 默认的抢不到锁的处理逻辑，什么都不做
 */
public class DefaultConcurrencyLockCallback implements ConcurrencyLockCallback {
    @Override
    public void execute(JoinPoint joinPoint, ConcurrencyLock concurrencyLock) {

    }
}
