package com.storyhasyou.kratos.handler.concurrencylock;

import com.storyhasyou.kratos.annotation.ConcurrencyLock;
import org.aspectj.lang.JoinPoint;

/**
 * @author fangxi created by 2023/10/18
 */
public interface ConcurrencyLockCallback {

    void execute(JoinPoint joinPoint, ConcurrencyLock concurrencyLock);

}
