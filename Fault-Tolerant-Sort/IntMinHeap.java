//package com.adlawren.fts;

import java.io.*;
import java.lang.*;
import java.util.*;

/*
 * @author adlawren
 *
 * TODO: Add more details.
 * This is the integer heap for the fault tolerant sorting application.
 */

public class IntMinHeap {

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
     nextIdx = 2 * i;

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
