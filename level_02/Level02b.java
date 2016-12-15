package level_02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * --- Part Two ---
 * <p>
 * You finally arrive at the bathroom (it's a several minute walk from the lobby so visitors can behold the many fancy conference rooms and water coolers on this floor) and go to punch in the code. Much to your bladder's dismay, the keypad is not at all like you imagined it. Instead, you are confronted with the result of hundreds of man-hours of bathroom-keypad-design meetings:
 * <p>
 * 1
 * 2 3 4
 * 5 6 7 8 9
 * A B C
 * D
 * <p>
 * You still start at "5" and stop when you're at an edge, but given the same instructions as above, the outcome is very different:
 * <p>
 * You start at "5" and don't move at all (up and left are both edges), ending at 5.
 * Continuing from "5", you move right twice and down three times (through "6", "7", "B", "D", "D"), ending at D.
 * Then, from "D", you move five more times (through "D", "B", "C", "C", "B"), ending at B.
 * Finally, after five more moves, you end at 3.
 * <p>
 * So, given the actual keypad layout, the code would be 5DB3.
 * <p>
 * Using the same instructions in your puzzle input, what is the correct bathroom code?
 */
public class Level02b {
	static char[][] keypad = {
			{'0', '0', '1', '0', '0'},
			{'0', '2', '3', '4', '0'},
			{'5', '6', '7', '8', '9'},
			{'0', 'A', 'B', 'C', '0'},
			{'0', '0', 'D', '0', '0'}};

	public static void main(String[] args) throws IOException {
		List<String> content = Files.readAllLines(Paths.get("level_02/in1.txt"));

		StringBuilder result = new StringBuilder("result: ");
		int xpos = 2;
		int ypos = 0;

		for (String codeLine : content) {
			System.out.println("decoding line: " + codeLine);

			for (char action : codeLine.toCharArray()) {
				switch (action) {
					case 'U':
						if (ypos > 0 && keypad[ypos - 1][xpos] != '0')
							ypos--;
						break;
					case 'R':
						if (xpos < 4 && keypad[ypos][xpos + 1] != '0')
							xpos++;
						break;
					case 'D':
						if (ypos < 4 && keypad[ypos + 1][xpos] != '0')
							ypos++;
						break;
					case 'L':
						if (xpos > 0 && keypad[ypos][xpos - 1] != '0')
							xpos--;
						break;
				}
			}
			result.append(keypad[ypos][xpos]);
		}
		System.out.println(result.toString());
	}
}
