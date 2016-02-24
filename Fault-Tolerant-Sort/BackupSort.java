//package com.adlawren.fts;

import java.io.*;
import java.lang.*;
import java.util.*;

/*
 * @author adlawren
 *
 * TODO: Add more details.
 * This is the Backup Sort variant for the fault tolerant sorting application.
 */

public class BackupSort extends Thread {

  private int[] buf = null;

  private int[] heapSort(int[] unsortedBuf) {

    // Build heap
    IntMinHeap heap = new IntMinHeap();
    for (int i = 0; i < unsortedBuf.length; ++i) {
      heap.insert(buf[i]);
    }

    int[] sortedBuf = new int[buf.length];
    for (int i = 0; i < buf.length; ++i) {
      sortedBuf[i] = heap.removeMin();
    }

    return sortedBuf;
  }

  public BackupSort(int[] initialBuf) {
    buf = initialBuf;
  }

  public void run() {

    try {

      buf = heapSort(buf);

      Thread.sleep(60000); // Used for testing
    } catch (InterruptedException e) {

      // ...
    }
  }

  public int[] getArray() {
    return buf;
  }
}
