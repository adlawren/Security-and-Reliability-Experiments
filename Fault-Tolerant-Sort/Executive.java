//package com.adlawren.fts;

import java.io.*;
import java.lang.*;
import java.util.*;

public class Executive extends Thread {

  private int threadTimeLimit = -1;

  public Executive(int timeLimit) {

    threadTimeLimit = timeLimit;
  }

  public void run() {

    try {

      Thread toRun = new InfiniteThread(); //new PrimarySort();

      WatchdogTimer wdt = new WatchdogTimer(toRun, threadTimeLimit);
      wdt.start();

      toRun.start();
      toRun.join();

      wdt.interrupt();
      if (wdt.timeElapsed()) {
        System.out.println("WDT time elapsed");
      } else {
        System.out.println("WDT time not elapsed");
      }
    } catch (InterruptedException e) {

      // ...
    }
  }
}
