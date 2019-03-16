package com.walterjwhite.timeout;

import java.time.Duration;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * TimeConstrained enforcer, let's public methods run for 1s (by default, unless otherwise
 * specified) then interrupts them.
 */
@Aspect
public class TimeConstrainedMethodEnforcer {
  @Around(
      "execution(* *(..)) && @annotation(com.walterjwhite.timeout.annotation.TimeConstrained) && !within(com.walterjwhite.timeout.*)")
  public Object doTimeConstrainedMethodCall(ProceedingJoinPoint proceedingJoinPoint)
      throws Throwable {
    final Duration allowedDuration = getAllowedDuration(proceedingJoinPoint);
    if (allowedDuration.isZero()) return proceedingJoinPoint.proceed();

    return new TimeConstrainedMethodCall(proceedingJoinPoint, allowedDuration).call();
  }

  private static Duration getAllowedDuration(ProceedingJoinPoint proceedingJoinPoint) {
    return ((TimeConstrainedMethodInvocation) proceedingJoinPoint.getThis())
        .getAllowedExecutionDuration();
  }
}
