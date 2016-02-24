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

  public BackupSort(int[] initialBuf) {
    buf = initialBuf;
  }

  public void run() {

    try {

      NativeInsertionSort s = new NativeInsertionSort();
    	System.loadLibrary("insertionsort");

    	buf = s.insertionSort(buf);

      Thread.sleep(0); // Used for testing
    } catch (InterruptedException e) {

      // ...
    }
  }

  public int[] getArray() {
    return buf;
  }
}
