package level_20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * // Used library by Kevin Dolan licensed under wtfpl.
 * <p>
 * --- Day 20: Firewall Rules ---
 * <p>
 * You'd like to set up a small hidden computer here so you can use it to get back into the network later. However, the corporate firewall only allows communication with certain external IP addresses.
 * <p>
 * You've retrieved the list of blocked IPs from the firewall, but the list seems to be messy and poorly maintained, and it's not clear which IPs are allowed. Also, rather than being written in dot-decimal notation, they are written as plain 32-bit integers, which can have any value from 0 through 4294967295, inclusive.
 * <p>
 * For example, suppose only the values 0 through 9 were valid, and that you retrieved the following blacklist:
 * <p>
 * 5-8
 * 0-2
 * 4-7
 * <p>
 * The blacklist specifies ranges of IPs (inclusive of both the start and end value) that are not allowed. Then, the only IPs that this firewall allows are 3 and 9, since those are the only numbers not in any range.
 * <p>
 * Given the list of blocked IPs you retrieved from the firewall (your puzzle input), what is the lowest-valued IP that is not blocked?
 * <p>
 * Your puzzle answer was 31053880.
 * <p>
 * --- Part Two ---
 * <p>
 * How many IPs are allowed by the blacklist?
 */
public class Level20 {

	public static void main(String[] args) throws IOException {

		List<String> content = Files.readAllLines(Paths.get("level_20/in1.txt"));
		IntervalTree<Integer> st = new IntervalTree<>();
		Pattern intervalPattern = Pattern.compile("^([0-9]+)-([0-9]+)$");
		Matcher intervalMatcher;

		int ictr = 0;
		for (String line : content) {
			intervalMatcher = intervalPattern.matcher(line);
			if (!intervalMatcher.matches()) {
				throw new RuntimeException("Unexpected string: " + line);
			}

			Long v1 = Long.parseLong(intervalMatcher.group(1));
			Long v2 = Long.parseLong(intervalMatcher.group(2));
			st.addInterval(v1, v2, ictr++);
		}

		st.build();
		System.out.println(st.inSync());
		System.out.println(st.currentSize());

		// brute force
		long firstFree = -1L;
		long result2 = 0L;
		for (long i = 0L; i <= 4294967295L; i++) {
			if (st.get(i, i).size() == 0) {
				if (firstFree == -1L) {
					firstFree = i;
					System.out.println("result 1: " + firstFree);
				}
				result2++;
			}
		}
		System.out.println("result 2: " + result2);
	}

}