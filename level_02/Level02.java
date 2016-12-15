package level_02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * --- Day 2: Bathroom Security ---
 * <p>
 * You arrive at Easter Bunny Headquarters under cover of darkness. However, you left in such a rush that you forgot to use the bathroom! Fancy office buildings like this one usually have keypad locks on their bathrooms, so you search the front desk for the code.
 * <p>
 * "In order to improve security," the document you find says, "bathroom codes will no longer be written down. Instead, please memorize and follow the procedure below to access the bathrooms."
 * <p>
 * The document goes on to explain that each button to be pressed can be found by starting on the previous button and moving to adjacent buttons on the keypad: U moves up, D moves down, L moves left, and R moves right. Each line of instructions corresponds to one button, starting at the previous button (or, for the first line, the "5" button); press whatever button you're on at the end of each line. If a move doesn't lead to a button, ignore it.
 * <p>
 * You can't hold it much longer, so you decide to figure out the code as you walk to the bathroom. You picture a keypad like this:
 * <p>
 * 1 2 3
 * 4 5 6
 * 7 8 9
 * <p>
 * Suppose your instructions are:
 * <p>
 * ULL
 * RRDDD
 * LURDL
 * UUUUD
 * <p>
 * You start at "5" and move up (to "2"), left (to "1"), and left (you can't, and stay on "1"), so the first button is 1.
 * Starting from the previous button ("1"), you move right twice (to "3") and then down three times (stopping at "9" after two moves and ignoring the third), ending up with 9.
 * Continuing from "9", you move left, up, right, down, and left, ending with 8.
 * Finally, you move up four times (stopping at "2"), then down once, ending with 5.
 * <p>
 * So, in this example, the bathroom code is 1985.
 * <p>
 * Your puzzle input is the instructions from the document you found at the front desk. What is the bathroom code?
 */
public class Level02 {
	static short[][] keypad = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};

	public static void main(String[] args) throws IOException {
		List<String> content = Files.readAllLines(Paths.get("level_02/in1.txt"));

		StringBuilder result = new StringBuilder("result: ");
		int xpos = 1;
		int ypos = 1;

		for (String codeLine : content) {
			System.out.println("decoding line: " + codeLine);

			for (char action : codeLine.toCharArray()) {
				switch (action) {
					case 'U':
						if (ypos > 0)
							ypos--;
						break;
					case 'R':
						if (xpos < 2)
							xpos++;
						break;
					case 'D':
						if (ypos < 2)
							ypos++;
						break;
					case 'L':
						if (xpos > 0)
							xpos--;
						break;
				}
			}
			result.append(keypad[ypos][xpos]);
		}
		System.out.println(result.toString());
	}
}
