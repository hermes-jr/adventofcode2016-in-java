package level_09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * --- Day 9: Explosives in Cyberspace ---
 * <p>
 * Wandering around a secure area, you come across a datalink port to a new part of the network. After briefly scanning it for interesting files, you find one file in particular that catches your attention. It's compressed with an experimental format, but fortunately, the documentation for the format is nearby.
 * <p>
 * The format compresses a sequence of characters. Whitespace is ignored. To indicate that some sequence should be repeated, a marker is added to the file, like (10x2). To decompress this marker, take the subsequent 10 characters and repeat them 2 times. Then, continue reading the file after the repeated data. The marker itself is not included in the decompressed output.
 * <p>
 * If parentheses or other characters appear within the data referenced by a marker, that's okay - treat it like normal data, not a marker, and then resume looking for markers after the decompressed section.
 * <p>
 * For example:
 * <p>
 * ADVENT contains no markers and decompresses to itself with no changes, resulting in a decompressed length of 6.
 * A(1x5)BC repeats only the B a total of 5 times, becoming ABBBBBC for a decompressed length of 7.
 * (3x3)XYZ becomes XYZXYZXYZ for a decompressed length of 9.
 * A(2x2)BCD(2x2)EFG doubles the BC and EF, becoming ABCBCDEFEFG for a decompressed length of 11.
 * (6x1)(1x3)A simply becomes (1x3)A - the (1x3) looks like a marker, but because it's within a data section of another marker, it is not treated any differently from the A that comes after it. It has a decompressed length of 6.
 * X(8x2)(3x3)ABCY becomes X(3x3)ABC(3x3)ABCY (for a decompressed length of 18), because the decompressed data from the (8x2) marker (the (3x3)ABC) is skipped and not processed further.
 * <p>
 * What is the decompressed length of the file (your puzzle input)? Don't count whitespace.
 */
public class Level09 {
	public static void main(String[] args) throws IOException {
		List<String> content = Files.readAllLines(Paths.get("level_09/in1.txt"));
		Pattern compressionPattern = Pattern.compile("([0-9]+)x([0-9]+)");
		String line = content.get(0);

/*
		// Test cases
		line = "ADVENT";
		line = "A(1x5)BC";
		line = "(3x3)XYZ";
		line = "A(2x2)BCD(2x2)EFG";
		line = "(6x1)(1x3)A";
		line = "X(8x2)(3x3)ABCY";
*/

		StringBuilder result = new StringBuilder();

		while (true) {
			final int nextIdx = line.indexOf("(");
			if (nextIdx == -1) {
				System.out.println("Remaining line: " + line);
				result.append(line);
				break;
			}
			System.out.println("Appending up to next expansion tag: " + line.substring(0, nextIdx));
			result.append(line.substring(0, nextIdx));
			line = line.substring(nextIdx);
			System.out.println("Trimmed line: " + line);

			final Matcher compressionMatcher = compressionPattern.matcher(line);
			if (!compressionMatcher.find()) {
				System.err.println("Shouldn't happen");
				break;
			}
			final int lenToRepeat = Integer.parseInt(compressionMatcher.group(1));
			final int timesRepeat = Integer.parseInt(compressionMatcher.group(2));
			final int matchLen = compressionMatcher.group(0).length() + 2;
			final String repeatMe = line.substring(matchLen, matchLen + lenToRepeat);

			System.out.println("Going to repeat " + timesRepeat + " times: " + repeatMe);

			for (int i = 0; i < timesRepeat; i++)
				result.append(repeatMe);

			line = line.substring(matchLen + lenToRepeat);
			System.out.println("Trimmed line: " + line);
		}

		System.out.println(result.toString());
		System.out.println("result 1: " + result.length());
	}
}
