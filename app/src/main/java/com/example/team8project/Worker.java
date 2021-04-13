package com.example.team8project;

import java.util.concurrent.CountDownLatch;

class Worker extends Thread {

  private final int delay;
  private final CountDownLatch latch;
  private final int Count;

  public Worker(int delay, CountDownLatch latch,
      int count) {
    this.Count = count;
    this.delay = delay;
    this.latch = latch;
  }

  @Override
  public void run() {
    try {
      Thread.sleep(delay);
      latch.countDown();

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
