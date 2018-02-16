package com.walterjwhite.timeout;

import java.util.concurrent.TimeUnit;

public interface TimeoutDefaults {
  int DEFAULT_INITIAL_DELAY = 1;
  int DEFAULT_DELAY = 1;
  int DEFAULT_METHOD_RUNTIME = 1;
  TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;
}
