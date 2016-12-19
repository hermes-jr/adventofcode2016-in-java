package level_09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * --- Part Two ---
 * <p>
 * Apparently, the file actually uses version two of the format.
 * <p>
 * In version two, the only difference is that markers within decompressed data are decompressed. This, the documentation explains, provides much more substantial compression capabilities, allowing many-gigabyte files to be stored in only a few kilobytes.
 * <p>
 * For example:
 * <p>
 * (3x3)XYZ still becomes XYZXYZXYZ, as the decompressed section contains no markers.
 * X(8x2)(3x3)ABCY becomes XABCABCABCABCABCABCY, because the decompressed data from the (8x2) marker is then further decompressed, thus triggering the (3x3) marker twice for a total of six ABC sequences.
 * (27x12)(20x12)(13x14)(7x10)(1x12)A decompresses into a string of A repeated 241920 times.
 * (25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN becomes 445 characters long.
 * <p>
 * Unfortunately, the computer you brought probably doesn't have enough memory to actually decompress the file; you'll have to come up with another way to get its decompressed length.
 * <p>
 * What is the decompressed length of the file using this improved format?
 */
public class Level09b {

	// I was to lazy to optimize, so the simple bruteforce will do...

	public static void main(String[] args) throws IOException {
		List<String> content = Files.readAllLines(Paths.get("level_09/in1.txt"));
		Pattern compressionPattern = Pattern.compile("([0-9]+)x([0-9]+)");
		String line = content.get(0);
		line = line.replaceAll("[A-Z]", "A");

/*
		// Test cases
		line = "ADVENT";
		line = "(3x3)XYZ"; // 9
		line = "X(8x2)(3x3)ABCY"; // 20
		line = "(27x12)(20x12)(13x14)(7x10)(1x12)A"; // 241920
*/

		long result = 0L; // We don't need to remember the actual content, total length is enough

		while (true) {
			final int nextIdx = line.indexOf("(");
			if (nextIdx == -1) {
				//System.out.println("Remaining line: " + line);
				result += line.length();
				break;
			}

			//System.out.println("Appending up to next expansion tag: " + line.substring(0, nextIdx));
			result += (line.substring(0, nextIdx)).length();
			line = line.substring(nextIdx);
			//System.out.println("Trimmed line: " + line);

			final Matcher compressionMatcher = compressionPattern.matcher(line);
			if (!compressionMatcher.find()) {
				System.err.println("Shouldn't happen");
				break;
			}
			final int lenToRepeat = Integer.parseInt(compressionMatcher.group(1));
			final int timesRepeat = Integer.parseInt(compressionMatcher.group(2));
			final int matchLen = compressionMatcher.group(0).length() + 2;
			final String repeatMe = line.substring(matchLen, matchLen + lenToRepeat);

			//System.out.println("Going to repeat " + timesRepeat + " times: " + repeatMe);

			final StringBuilder tempResult = new StringBuilder();
			for (int i = 0; i < timesRepeat; i++)
				tempResult.append(repeatMe);

			line = line.substring(matchLen + lenToRepeat);
			tempResult.append(line);
			line = tempResult.toString();

			//System.out.println("Trimmed and semi-expanded line (" + (line.length()) + "/" + result + "): " + line);
			System.out.println(line.length() + "/" + result);
		}

		//System.out.println(result.toString());
		System.out.println("result 2: " + result);
	}
}
