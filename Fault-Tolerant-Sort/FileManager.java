//package com.adlawren.fts;

import java.io.*;
import java.lang.*;
import java.util.*;

public class FileManager {

  public static int[] readIntArrayFromFile(String fileName) throws FileNotFoundException, IOException {

    BufferedReader reader = new BufferedReader(new FileReader(fileName));
    String[] values = reader.readLine().split(" ");

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
