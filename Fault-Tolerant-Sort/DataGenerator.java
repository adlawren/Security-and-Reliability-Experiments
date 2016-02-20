import java.io.*;
import java.lang.*;
import java.util.*;

/*
 * @author adlawren
 *
 * TODO: Add more details.
 * This is the data generator for the fault tolerant sorting application.
 */

public class DataGenerator {

  private static final int RAND_MAX = 100;

	public static void main(String[] args) {

    if (args.length != 2) {
      System.err.println("ERROR: Incorrect number of arguments");
      return;
    }

    FileWriter writer = null;
    try {
      writer = new FileWriter(args[0]);
    } catch (Exception e) {

      System.err.println("ERROR: File could not be created.");
      return;
    }

    int count = -1;
    try {
      count = Integer.parseInt(args[1]);
    } catch (Exception e) {

      System.err.println("ERROR: Could not parse the numeric parameter.");
      return;
    }

    Random randGenerator = new Random(System.currentTimeMillis());
		try {

      for (int i = 0; i < count; ++i) {

        int nextRand = randGenerator.nextInt(RAND_MAX);
        writer.write(Integer.toString(nextRand));

        if (i != count - 1) {
          writer.write(" ");
        }
      }

      writer.write("\n");
      writer.flush();
      writer.close();

		} catch (Exception e) {
      System.err.println("ERROR: Values could not be written to the file.");
		}
	}
}
