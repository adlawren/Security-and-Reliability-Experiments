//package com.adlawren.fts;

import java.io.*;
import java.lang.*;
import java.util.*;

/*
 * @author adlawren
 *
 * TODO: Add more details.
 * This is the data sorter for the fault tolerant sorting application.
 */

public class DataSorter {

	private static int[] heapSort(int[] buf) {

		IntMinHeap heap = new IntMinHeap();
		for (int i = 0; i < buf.length; ++i) {
			heap.insert(buf[i]);
		}

		int[] sortedBuf = new int[buf.length];
		for (int i = 0; i < buf.length; ++i) {
			sortedBuf[i] = heap.removeMin();
		}

		return sortedBuf;
	}

	public static void main(String[] args) {

		// if (args.length != 5) {
		// 	System.err.println("ERROR: Incorrect number of arguments");
		// 	return;
		// }
    //
		// int[] buf = null;
		// BufferedReader reader = null;
		// try {
    //
		// 	// Read Input Values
    //   buf = FileManager.readIntArrayFromFile(args[0]);
    //
		// 	// Sort Values
		// 	int[] sortedBuf = heapSort(buf);
    //
    //   // Write sorted values to output file
    //   FileManager.writeIntArrayToFile(args[1], sortedBuf);
    //
		// } catch (Exception e) {
		// 	System.out.println("ERROR: Exception Occured.");
		// }
    //
		// try {
    //
		// 	MySort s = new MySort();
		// 	System.loadLibrary("sort");
    //
		// 	System.out.println("Before: " + Arrays.toString(buf));
    //
		// 	int[] testBuf = s.sort(buf);
    //
		// 	System.out.println("After: " + Arrays.toString(testBuf));
		// } catch (Exception e) {
		// 		System.out.println("ERROR: Exception Occured.");
		// }

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

    // Needed?
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
    } catch (InterruptedException e) {

      // ...
    }
	}
}
