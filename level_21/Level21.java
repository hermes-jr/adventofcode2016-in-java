package level_21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * --- Day 21: Scrambled Letters and Hash ---
 * <p>
 * The computer system you're breaking into uses a weird scrambling function to store its passwords. It shouldn't be much trouble to create your own scrambled password so you can add it to the system; you just have to implement the scrambler.
 * <p>
 * The scrambling function is a series of operations (the exact list is provided in your puzzle input). Starting with the password to be scrambled, apply each operation in succession to the string. The individual operations behave as follows:
 * <p>
 * swap position X with position Y means that the letters at indexes X and Y (counting from 0) should be swapped.
 * swap letter X with letter Y means that the letters X and Y should be swapped (regardless of where they appear in the string).
 * rotate left/right X steps means that the whole string should be rotated; for example, one right rotation would turn abcd into dabc.
 * rotate based on position of letter X means that the whole string should be rotated to the right based on the index of letter X (counting from 0) as determined before this instruction does any rotations. Once the index is determined, rotate the string to the right one time, plus a number of times equal to that index, plus one additional time if the index was at least 4.
 * reverse positions X through Y means that the span of letters at indexes X through Y (including the letters at X and Y) should be reversed in order.
 * move position X to position Y means that the letter which is at index X should be removed from the string, then inserted such that it ends up at index Y.
 * <p>
 * For example, suppose you start with abcde and perform the following operations:
 * <p>
 * swap position 4 with position 0 swaps the first and last letters, producing the input for the next step, ebcda.
 * swap letter d with letter b swaps the positions of d and b: edcba.
 * reverse positions 0 through 4 causes the entire string to be reversed, producing abcde.
 * rotate left 1 step shifts all letters left one position, causing the first letter to wrap to the end of the string: bcdea.
 * move position 1 to position 4 removes the letter at position 1 (c), then inserts it at position 4 (the end of the string): bdeac.
 * move position 3 to position 0 removes the letter at position 3 (a), then inserts it at position 0 (the front of the string): abdec.
 * rotate based on position of letter b finds the index of letter b (1), then rotates the string right once plus a number of times equal to that index (2): ecabd.
 * rotate based on position of letter d finds the index of letter d (4), then rotates the string right once, plus a number of times equal to that index, plus an additional time because the index was at least 4, for a total of 6 right rotations: decab.
 * <p>
 * After these steps, the resulting scrambled password is decab.
 * <p>
 * Now, you just need to generate a new scrambled password and you can access the system. Given the list of scrambling operations in your puzzle input, what is the result of scrambling abcdefgh?
 */
public class Level21 {

	public static void main(String[] args) throws IOException, InterruptedException {
/*
		List<String> commands = Files.readAllLines(Paths.get("level_21/in1.txt"));
		String password = "abcdefgh";
*/
		List<String> commands = Files.readAllLines(Paths.get("level_21/testin.txt")); // test case
		String password = "abcde";

		final Pattern swapLtrPattern = Pattern.compile("^swap letter (.) with letter (.)$");
		final Pattern swapPosPattern = Pattern.compile("^swap position ([0-9]+) with position ([0-9]+)$");
		final Pattern rotatePattern = Pattern.compile("^rotate (left|right) ([0-9]+) step.?$");
		final Pattern movePattern = Pattern.compile("^move position ([0-9]+) to position ([0-9]+)$");
		final Pattern reversePattern = Pattern.compile("^reverse positions ([0-9]+) through ([0-9]+)$");
		final Pattern hardRotatePattern = Pattern.compile("^rotate based on position of letter (.)$");

		for (String command : commands) {
			System.out.println("processing: " + command);
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
			System.out.println(password);
		}

		System.out.println("result: " + password);
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

}
