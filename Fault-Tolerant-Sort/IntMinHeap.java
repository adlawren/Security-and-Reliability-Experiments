//package com.adlawren.fts;

import java.io.*;
import java.lang.*;
import java.util.*;

/*
 * @author adlawren
 *
 * This is the integer heap for the fault tolerant sorting application.
 */

public class IntMinHeap {

	private int memoryAccessCount = 0;

	private ArrayList<Integer> heap = null;

	private void percolateUp() {

		for (int i = heap.size() - 1; i > 1; i /= 2) {

			if (heap.get(i / 2) > heap.get(i)) {
				Collections.swap(heap, i, i / 2);
				memoryAccessCount += 2;
			} else {
				break;
			}
		}
	}

	private void percolateDown() {

		int i = 1, nextIdx = -1, minIdx = -1;
		memoryAccessCount += 3;

		while (i < heap.size()) {

			minIdx = -1;
			nextIdx = 2 * i;
			memoryAccessCount += 2;

			if ((nextIdx < heap.size()) && (heap.get(i) > heap.get(nextIdx))) {
				minIdx = nextIdx;
				memoryAccessCount += 1;
			}

			if ((nextIdx + 1 < heap.size()) && (heap.get(i) > heap.get(nextIdx + 1))) {
				if (minIdx != -1) {
					if (heap.get(minIdx) > heap.get(nextIdx + 1)) {
						minIdx = nextIdx + 1;
						memoryAccessCount += 1;
					}
				} else {
					minIdx = nextIdx + 1;
					memoryAccessCount += 1;
				}
			}

			if (minIdx != -1) {
				Collections.swap(heap, i, minIdx);
				i = minIdx;

				memoryAccessCount += 2;
			} else {
				break;
			}
		}
	}

	public IntMinHeap() {
		heap = new ArrayList<Integer>();
		heap.add(-1); // Heap items start at index 1

		memoryAccessCount += 2;
	}

	public void insert(int toAdd) {
    heap.add(toAdd);
		memoryAccessCount += 1;

    percolateUp();
	}

	public int removeMin() {

		int min = heap.get(1);
		heap.set(1, heap.get(heap.size() - 1));
		heap.remove(heap.size() - 1);
		memoryAccessCount += 3;

		percolateDown();

		return min;
	}

	public int getMemoryAccesses() {
		return memoryAccessCount;
	}
}
