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

		if (args.length != 5) {
			System.err.println("ERROR: Incorrect number of arguments");
			return;
		}

		int[] buf = null;
		BufferedReader reader = null;
		try {

			// Read Input Values
      buf = FileManager.readIntArrayFromFile(args[0]);

			// Sort Values
			int[] sortedBuf = heapSort(buf);

      // Write sorted values to output file
      FileManager.writeIntArrayToFile(args[1], sortedBuf);

		} catch (Exception e) {
			System.out.println("ERROR: Exception Occured.");
		}

		try {

			MySort s = new MySort();
			System.loadLibrary("sort");

			System.out.println("Before: " + Arrays.toString(buf));

			int[] testBuf = s.sort(buf);

			System.out.println("After: " + Arrays.toString(testBuf));
		} catch (Exception e) {
				System.out.println("ERROR: Exception Occured.");
		}
	}
}
