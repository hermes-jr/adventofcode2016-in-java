package level_07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * --- Day 7: Internet Protocol Version 7 ---
 * <p>
 * While snooping around the local network of EBHQ, you compile a list of IP addresses (they're IPv7, of course; IPv6 is much too limited). You'd like to figure out which IPs support TLS (transport-layer snooping).
 * <p>
 * An IP supports TLS if it has an Autonomous Bridge Bypass Annotation, or ABBA. An ABBA is any four-character sequence which consists of a pair of two different characters followed by the reverse of that pair, such as xyyx or abba. However, the IP also must not have an ABBA within any hypernet sequences, which are contained by square brackets.
 * <p>
 * For example:
 * <p>
 * abba[mnop]qrst supports TLS (abba outside square brackets).
 * abcd[bddb]xyyx does not support TLS (bddb is within square brackets, even though xyyx is outside square brackets).
 * aaaa[qwer]tyui does not support TLS (aaaa is invalid; the interior characters must be different).
 * ioxxoj[asdfgh]zxcvbn supports TLS (oxxo is outside square brackets, even though it's within a larger string).
 * <p>
 * How many IPs in your puzzle input support TLS?
 * <p>
 * <p>
 * --- Part Two ---
 * <p>
 * You would also like to know which IPs support SSL (super-secret listening).
 * <p>
 * An IP supports SSL if it has an Area-Broadcast Accessor, or ABA, anywhere in the supernet sequences (outside any square bracketed sections), and a corresponding Byte Allocation Block, or BAB, anywhere in the hypernet sequences. An ABA is any three-character sequence which consists of the same character twice with a different character between them, such as xyx or aba. A corresponding BAB is the same characters but in reversed positions: yxy and bab, respectively.
 * <p>
 * For example:
 * <p>
 * aba[bab]xyz supports SSL (aba outside square brackets with corresponding bab within square brackets).
 * xyx[xyx]xyx does not support SSL (xyx, but no corresponding yxy).
 * aaa[kek]eke supports SSL (eke in supernet with corresponding kek in hypernet; the aaa sequence is not related, because the interior character must be different).
 * zazbz[bzb]cdb supports SSL (zaz has no corresponding aza, but zbz has a corresponding bzb, even though zaz and zbz overlap).
 * <p>
 * How many IPs in your puzzle input support SSL?
 */
public class Level07 {

	public static void main(String[] args) throws IOException {
		List<String> content = Files.readAllLines(Paths.get("level_07/in1.txt"));

		Pattern abbas = Pattern.compile("^.*(.)((?!\\1).)\\2\\1.*$");
		Pattern inBrackets = Pattern.compile("\\[(.*?)\\]");

		int counter = 0;

		lineLoop:
		for (String line : content) {
			System.out.println("Processing: " + line);

			String outside = line.replaceAll("\\[[^\\]]+\\]", "_-");
			System.out.println(outside);

			if (abbas.matcher(outside).matches()) {
				// Possible TLS, need to check inside brackets

				Matcher inBracketsMatcher = inBrackets.matcher(line);
				while (inBracketsMatcher.find()) {

					String inBracketsGroup = inBracketsMatcher.group(1);
					System.out.println(inBracketsGroup);

					if (abbas.matcher(inBracketsGroup).matches()) {
						continue lineLoop; // This line doesn't fit
					}
				}

				// Nothing found inside brackets, this is TLS
				counter++;
			}
		}

		Pattern sslPattern = Pattern.compile("^(.)((?!\\1).)\\1$");
		int counter2 = 0;

		lineLoop:
		for (String line : content) {
			String outside = line.replaceAll("\\[[^\\]]+\\]", "_-");
			System.out.println(outside);

			// Find all possible (with overlapping) combinations of ABA
			Set<String> possibleSsls = new HashSet<>();
			Matcher m = sslPattern.matcher(outside);

			int end = outside.length();
			for (int i = 0; i < end; ++i) {
				for (int j = i + 1; j <= end; ++j) {
					m.region(i, j);

					if (m.find()) {
						possibleSsls.add(m.group());
					}
				}
			}

			// There are no ABAs outside brackets. This address is invalid, skip.
			if (possibleSsls.isEmpty())
				continue;

			System.out.println(possibleSsls);

			// Convert all existing ABAs to BABs and attempt to find one of them inside brackets
			List<String> mapped = possibleSsls.stream().map(Level07::invertSsl).collect(Collectors.toList());
			System.out.println(mapped);

			Matcher inBracketsMatcher = inBrackets.matcher(line);
			while (inBracketsMatcher.find()) {

				String inBracketsGroup = inBracketsMatcher.group(1);
				System.out.println(inBracketsGroup);

				for (String invertedSsl : mapped) {
					if (inBracketsGroup.contains(invertedSsl)) {
						counter2++;
						continue lineLoop; // This line fits
					}
				}
			}

		}

		System.out.println("Result 1: " + counter);
		System.out.println("Result 2: " + counter2);
	}

	/**
	 * convert ABA to BAB
	 */
	private static String invertSsl(String string) {
		StringBuilder ret = new StringBuilder();
		char[] ltrs = string.toCharArray();
		ret.append(ltrs[1]);
		ret.append(ltrs[0]);
		ret.append(ltrs[1]);
		return ret.toString();
	}

}
