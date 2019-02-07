package com.walterjwhite.timeout;

import java.time.Duration;
import org.aspectj.lang.ProceedingJoinPoint;

public class TimeConstrainedMethodCall extends AbstractTimeConstrainedMethodCall {
  public TimeConstrainedMethodCall(ProceedingJoinPoint proceedingJoinPoint) {
    super(proceedingJoinPoint);
  }

  @Override
  protected Duration getAllowedExecutionDuration() {
    final TimeConstrainedMethodInvocation timeConstrainedMethodInvocation =
        (TimeConstrainedMethodInvocation) proceedingJoinPoint.getThis();
    return timeConstrainedMethodInvocation.getAllowedExecutionDuration();
  }
}
