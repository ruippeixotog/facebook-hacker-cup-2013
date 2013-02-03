import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

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

			Pixel[] pixels = new Pixel[N];
			pixels[0] = new Pixel(X, Y);
			for (int i = 1; i < N; i++) {
				int x = (pixels[i - 1].x * a + pixels[i - 1].y * b + 1) % W;
				int y = (pixels[i - 1].x * c + pixels[i - 1].y * d + 1) % H;
				pixels[i] = new Pixel(x, y);
			}
			Arrays.sort(pixels);
			// System.out.println("Screen: " + W + "x" + H + "; pic: " + P + "x"
			// + Q + "; " + N + " dead pixels");

			long result = safeLocations(pixels, 0, 0, W, H, P, Q);
			out.println(String.format("Case #%d: %d", t + 1, result));
		}
	}

	static long safeLocations(Pixel[] pixels, int x0, int y0, int w, int h,
			int p, int q) {
		if (w < p || h < q)
			return 0;

		Pixel mid = new Pixel(x0 + w / 2, y0 + h / 2);
		int midIdx = Arrays.binarySearch(pixels, mid);
		if (midIdx < 0)
			midIdx = -(midIdx + 1);

		int idx = midIdx;
		Pixel midRight = new Pixel(Integer.MAX_VALUE / 2, 0);
		while (idx < pixels.length
				&& (pixels[idx].y < y0 || pixels[idx].y >= y0 + h)) {
			idx++;
		}
		if (idx < pixels.length)
			midRight = pixels[idx];

		idx = midIdx - 1;
		Pixel midLeft = new Pixel(Integer.MIN_VALUE / 2, 0);
		while (idx >= 0 && (pixels[idx].y < y0 || pixels[idx].y >= y0 + h)) {
			idx--;
		}
		if (idx >= 0)
			midLeft = pixels[idx];

		if (midLeft.x < x0 && midRight.x >= x0 + w) {
			return (w - p + 1) * (h - q + 1);
		}
		Pixel pivot = mid.x - midLeft.x <= midRight.x - mid.x ? midLeft
				: midRight;

		int vertX0 = Math.max(x0, pivot.x - p + 1);
		int vertX1 = Math.min(x0 + w, pivot.x + p);

		int[][] subParams = { { x0, y0, pivot.x - x0, h },
				{ pivot.x + 1, y0, x0 + w - pivot.x - 1, h },
				{ vertX0, y0, vertX1 - vertX0, pivot.y - y0 },
				{ vertX0, pivot.y + 1, vertX1 - vertX0, h - pivot.y + y0 - 1 } };

		long results = 0;
		for (int[] pa : subParams) {
			// System.out.println("Subproblem: " + Arrays.toString(pa));
			results += safeLocations(pixels, pa[0], pa[1], pa[2], pa[3], p, q);
		}
		return results;
	}
}
