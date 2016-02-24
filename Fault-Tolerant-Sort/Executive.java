//package com.adlawren.fts;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.concurrent.*;
import java.nio.file.*;

/*
 * @author adlawren
 *
 * TODO: Add more details.
 * This is the Executive for the fault tolerant sorting application.
 */

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

    // Establish checkpoint
    int[] buf = null;
    try {
       buf = FileManager.readIntArrayFromFile(inputFileName);
    } catch (Exception e) {
      System.err.println("ERROR: Failed to establish checkpoint");
      return;
    }

    PrimarySort primarySort = new PrimarySort(buf);

    WatchdogTimer wdt = new WatchdogTimer(primarySort, timeLimit);
    wdt.start();

    try {

      primarySort.start();
      primarySort.join();

      wdt.interrupt();
      if (wdt.timeElapsed()) {
        throw new TimeoutException();
      }

      buf = primarySort.getArray();

      if (Adjudicator.acceptanceTest(buf, buf)) { // TODO: fix

        FileManager.writeIntArrayToFile(outputFileName, buf);

        // Determine whether or not to trigger failure
        double hazard = primarySort.getMemoryAccesses() * primaryFailureProbability;

        Random randGenerator = new Random(System.currentTimeMillis());
        double randomValue = randGenerator.nextDouble();
        if (Double.compare(randomValue, 0.5f) > 0 && Double.compare(randomValue, 0.5 + hazard) < 0) {
          throw new IOException();
        }

        System.out.println("Primary Success!! :D"); // Test
        return;
      }
    } catch (Exception e) {
      // ...
    }

    System.err.println("Primary sorting variant failed, running backup variant");

    // Restore checkpoint
    try {
       buf = FileManager.readIntArrayFromFile(inputFileName);
    } catch (Exception e) {
      System.err.println("ERROR: Failed to establish checkpoint");
      return;
    }

    BackupSort backupSort = new BackupSort(buf);

    wdt = new WatchdogTimer(backupSort, timeLimit);
    wdt.start();

    try {

      backupSort.start();
      backupSort.join();

      wdt.interrupt();
      if (wdt.timeElapsed()) {
        throw new TimeoutException();
      }

      buf = backupSort.getArray();

      if (Adjudicator.acceptanceTest(buf, buf)) { // TODO: fix

        FileManager.writeIntArrayToFile(outputFileName, buf);

        // Determine whether or not to trigger failure
        double hazard = backupSort.getMemoryAccesses() * secondaryFailureProbability;

        Random randGenerator = new Random(System.currentTimeMillis());
        double randomValue = randGenerator.nextDouble();
        if (Double.compare(randomValue, 0.5f) > 0 && Double.compare(randomValue, 0.5 + hazard) < 0) {
          throw new IOException();
        }

        System.out.println("Backup Success!! :D"); // Test
        return;
      }
    } catch (Exception e) {
      // ...
    }

    System.err.println("Both primary & secondary sorting variants have failed");

    // Remove the output file if it exists, has a non-empty filename and is a file
    try {
      Files.deleteIfExists(FileSystems.getDefault().getPath(outputFileName));
    } catch (IOException e) {
      // ...
    }
  }
}
