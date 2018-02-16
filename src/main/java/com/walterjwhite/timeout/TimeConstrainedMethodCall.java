package com.walterjwhite.timeout;

import com.walterjwhite.timeout.annotation.Timeout;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/** Method invocation that is to be time constrained. */
public class TimeConstrainedMethodCall {
  protected final transient Thread thread;

  protected final transient ProceedingJoinPoint proceedingJoinPoint;

  protected final long maximumRuntime;

  protected final TimeUnit runtimeTimeUnit;

  public TimeConstrainedMethodCall(final ProceedingJoinPoint cproceedingJoinPoint) {
    super();
    thread = Thread.currentThread();
    proceedingJoinPoint = cproceedingJoinPoint;

    final Method method =
        MethodSignature.class.cast(proceedingJoinPoint.getSignature()).getMethod();

    final Timeout timeout = method.getAnnotation(Timeout.class);
    if (timeout == null) {
      maximumRuntime = TimeoutDefaults.DEFAULT_METHOD_RUNTIME;
      runtimeTimeUnit = TimeoutDefaults.DEFAULT_TIME_UNIT;
    } else {
      maximumRuntime = timeout.value();
      runtimeTimeUnit = timeout.units();
    }
  }

  public Thread getThread() {
    return thread;
  }

  public ProceedingJoinPoint getProceedingJoinPoint() {
    return proceedingJoinPoint;
  }

  public long getMaximumRuntime() {
    return maximumRuntime;
  }

  public TimeUnit getRuntimeTimeUnit() {
    return runtimeTimeUnit;
  }

  public void interrupt() {
    if (thread.isAlive()) {
      thread.interrupt();
    }
  }
}
