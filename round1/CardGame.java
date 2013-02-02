import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

public class CardGame {
	
	static Scanner in;
	static PrintStream out;
	
	static final int MOD = 1000000007;

	public static void main(String[] args) throws FileNotFoundException {
		in = new Scanner(new File("in1.txt"));
		out = new PrintStream("out1.txt");
		
		int numTests = in.nextInt();
		in.nextLine();
		
		for (int t = 0; t < numTests; t++) {
			int n = in.nextInt();
			int k = in.nextInt();
			
			int[] a = new int[n];
			for(int i = 0; i < n; i++) {
				a[i] = in.nextInt();
			}
			Arrays.sort(a);
			int sum = 0;
			for(int i = k - 1; i < n; i++) {
				sum = modAdd(sum, modMult(a[i], modComb(i, k - 1)));
			}
			
			out.println(String.format("Case #%d: %d", t + 1, sum));
		}
	}
	
	private static int[][] combMem = new int[10001][10001];

	static int modComb(int n, int k) {
		if (k == 0 || k == n)
			return 1;
		if (k > n / 2)
			k = n - k;
		if (combMem[n][k - 1] == 0)
			combMem[n][k - 1] = modComb(n, k - 1);
		return modMult(combMem[n][k - 1], n + 1 - k) / k;
	}
	
	static int modAdd(int a, int b) {
		return (int) ((a + (long) b) % MOD);
	}
	
	static int modMult(int a, int b) {
		return (int) ((a * (long) b) % MOD);
	}
	
	
}
