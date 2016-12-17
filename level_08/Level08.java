package level_08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * --- Day 8: Two-Factor Authentication ---
 * <p>
 * You come across a door implementing what you can only assume is an implementation of two-factor authentication after a long game of requirements telephone.
 * <p>
 * To get past the door, you first swipe a keycard (no problem; there was one on a nearby desk). Then, it displays a code on a little screen, and you type that code on a keypad. Then, presumably, the door unlocks.
 * <p>
 * Unfortunately, the screen has been smashed. After a few minutes, you've taken everything apart and figured out how it works. Now you just have to work out what the screen would have displayed.
 * <p>
 * The magnetic strip on the card you swiped encodes a series of instructions for the screen; these instructions are your puzzle input. The screen is 50 pixels wide and 6 pixels tall, all of which start off, and is capable of three somewhat peculiar operations:
 * <p>
 * rect AxB turns on all of the pixels in a rectangle at the top-left of the screen which is A wide and B tall.
 * rotate row y=A by B shifts all of the pixels in row A (0 is the top row) right by B pixels. Pixels that would fall off the right end appear at the left end of the row.
 * rotate column x=A by B shifts all of the pixels in column A (0 is the left column) down by B pixels. Pixels that would fall off the bottom appear at the top of the column.
 * <p>
 * For example, here is a simple sequence on a smaller screen:
 * <p>
 * rect 3x2 creates a small rectangle in the top-left corner:
 * <p>
 * ###....
 * ###....
 * .......
 * <p>
 * rotate column x=1 by 1 rotates the second column down by one pixel:
 * <p>
 * #.#....
 * ###....
 * .#.....
 * <p>
 * rotate row y=0 by 4 rotates the top row right by four pixels:
 * <p>
 * ....#.#
 * ###....
 * .#.....
 * <p>
 * rotate column x=1 by 1 again rotates the second column down by one pixel, causing the bottom pixel to wrap back to the top:
 * <p>
 * .#..#.#
 * #.#....
 * .#.....
 * <p>
 * As you can see, this display technology is extremely powerful, and will soon dominate the tiny-code-displaying-screen market. That's what the advertisement on the back of the display tries to convince you, anyway.
 * <p>
 * There seems to be an intermediate check of the voltage used by the display: after you swipe your card, if the screen did work, how many pixels should be lit?
 * <p>
 * --- Part Two ---
 * <p>
 * You notice that the screen is only capable of displaying capital letters; in the font it uses, each letter is 5 pixels wide and 6 tall.
 * <p>
 * After you swipe your card, what code is the screen trying to display?
 */
public class Level08 {

	public static void main(String[] args) throws IOException {
		List<String> content = Files.readAllLines(Paths.get("level_08/in1.txt"));

		boolean[][] data = new boolean[6][50];

		Pattern rectPattern = Pattern.compile("^rect ([0-9]+)x([0-9]+)$");
		Pattern rowPattern = Pattern.compile("^rotate row y=([0-9]+) by ([0-9]+)$");
		Pattern colPattern = Pattern.compile("^rotate column x=([0-9]+) by ([0-9]+)$");

		for (String line : content) {
			Matcher rectMatcher = rectPattern.matcher(line);
			Matcher rowMatcher = rowPattern.matcher(line);
			Matcher colMatcher = colPattern.matcher(line);

			if (rectMatcher.matches()) {
				rectOn(data, Integer.parseInt(rectMatcher.group(2)), Integer.parseInt(rectMatcher.group(1)));
			} else if (rowMatcher.matches()) {
				rowShift(data, Integer.parseInt(rowMatcher.group(1)), Integer.parseInt(rowMatcher.group(2)));
			} else if (colMatcher.matches()) {
				colShift(data, Integer.parseInt(colMatcher.group(1)), Integer.parseInt(colMatcher.group(2)));
			}
		}

		System.out.println("result1: " + countActive(data));

		System.out.println("result2:");
		printArray(data);

		/*
		 result1: 110
		 result2:

		 [[####   ## #  # ###  #  #  ##  ###  #    #   #  ## ]
		  [   #    # #  # #  # # #  #  # #  # #    #   #   # ]
		  [  #     # #### #  # ##   #    #  # #     # #    # ]
		  [ #      # #  # ###  # #  #    ###  #      #     # ]
		  [#    #  # #  # # #  # #  #  # #    #      #  #  # ]
		  [####  ##  #  # #  # #  #  ##  #    ####   #   ##  ]]
		 */

	}

	private static void printArray(boolean[][] data) {
		System.out.println(Arrays.deepToString(data).replaceAll("\\], \\[", "]\n [").replaceAll("false", " ").replaceAll("true", "#").replaceAll(", ", ""));
	}

	private static void rectOn(boolean[][] data, int w, int h) {
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				data[i][j] = true;
			}
		}
	}

	private static void rowShift(boolean[][] data, int rowNum, int by) {
		int m = data[rowNum].length;
		for (int i = 0; i < by; i++) {
			boolean temp = data[rowNum][m - 1];
			System.arraycopy(data[rowNum], 0, data[rowNum], 1, m - 1);
			data[rowNum][0] = temp;
		}
	}

	private static void colShift(boolean[][] data, int colNum, int by) {
		int m = data.length;
		for (int i = 0; i < by; i++) {
			boolean temp = data[m - 1][colNum];
			for (int k = m - 1; k >= 1; k--) {
				data[k][colNum] = data[k - 1][colNum];
			}
			data[0][colNum] = temp;
		}
	}

	private static int countActive(boolean[][] data) {
		int result = 0;
		for (boolean[] r : data) {
			for (boolean cell : r) {
				if (cell) result++;
			}
		}
		return result;
	}
}
