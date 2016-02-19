import java.io.*;
import java.lang.*;

/*
 * @author adlawren
 *
 * TODO: Add more details.
 * This is the data generator for the fault tolerant sorting application.
 */

public class DataGenerator {

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

		try {

      for (int i = 0; i < count; ++i) {

        if (i != count - 1) {
          writer.write(Integer.toString(i) + " ");
        } else {
          writer.write(Integer.toString(i));
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
