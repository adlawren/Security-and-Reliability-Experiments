//package com.adlawren.fts;

import java.io.*;
import java.lang.*;
import java.util.*;

/*
 * @author adlawren
 *
 * TODO: Add more details.
 * This is the Adjudicator for the fault tolerant sorting application.
 */

public class Adjudicator {

  public static boolean acceptanceTest(int[] originalBuf, int[] sortedBuf) {

    if (originalBuf.length != sortedBuf.length) {
      return false;
    }

    if (originalBuf.length == 0) {
      return true;
    }

    if (originalBuf.length == 1) {
      if (originalBuf[0] == sortedBuf[0]) {
        return true;
      } else {
        return false;
      }
    }

    for (int i = 1; i < sortedBuf.length; ++i) {
      if (sortedBuf[i - 1] > sortedBuf[i]) {
        return false;
      }
    }

    return true;
  }
}
