//package com.adlawren.fts;

import java.io.*;
import java.lang.*;
import java.util.*;

/*
 * @author adlawren
 *
 * This is the Adjudicator for the fault tolerant sorting application.
 */

public class Adjudicator {

  public static boolean acceptanceTest(int[] sortedBuf, int originalLength) {

    if (sortedBuf.length != originalLength) {
      return false;
    }

    if (sortedBuf.length <= 1) {
      return true;
    }

    for (int i = 1; i < sortedBuf.length; ++i) {
      if (sortedBuf[i - 1] > sortedBuf[i]) {
        return false;
      }
    }

    return true;
  }
}
