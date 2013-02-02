import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class DeadPixels {

	static Scanner in;
	static PrintStream out;

	static class Pixel implements Comparable<Pixel> {
		int x, y;

		public Pixel(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(Pixel o) {
			if (x != o.x)
				return x - o.x;
			else
				return y - o.y;
		}

		@Override
		public String toString() {
			return "Pixel [x=" + x + ", y=" + y + "]";
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		in = new Scanner(new File("in3.txt"));
		out = new PrintStream("out3.txt");

		int numTests = in.nextInt();
		in.nextLine();

		for (int t = 0; t < numTests; t++) {
			int W = in.nextInt(), H = in.nextInt(), P = in.nextInt(), Q = in
					.nextInt(), N = in.nextInt(), X = in.nextInt(), Y = in
					.nextInt(), a = in.nextInt(), b = in.nextInt(), c = in
					.nextInt(), d = in.nextInt();

			int[] x = new int[N];
			int[] y = new int[N];
			Pixel[] pixels = new Pixel[N];
			TreeSet<Pixel> tree = new TreeSet<>();

			x[0] = X;
			y[0] = Y;
			pixels[0] = new Pixel(X, Y);
			tree.add(pixels[0]);
			for (int i = 1; i < N; i++) {
				x[i] = (x[i - 1] * a + y[i - 1] * b + 1) % W;
				y[i] = (x[i - 1] * c + y[i - 1] * d + 1) % H;
				pixels[i] = new Pixel(x[i], y[i]);
				tree.add(pixels[i]);
			}
			Arrays.sort(pixels);
//			System.out.println("Screen: " + W + "x" + H + "; pic: " + P + "x" + Q);
//			System.out.println(tree);

			long result = safeLocations(pixels, tree, 0, 0, W, H, P, Q);
			out.println(String.format("Case #%d: %d", t + 1, result));
		}
	}

	static long safeLocations(Pixel[] pixels, TreeSet<Pixel> tree, int x0,
			int y0, int w, int h, int p, int q) {
		if (w < p || h < q)
			return 0;

		int mid = x0 + (w / 2);
		SortedSet<Pixel> set = tree.tailSet(new Pixel(mid, 0));
		Pixel midRight = new Pixel(Integer.MAX_VALUE / 2, 0);
		while (!set.isEmpty()) {
			Pixel pix = set.first();
			if (pix.y >= y0 && pix.y < y0 + h) {
				midRight = pix;
				break;
			}
			set = set.tailSet(new Pixel(pix.x, pix.y + 1));
		}

		set = tree.headSet(new Pixel(mid, 0));
		Pixel midLeft = new Pixel(Integer.MIN_VALUE / 2, 0);
		while (!set.isEmpty()) {
			Pixel pix = set.last();
			if (pix.y >= y0 && pix.y < y0 + h) {
				midLeft = pix;
				break;
			}
			set = set.headSet(pix);
		}

		if (midLeft.x < x0 && midRight.x >= x0 + w) {
			return (w - p + 1) * (h - q + 1);
		}
		Pixel pivot = mid - midLeft.x < midRight.x - mid ? midLeft : midRight;

		int vertX0 = Math.max(x0, pivot.x - p + 1);
		int vertX1 = Math.min(x0 + w, pivot.x + p);

		int[][] subParams = { { x0, y0, pivot.x - x0, h },
				{ pivot.x + 1, y0, x0 + w - pivot.x - 1, h },
				{ vertX0, y0, vertX1 - vertX0, pivot.y - y0 },
				{ vertX0, pivot.y + 1, vertX1 - vertX0, h - pivot.y + y0 - 1 } };

		long results = 0;
		for (int[] pa : subParams) {
			// System.out.println("Subproblem: " + Arrays.toString(pa));
			results += safeLocations(pixels, tree, pa[0], pa[1], pa[2], pa[3],
					p, q);
		}
		return results;
	}
}
