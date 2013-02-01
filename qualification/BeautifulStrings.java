import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

public class BeautifulStrings {

	static Scanner in;
	static PrintStream out;

	public static void main(String[] args) throws FileNotFoundException {
		in = new Scanner(new File("in1.txt"));
		out = new PrintStream("out1.txt");

		int numTests = in.nextInt();
		in.nextLine();
		
		for (int i = 0; i < numTests; i++) {
			String str = in.nextLine().toLowerCase();
			System.out.println(str);

			int[] freqs = new int[26];
			for (char c : str.toCharArray()) {
				if (c >= 'a' && c <= 'z')
					freqs[c - 'a']++;
			}
			Arrays.sort(freqs);

			int sum = 0;
			for (int j = 0; j < 26; j++) {
				sum += freqs[j] * (j + 1);
			}
			out.println(String.format("Case #%d: %d", i + 1, sum));
		}
	}
}
