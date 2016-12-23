package level_18;

/**
 * --- Day 18: Like a Rogue ---
 * <p>
 * As you enter this room, you hear a loud click! Some of the tiles in the floor here seem to be pressure plates for traps, and the trap you just triggered has run out of... whatever it tried to do to you. You doubt you'll be so lucky next time.
 * <p>
 * Upon closer examination, the traps and safe tiles in this room seem to follow a pattern. The tiles are arranged into rows that are all the same width; you take note of the safe tiles (.) and traps (^) in the first row (your puzzle INPUT).
 * <p>
 * The type of tile (trapped or safe) in each row is based on the types of the tiles in the same position, and to either side of that position, in the previous row. (If either side is off either end of the row, it counts as "safe" because there isn't a trap embedded in the wall.)
 * <p>
 * For example, suppose you know the first row (with tiles marked by letters) and want to determine the next row (with tiles marked by numbers):
 * <p>
 * ABCDE
 * 12345
 * <p>
 * The type of tile 2 is based on the types of tiles A, B, and C; the type of tile 5 is based on tiles D, E, and an imaginary "safe" tile. Let's call these three tiles from the previous row the left, center, and right tiles, respectively. Then, a new tile is a trap only in one of the following situations:
 * <p>
 * Its left and center tiles are traps, but its right tile is not.
 * Its center and right tiles are traps, but its left tile is not.
 * Only its left tile is a trap.
 * Only its right tile is a trap.
 * <p>
 * In any other situation, the new tile is safe.
 * <p>
 * Then, starting with the row ..^^., you can determine the next row by applying those rules to each new tile:
 * <p>
 * The leftmost character on the next row considers the left (nonexistent, so we assume "safe"), center (the first ., which means "safe"), and right (the second ., also "safe") tiles on the previous row. Because all of the trap rules require a trap in at least one of the previous three tiles, the first tile on this new row is also safe, ..
 * The second character on the next row considers its left (.), center (.), and right (^) tiles from the previous row. This matches the fourth rule: only the right tile is a trap. Therefore, the next tile in this new row is a trap, ^.
 * The third character considers .^^, which matches the second trap rule: its center and right tiles are traps, but its left tile is not. Therefore, this tile is also a trap, ^.
 * The last two characters in this new row match the first and third rules, respectively, and so they are both also traps, ^.
 * <p>
 * After these steps, we now know the next row of tiles in the room: .^^^^. Then, we continue on to the next row, using the same rules, and get ^^..^. After determining two new rows, our map looks like this:
 * <p>
 * ..^^.
 * .^^^^
 * ^^..^
 * <p>
 * Here's a larger example with ten tiles per row and ten rows:
 * <p>
 * .^^.^.^^^^
 * ^^^...^..^
 * ^.^^.^.^^.
 * ..^^...^^^
 * .^^^^.^^.^
 * ^^..^.^^..
 * ^^^^..^^^.
 * ^..^^^^.^^
 * .^^^..^.^^
 * ^^.^^^..^^
 * <p>
 * In ten rows, this larger example has 38 safe tiles.
 * <p>
 * Starting with the map in your puzzle INPUT, in a total of 40 rows (including the starting row), how many safe tiles are there?
 * <p>
 * <p>
 * --- Part Two ---
 * <p>
 * How many safe tiles are there in a total of 400000 rows?
 */
public class Level18 {
	/*
		private static final String INPUT = ".^^.^.^^^^"; // test case, 10 rows
		private static final String INPUT = "..^^."; // test case, 3 rows
	*/

	private static final String INPUT = ".^^^.^.^^^.^.......^^.^^^^.^^^^..^^^^^.^.^^^..^^.^.^^..^.^..^^...^.^^.^^^...^^.^.^^^..^^^^.....^....";

	public static void main(String[] args) {
		final int result1 = calculate(INPUT, 40);
		final int result2 = calculate(INPUT, 400000);

		System.out.println("result1: " + result1);
		System.out.println("result2: " + result2);
	}

	private static int calculate(String initialRow, int numRows) {
		boolean[][] map = new boolean[numRows][initialRow.length() + 2];

		int result = 0;

		int ii = 1;
		for (Character chrc : initialRow.toCharArray()) {
			if (chrc.equals('^'))
				map[0][ii] = true;
			else
				result++;
			ii++;
		}

		for (int i = 1; i < map.length; i++) {
			for (int j = 1; j < map[i].length - 1; j++) {
				boolean l = map[i - 1][j - 1];
				boolean c = map[i - 1][j];
				boolean r = map[i - 1][j + 1];
				boolean newCell = (l && c && !r) || (!l && c && r) || (l && !c && !r) || (!l && !c && r);
				map[i][j] = newCell;

				if (!newCell)
					result++;
			}
		}

		return result;
	}

	private static void printMap(boolean[][] map) {
		for (boolean[] aMap : map) {
			for (int j = 1; j < aMap.length - 1; j++) {
				if (aMap[j])
					System.out.print("^");
				else
					System.out.print(".");
			}
			System.out.println("");
		}
	}
}
