//package com.adlawren.fts;

import java.io.*;
import java.lang.*;
import java.util.*;

/*
 * @author adlawren
 *
 * This is the Primary Sort variant for the fault tolerant sorting application.
 */

public class PrimarySort extends Thread {

  private int memoryAccessCount = 0;

  private int[] buf = null;

  private int[] heapSort(int[] unsortedBuf) {

    // Build heap
    IntMinHeap heap = new IntMinHeap();
    memoryAccessCount += 1;

    for (int i = 0; i < unsortedBuf.length; ++i) {
      heap.insert(buf[i]);
      memoryAccessCount += 1;
    }

    int[] sortedBuf = new int[buf.length];
    memoryAccessCount += 1;

    for (int i = 0; i < buf.length; ++i) {
      sortedBuf[i] = heap.removeMin();
      memoryAccessCount += 1;
    }

    memoryAccessCount += heap.getMemoryAccesses();

    return sortedBuf;
  }

  public PrimarySort(int[] initialBuf) {
    buf = initialBuf;
    memoryAccessCount += 1;
  }

  public void run() {

    try {
      buf = heapSort(buf);
      memoryAccessCount += 1;

      Thread.sleep(0);
    } catch (InterruptedException e) {}
  }

  public int[] getArray() {
    memoryAccessCount += 1;
    return buf;
  }

  public int getMemoryAccesses() {
    return memoryAccessCount + 1;
  }
}
