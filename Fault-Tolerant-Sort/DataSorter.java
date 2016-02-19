import java.io.*;
import java.lang.*;

/*
 * @author adlawren
 *
 * TODO: Add more details.
 * This is the data sorter for the fault tolerant sorting application.
 */

public class DataSorter {

	public static void main(String[] args) {

		if (args.length != 5) {
			System.err.println("ERROR: Incorrect number of arguments");
			return;
		}

		int[] buf = new int[1000];
		for (int i = 1000 - 1; i >= 0; --i){
			buf[i] = i;
		}

		try {
			MySort s = new MySort();
			System.loadLibrary("sort");

			s.sort(buf);
		} catch (Exception e) {
				System.out.println("ERROR: Exception Occured.");
		}
	}
}
