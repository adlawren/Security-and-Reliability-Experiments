//package com.adlawren.fts;

import java.io.*;
import java.lang.*;
import java.util.*;

/*
 * @author adlawren
 *
 * This is the Backup Sort variant for the fault tolerant sorting application.
 */

public class BackupSort extends Thread {

  private int memoryAccessCount = 0;

  private int[] buf = null;

  public BackupSort(int[] initialBuf) {
    buf = initialBuf;
    memoryAccessCount += 1;
  }

  public void run() {

    try {

      NativeInsertionSort s = new NativeInsertionSort();
    	System.loadLibrary("insertionsort");

    	buf = s.insertionSort(buf);

      Thread.sleep(0);
    } catch (InterruptedException e) {}
  }

  public int[] getArray() {

    memoryAccessCount += buf.length - 1;
    return Arrays.copyOf(buf, buf.length - 1);
  }

  public int getMemoryAccesses() {
    return memoryAccessCount + 1;
  }
}
