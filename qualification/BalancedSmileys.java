import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class BalancedSmileys {

	static Scanner in;
	static PrintStream out;

	public static void main(String[] args) throws FileNotFoundException {
		in = new Scanner(new File("balanced_smileystxt.txt"));
		out = new PrintStream("out2.txt");

		int numTests = in.nextInt();
		in.nextLine();

		for (int i = 0; i < numTests; i++) {
			String str = in.nextLine();

			int[] depths = new int[100];
			depths[0] = 1;

			boolean smileInit = false;
			for (char c : str.toCharArray()) {
				if (c == '(') {
					for (int j = 99; j > 0; j--) {
						depths[j] = (smileInit ? depths[j] : 0) + depths[j - 1];
					}
					if (!smileInit)
						depths[0] = 0;
				} else if (c == ')') {
					for (int j = 0; j < 99; j++) {
						depths[j] = (smileInit ? depths[j] : 0) + depths[j + 1];
					}
					if (!smileInit)
						depths[99] = 0;
				}
				smileInit = (c == ':');
			}

			out.println(String.format("Case #%d: %s", i + 1,
					depths[0] > 0 ? "YES" : "NO"));
		}
	}

}
