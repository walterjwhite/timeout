package com.walterjwhite.timeout;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.aspectj.lang.ProceedingJoinPoint;

public class TimeConstrainedMethodCall {
  private static final transient ScheduledExecutorService INTERRUPTER_EXECUTOR_SERVICE =
      Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2 - 1);

  protected final transient ProceedingJoinPoint proceedingJoinPoint;

  protected final Duration allowedExecutionDuration;
  protected final Interrupter interrupter = new Interrupter(Thread.currentThread());

  public TimeConstrainedMethodCall(
      ProceedingJoinPoint proceedingJoinPoint, final Duration allowedExecutionDuration) {
    this.proceedingJoinPoint = proceedingJoinPoint;
    this.allowedExecutionDuration = allowedExecutionDuration;
  }

  public Object call() throws Throwable {
    final ScheduledFuture future = scheduleInterruption();
    try {
      return proceedingJoinPoint.proceed();
    } finally {
      cancelInterruption(future);
    }
  }

  /**
   * Attempts to cancel the interruption of the target method.
   *
   * @param future the future to cancel
   * @return false if the task could not be cancelled
   */
  protected boolean cancelInterruption(ScheduledFuture future) {
    if (!future.isCancelled() && !future.isDone()) {
      return future.cancel(true);
    }

    return true;
  }

  /**
   * Schedules the method call for interruption with the configured delay.
   *
   * @return the future queuedJob to cancel the current invocation
   */
  protected ScheduledFuture scheduleInterruption() {
    return INTERRUPTER_EXECUTOR_SERVICE.schedule(
        interrupter, allowedExecutionDuration.toNanos(), TimeUnit.NANOSECONDS);
  }
}
