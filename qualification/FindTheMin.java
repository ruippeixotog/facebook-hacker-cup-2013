import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FindTheMin {

	static Scanner in;
	static PrintStream out;

	public static void main(String[] args) throws FileNotFoundException {
		in = new Scanner(new File("find_the_mintxt.txt"));
		out = new PrintStream("out3.txt");

		int numTests = in.nextInt();
		in.nextLine();

		for (int t = 0; t < numTests; t++) {
			int n = in.nextInt(), k = in.nextInt();
			int a = in.nextInt(), b = in.nextInt(), c = in.nextInt(), r = in
					.nextInt();

			int[] m = new int[k];
			Map<Integer, Integer> map = new HashMap<>();
			m[0] = a;
			map.put(a, 1);
			for (int i = 1; i < k; i++) {
				m[i] = (int) ((b * (long) m[i - 1] + c) % r);
				Integer counter = map.get(m[i]);
				map.put(m[i], counter == null ? 1 : counter + 1);
			}

			int pointer = 0;
			while (map.get(pointer) != null)
				pointer++;

			int result = -1;
			int[] m2 = new int[k + 1];
			int override = -1;
			for (int i = 0; i <= k; i++) {
				if (override != -1) {
					m2[i] = override;
					override = -1;
				} else {
					m2[i] = pointer++;
				}
				map.put(m2[i], 1);

				if (i < k) {
					int toRemove = m[i];
					int toRemoveCount = map.get(toRemove);
					if (toRemoveCount == 1) {
						if (toRemove < pointer) {
							override = toRemove;
						}
						map.remove(toRemove);
					} else {
						map.put(toRemove, toRemoveCount - 1);
					}
				}

				if (k + i == n - 1) {
					result = m2[i];
					break;
				}

				while (map.get(pointer) != null)
					pointer++;
			}

			if (result == -1) {
				result = m2[(n - 1 - k) % (k + 1)];
			}

			out.println(String.format("Case #%d: %d", t + 1, result));
		}
	}
}
