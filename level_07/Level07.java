package level_07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

			String outside = line.replaceAll("\\[[^\\]]+\\]", "-");
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

		System.out.println("Result: " + counter);

	}
}
