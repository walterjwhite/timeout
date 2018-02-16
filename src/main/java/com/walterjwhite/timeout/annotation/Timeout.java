package com.walterjwhite.timeout.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/** Default timeout for all public methods is 1 second. */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Timeout {
  int value();

  TimeUnit units();
}
