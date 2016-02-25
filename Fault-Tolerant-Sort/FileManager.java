//package com.adlawren.fts;

import java.io.*;
import java.lang.*;
import java.util.*;

/*
 * @author adlawren
 *
 * This is the File Manager for the fault tolerant sorting application.
 */

public class FileManager {

  public static int[] readIntArrayFromFile(String fileName) throws FileNotFoundException, IOException {

    BufferedReader reader = new BufferedReader(new FileReader(fileName));
    String[] values = reader.readLine().split(" ");

    if (values.length == 1 && values[0].length() == 0) {
      return new int[0];
    }

    int[] buf = new int[values.length];
    for (int i = 0; i < buf.length; ++i) {
        buf[i] = Integer.parseInt(values[i]);
    }

    return buf;
  }

  public static void writeIntArrayToFile(String fileName, int[] array) throws IOException {

    FileWriter writer = new FileWriter(fileName);

    for (int i = 0; i < array.length; ++i) {

      writer.write(Integer.toString(array[i]));

      if (i != array.length - 1) {
        writer.write(" ");
      }
    }

    writer.write("\n");
    writer.flush();
    writer.close();
  }
}
