package com.walterjwhite.timeout;

import java.time.Duration;

public interface TimeConstrainedMethodInvocation {
  Duration getAllowedExecutionDuration();
}
