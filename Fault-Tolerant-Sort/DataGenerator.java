//package com.adlawren.fts;

import java.io.*;
import java.lang.*;
import java.util.*;

/*
 * @author adlawren
 *
 * This is the data generator for the fault tolerant sorting application.
 */

public class DataGenerator {

  private static final int RAND_MAX = 1000000;

	public static void main(String[] args) {

    if (args.length != 2) {
      System.err.println("ERROR: Incorrect number of arguments");
      return;
    }

    int count = -1;
    try {
      count = Integer.parseInt(args[1]);
    } catch (Exception e) {

      System.err.println("ERROR: Could not parse the numeric parameter.");
      return;
    }

    Random randGenerator = new Random(System.currentTimeMillis());
		try {

      int[] buf = new int[count];
      for (int i = 0; i < count; ++i) {
        buf[i] = randGenerator.nextInt(RAND_MAX);
      }

      FileManager.writeIntArrayToFile(args[0], buf);

		} catch (Exception e) {
      System.err.println("ERROR: Values could not be written to the file.");
		}
	}
}
