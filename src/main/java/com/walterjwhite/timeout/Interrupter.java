package com.walterjwhite.timeout;

/** Interrupts the target method, by targeting the thread. */
public class Interrupter implements Runnable {
  protected final transient Thread thread;

  public Interrupter(Thread thread) {
    this.thread = thread;
  }

  @Override
  public void run() {
    interrupt();
  }

  public void interrupt() {
    if (thread.isAlive()) {
      thread.interrupt();
    }
  }
}
