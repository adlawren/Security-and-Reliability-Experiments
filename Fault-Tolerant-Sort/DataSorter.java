//package com.adlawren.fts;

import java.io.*;
import java.lang.*;
import java.util.*;

/*
 * @author adlawren
 *
 * This is the data sorter for the fault tolerant sorting application.
 */

public class DataSorter {

	public static void main(String[] args) {

		// Input Sanitation
    if (args.length != 5) {
      System.err.println("ERROR: Incorrect number of arguments");
      return;
    }

    File inputFile = new File(args[0]);
    if (!inputFile.exists() || !inputFile.isFile()) {

      System.err.println("ERROR: Input file does not exist");
      return;
    }

    if (args[1].length() < 1) {

      System.err.println("ERROR: Output filename must have non-zero length");
      return;
    }

    double primaryFailureProbability = -1.0f;
    try {

      primaryFailureProbability = Double.parseDouble(args[2]);

      if (Double.compare(primaryFailureProbability, 0.0f) < 0 || Double.compare(primaryFailureProbability, 1.0f) > 0) {
        throw new IllegalArgumentException();
      }
    } catch (Exception e) {

      System.err.println("ERROR: Failure while parsing probability of primary failure (third argument), is the given value a floating point number between 0 and 1?");
      return;
    }

    double secondaryFailureProbability = -1.0f;
    try {

      secondaryFailureProbability = Double.parseDouble(args[3]);

      if (Double.compare(secondaryFailureProbability, 0.0f) < 0 || Double.compare(secondaryFailureProbability, 1.0f) > 0) {
        throw new IllegalArgumentException();
      }
    } catch (Exception e) {

      System.err.println("ERROR: Failure while parsing probability of secondary failure (fourth argument), is the given value a floating point number between 0 and 1?");
      return;
    }

    int timeLimit = -1;
    try {

      timeLimit = Integer.parseInt(args[4]);

      if (timeLimit < 0) {
        throw new IllegalArgumentException();
      }
    } catch (Exception e) {

      System.err.println("ERROR: Failure while parsing time limite (fifth argument), is the given value a non-zero integer?");
      return;
    }

    Executive exec = new Executive(args[0],
                                    args[1],
                                    primaryFailureProbability,
                                    secondaryFailureProbability,
                                    timeLimit);

    exec.start();
    try {
      exec.join();
    } catch (InterruptedException e) {}
	}
}
