package com.walterjwhite.timeout;

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
    return new TimeConstrainedMethodCall(proceedingJoinPoint).call();
  }
}
