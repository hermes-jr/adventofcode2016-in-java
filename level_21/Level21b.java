package level_21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
public class Level21b {

	public static void main(String[] args) throws IOException, InterruptedException {
		List<String> commands = Files.readAllLines(Paths.get("level_21/in1.txt"));
		//String password = "abcdefgh";
		String initialHash = "fbgdceah";
/*
		List<String> commands = Files.readAllLines(Paths.get("level_21/testin.txt")); // test case
		String password = "abcde";
		String initialHash = "decab";
*/

		final Pattern swapLtrPattern = Pattern.compile("^swap letter (.) with letter (.)$");
		final Pattern swapPosPattern = Pattern.compile("^swap position ([0-9]+) with position ([0-9]+)$");
		final Pattern rotatePattern = Pattern.compile("^rotate (left|right) ([0-9]+) step.?$");
		final Pattern movePattern = Pattern.compile("^move position ([0-9]+) to position ([0-9]+)$");
		final Pattern reversePattern = Pattern.compile("^reverse positions ([0-9]+) through ([0-9]+)$");
		final Pattern hardRotatePattern = Pattern.compile("^rotate based on position of letter (.)$");

		Set<String> possibleCleartexts = permute(initialHash);
		System.out.println(possibleCleartexts);

		String correctCleartext = null;

		for (String password : possibleCleartexts) {
			correctCleartext = password;

			for (String command : commands) {
				Matcher swapPosMatcher = swapPosPattern.matcher(command);
				Matcher swapLtrMatcher = swapLtrPattern.matcher(command);
				Matcher rotateMatcher = rotatePattern.matcher(command);
				Matcher hardRotateMatcher = hardRotatePattern.matcher(command);
				Matcher reverseMatcher = reversePattern.matcher(command);
				Matcher moveMatcher = movePattern.matcher(command);

				if (swapPosMatcher.matches()) {
					password = swap(password, Integer.parseInt(swapPosMatcher.group(1)), Integer.parseInt(swapPosMatcher.group(2)));
				} else if (swapLtrMatcher.matches()) {
					password = swap(password, password.indexOf(swapLtrMatcher.group(1)), password.indexOf(swapLtrMatcher.group(2)));
				} else if (rotateMatcher.matches()) {
					int steps = Integer.parseInt(rotateMatcher.group(2));
					if (rotateMatcher.group(1).equals("left"))
						steps *= -1;
					password = rotate(password, steps);
				} else if (hardRotateMatcher.matches()) {
					int baseIdx = password.indexOf(hardRotateMatcher.group(1));
					if (baseIdx >= 4) {
						baseIdx += 1;
					}
					password = rotate(password, baseIdx + 1);
				} else if (moveMatcher.matches()) {
					password = move(password, Integer.parseInt(moveMatcher.group(1)), Integer.parseInt(moveMatcher.group(2)));
				} else if (reverseMatcher.matches()) {
					password = reverse(password, Integer.parseInt(reverseMatcher.group(1)), Integer.parseInt(reverseMatcher.group(2)));
				} else {
					throw new RuntimeException("unknown command: " + command);
				}
			}

			if (password.equals(initialHash)) {
				System.out.println("CLEARTEXT FITS: " + correctCleartext);
				break;
			}

		}
		System.out.println("result: " + correctCleartext);
	}

	/**
	 * Swap two characters in a string
	 *
	 * @param input data
	 * @param posA  position A, starting at 0
	 * @param posB  position B, starting at 0
	 * @return modified data
	 */
	private static String swap(final String input, final int posA, final int posB) {
		char data[] = input.toCharArray();
		final char tmp = data[posA];
		data[posA] = data[posB];
		data[posB] = tmp;
		return String.valueOf(data);
	}

	/**
	 * Rotate string by X positions right/left
	 *
	 * @param input data
	 * @param by    positive = shift right, negative - shift left
	 * @return modified data
	 */
	private static String rotate(final String input, int by) {
		if (by < 0)
			by = input.length() - Math.floorMod(-by, input.length());
		by = Math.floorMod(by, input.length());

		char data[] = input.toCharArray();
		int m = data.length;
		for (int i = 0; i < by; i++) {
			char temp = data[m - 1];
			System.arraycopy(data, 0, data, 1, m - 1);
			data[0] = temp;
		}
		return String.valueOf(data);
	}

	/**
	 * Remove character at A from string, then insert it in position B
	 *
	 * @param input data
	 * @param posA  index of first character, starting at 0
	 * @param posB  index of second character, starting at 0
	 * @return modified data
	 */
	private static String move(final String input, final int posA, final int posB) {
		char data[] = input.toCharArray();
		if (posB > posA) {
			char tmp = data[posA];
			System.arraycopy(data, posA + 1, data, posA, posB - posA);
			data[posB] = tmp;
		} else {
			char tmp = data[posA];
			System.arraycopy(data, posB, data, posB + 1, posA - posB);
			data[posB] = tmp;
		}
		return String.valueOf(data);
	}

	private static String reverse(final String input, final int posA, final int posB) {
		StringBuilder sb = new StringBuilder(input.substring(posA, posB + 1));
		sb.reverse();
		return input.substring(0, posA) + sb.toString() + input.substring(posB + 1, input.length());
	}

	/**
	 * Function by Eric Grunzke
	 * https://stackoverflow.com/questions/9666903/every-combination-of-character-array
	 */
	private static Set<String> permute(String chars) {
		// Use sets to eliminate semantic duplicates (aab is still aab even if you switch the two 'a's)
		// Switch to HashSet for better performance
		Set<String> set = new TreeSet<String>();

		// Termination condition: only 1 permutation for a string of length 1
		if (chars.length() == 1) {
			set.add(chars);
		} else {
			// Give each character a chance to be the first in the permuted string
			for (int i = 0; i < chars.length(); i++) {
				// Remove the character at index i from the string
				String pre = chars.substring(0, i);
				String post = chars.substring(i + 1);
				String remaining = pre + post;

				// Recurse to find all the permutations of the remaining chars
				for (String permutation : permute(remaining)) {
					// Concatenate the first character with the permutations of the remaining chars
					set.add(chars.charAt(i) + permutation);
				}
			}
		}
		return set;
	}
}
