package com.walterjwhite.timeout;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Timeout enforcer, let's public methods run for 1s (by default, unless otherwise specified) then
 * interrupts them.
 */
@Aspect
public class TimeConstrainedMethodEnforcer {
  private static final Logger LOGGER = LoggerFactory.getLogger(TimeConstrainedMethodEnforcer.class);

  protected final transient ScheduledExecutorService interrupter =
      Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2 - 1);

  @Around("execution(public * *(..)) && !within(com.walterjwhite.timeout.aspects.TimeoutEnforcer)")
  public Object doPublicAround(ProceedingJoinPoint point) throws Throwable {
    final ScheduledFuture future = scheduleInterruption(new TimeConstrainedMethodCall(point));
    try {
      return point.proceed();
    } finally {
      cancel(future);
    }
  }

  /**
   * Attempts to cancel the interruption of the target method.
   *
   * @param future the future to cancel
   * @return false if the task could not be cancelled
   */
  protected boolean cancel(ScheduledFuture future) {
    try {
      if (!future.isCancelled() && !future.isDone()) {
        return future.cancel(true);
      }

      return false;
    } catch (Exception e) {
      LOGGER.trace("error cancelling task", e);
      return false;
    }
  }

  /**
   * Schedules the method call for interruption with the configured delay.
   *
   * @param timeConstrainedMethodCall the method call to interrupt.
   * @return
   */
  protected ScheduledFuture scheduleInterruption(
      TimeConstrainedMethodCall timeConstrainedMethodCall) {
    return interrupter.schedule(
        new TimeoutRunnable(timeConstrainedMethodCall),
        timeConstrainedMethodCall.getMaximumRuntime(),
        timeConstrainedMethodCall.getRuntimeTimeUnit());
  }

  /** Interrupts the target method. */
  private class TimeoutRunnable implements Runnable {
    protected final TimeConstrainedMethodCall timeConstrainedMethodCall;

    private TimeoutRunnable(TimeConstrainedMethodCall timeConstrainedMethodCall) {
      this.timeConstrainedMethodCall = timeConstrainedMethodCall;
    }

    @Override
    public void run() {
      timeConstrainedMethodCall.interrupt();
    }
  }
}
