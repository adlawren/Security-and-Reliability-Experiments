//package com.adlawren.fts;

import java.io.*;
import java.lang.*;
import java.util.*;

import java.util.concurrent.*;

/*
 * @author adlawren
 *
 * TODO: Add more details.
 * This is the Watchdog Timer for the fault tolerant sorting application.
 */

public class WatchdogTimer extends Thread {

  Thread monitoredThread = null;

  int killDelay = -1;
  boolean delayElapsed = false;

  public WatchdogTimer(Thread toMonitor, int delay) {

    monitoredThread = toMonitor;
    killDelay = delay;
  }

  public void run() {

    // Wait for the specified time before killing the thread
    try {

      Thread.sleep(killDelay);

      monitoredThread.stop();
      delayElapsed = true;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public boolean timeElapsed() {
    return delayElapsed;
  }

  // Needed??
  // public void updateMonitoredThread(Thread newThread) {
  //   monitoredThread = newThread;
  // }
}