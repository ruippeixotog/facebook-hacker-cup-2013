import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class Security {

	static Scanner in;
	static PrintStream out;

	public static void main(String[] args) throws FileNotFoundException {
		in = new Scanner(new File("in2.txt"));
		out = new PrintStream("out2.txt");

		int numTests = in.nextInt();
		in.nextLine();

		for (int t = 0; t < numTests; t++) {
			int m = in.nextInt();
			in.nextLine();
			String k1 = in.nextLine();
			String k2 = in.nextLine();

			int l = k1.length() / m;

			String[] secs = new String[m];
			for (int i = 0; i < m; i++) {
				secs[i] = k1.substring(l * i, l * (i + 1));
			}

			SortedMap<String, Integer> pieces = new TreeMap<>();
			for (int i = 0; i < m; i++) {
				String piece = k2.substring(l * i, l * (i + 1));
				if (pieces.containsKey(piece)) {
					pieces.put(piece, pieces.get(piece) + 1);
				} else {
					pieces.put(piece, 1);
				}
			}

			Map<String, Set<Integer>> dependencies = new HashMap<>();
			List<Set<String>> dependenciesInv = new ArrayList<>();

			for (int i = 0; i < secs.length; i++) {
				boolean hasMatch = false;

				Set<String> set2 = new TreeSet<>();
				dependenciesInv.add(set2);

				for (String piece : pieces.keySet()) {
					if (match(secs[i], piece) != null) {
						hasMatch = true;
						Set<Integer> set = dependencies.get(piece);
						if (set == null) {
							set = new HashSet<>();
							dependencies.put(piece, set);
						}
						set.add(i);

						set2.add(piece);
					}
				}
				if (!hasMatch) {
					dependencies = null;
					break;
				}
			}

//			System.out.println(dependencies);
//			System.out.println(dependenciesInv);

			String result = dependencies == null ? null : findOriginal(secs,
					new String[secs.length], pieces, dependencies,
					dependenciesInv, 0);
			out.println(String.format("Case #%d: %s", t + 1,
					result == null ? "IMPOSSIBLE" : result));
		}
	}

	static String findOriginal(String[] secs, String[] original,
			SortedMap<String, Integer> pieces,
			Map<String, Set<Integer>> dependencies,
			List<Set<String>> dependenciesInv, int s) {
		if (s == secs.length) {
			StringBuilder orig = new StringBuilder();
			for (String sec : original) {
				orig.append(sec);
			}
			return orig.toString().replace('?', 'a');
		}

		Set<String> possiblePieces = dependenciesInv.get(s);

		for (String piece : possiblePieces) {
			int pieceCount = pieces.get(piece);
			if (pieceCount > 0) {
				String match = match(secs[s], piece);
				if (match == null)
					continue;

				Set<Integer> deps = null;
				if (pieceCount == 1) {
					deps = dependencies.get(piece);
					boolean canRemove = true;
					for (int si : deps) {
						if (si > s && dependenciesInv.get(si).size() == 1) {
							canRemove = false;
							break;
						}
					}
					if (!canRemove)
						continue;
					for (int si : deps) {
						if (si > s)
							dependenciesInv.get(si).remove(piece);
					}
				}

				pieces.put(piece, pieces.get(piece) - 1);
				original[s] = match;
				String res = findOriginal(secs, original, pieces, dependencies,
						dependenciesInv, s + 1);

				if (res != null)
					return res;

				pieces.put(piece, pieces.get(piece) + 1);
				if (deps != null) {
					for (int si : deps) {
						if (si > s)
							dependenciesInv.get(si).add(piece);
					}
				}
			}
		}
		return null;
	}

	static String match(String sec1, String sec2) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < sec1.length(); i++) {
			if (sec1.charAt(i) == '?') {
				str.append(sec2.charAt(i));
			} else if (sec2.charAt(i) == '?') {
				str.append(sec1.charAt(i));
			} else if (sec1.charAt(i) == sec2.charAt(i)) {
				str.append(sec1.charAt(i));
			} else {
				return null;
			}
		}
		return str.toString();
	}
}
