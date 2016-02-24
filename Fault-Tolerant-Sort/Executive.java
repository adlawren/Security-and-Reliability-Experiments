//package com.adlawren.fts;

import java.io.*;
import java.lang.*;
import java.util.*;

public class Executive extends Thread {

  String inputFileName = null,
          outputFileName = null;

  private double primaryFailureProbability = -1.0f,
                secondaryFailureProbability = -1.0f;

  private int timeLimit = -1;

  public Executive(String initialInputFileName,
                    String initialOutputFileName,
                    double initialPrimaryFailureProbability,
                    double initialSecondaryFailureProbability,
                    int initialTimeLimit) {

    inputFileName = initialInputFileName;
    outputFileName = initialOutputFileName;

    primaryFailureProbability = initialPrimaryFailureProbability;
    secondaryFailureProbability = initialSecondaryFailureProbability;

    timeLimit = initialTimeLimit;
  }

  public void run() {

    // int[] buf = FileManager();

    try {

      Thread toRun = new InfiniteThread(); //new PrimarySort();

      WatchdogTimer wdt = new WatchdogTimer(toRun, timeLimit);
      wdt.start();

      toRun.start();
      toRun.join();

      wdt.interrupt();
      if (wdt.timeElapsed()) {
        System.out.println("WDT time elapsed");
      } else {
        System.out.println("WDT time not elapsed");
      }

      // ...

      // Remove the output file if it exists, has a non-empty filename and is a file
      // ...

    } catch (InterruptedException e) {

      // ...
    }
  }
}
