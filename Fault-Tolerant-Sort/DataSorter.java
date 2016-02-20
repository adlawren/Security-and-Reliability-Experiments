//package com.adlawren.fault.tolerant.sort;

import java.io.*;
import java.lang.*;
import java.util.*;

/*
 * @author adlawren
 *
 * TODO: Add more details.
 * This is the data sorter for the fault tolerant sorting application.
 */

class IntMinHeap {

 	ArrayList<Integer> heap = null;

 	private void percolateUp() {

 		for (int i = heap.size() - 1; i > 1; i /= 2) {

 			if (heap.get(i / 2) > heap.get(i)) {
 				Collections.swap(heap, i, i / 2);
 			} else {
 				break;
 			}
 		}
 	}

 	private void percolateDown() {

		int i = 1, nextIdx = -1, minIdx = -1;
		while (i < heap.size()) {

			minIdx = -1;

			nextIdx = new Double(Math.pow(2.0f, new Integer(i).doubleValue())).intValue();
			if ((nextIdx < heap.size()) && (heap.get(i) > heap.get(nextIdx))) {
				minIdx = nextIdx;
			}

			if ((nextIdx + 1 < heap.size()) && (heap.get(i) > heap.get(nextIdx + 1))) {
				if (minIdx != -1) {
					if (heap.get(minIdx) > heap.get(nextIdx + 1)) {
						minIdx = nextIdx + 1;
					}
				} else {
					minIdx = nextIdx + 1;
				}
			}

			if (minIdx != -1) {
				Collections.swap(heap, i, minIdx);
				i = minIdx;
			} else {
				break;
			}
		}
 	}

 	public IntMinHeap() {
 		heap = new ArrayList<Integer>();
 		heap.add(-1); // Heap items start at index 1
 	}

	public int size() {
		return heap.size() - 1;
	}

 	public void insert(int toAdd) {
     heap.add(toAdd);
     percolateUp();
 	}

 	public int removeMin() {

		int min = heap.get(1);
		heap.set(1, heap.get(heap.size() - 1));
		heap.remove(heap.size() - 1);

		percolateDown();

 		return min;
 	}

	public int[] toIntArray() {
		Integer[] intermediateArray = heap.toArray(new Integer[heap.size()]);

		int[] array = new int[heap.size()];
		for (int i = 0; i < heap.size(); ++i) {
			array[i] = intermediateArray[i].intValue();
		}

		return array;
	}

   public String toString() {

     StringBuilder builder = new StringBuilder();
     for (int i = 1; i < heap.size(); ++i) {
       builder.append(heap.get(i));

			 if (i < heap.size() - 1) {
				 builder.append(" ");
			 }
     }

     return builder.toString();
   }
 }

public class DataSorter {

	private static final int BUFFER_SIZE = 7;

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

		// TODO: uncomment; testing right now
		if (args.length != 5) {
			System.err.println("ERROR: Incorrect number of arguments");
			return;
		}

		int[] buf = null;
		BufferedReader reader = null;
		try {

			// Read Input Values
			reader = new BufferedReader(new FileReader(args[0]));
			String[] values = reader.readLine().split(" ");

			buf = new int[values.length];
			for (int i = 0; i < buf.length; ++i) {
				buf[i] = Integer.parseInt(values[i]);
			}

			// Sort Values
			int[] sortedBuf = heapSort(buf);

			// Test
			// buf = heapSort(buf);

			// Store Values in Output File
			FileWriter writer = new FileWriter(args[1]);
			for (int i = 0; i < sortedBuf.length; ++i) {

        writer.write(Integer.toString(sortedBuf[i]));

        if (i != sortedBuf.length - 1) {
          writer.write(" ");
        }
      }

      writer.write("\n");
      writer.flush();
      writer.close();

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
