package level_01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * --- Day 1: No Time for a Taxicab ---
 * <p>
 * Santa's sleigh uses a very high-precision clock to guide its movements, and the clock's oscillator is regulated by stars. Unfortunately, the stars have been stolen... by the Easter Bunny. To save Christmas, Santa needs you to retrieve all fifty stars by December 25th.
 * <p>
 * Collect stars by solving puzzles. Two puzzles will be made available on each day in the advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!
 * <p>
 * You're airdropped near Easter Bunny Headquarters in a city somewhere. "Near", unfortunately, is as close as you can get - the instructions on the Easter Bunny Recruiting Document the Elves intercepted start here, and nobody had time to work them out further.
 * <p>
 * The Document indicates that you should start at the given coordinates (where you just landed) and face North. Then, follow the provided sequence: either turn left (L) or right (R) 90 degrees, then walk forward the given number of blocks, ending at a new intersection.
 * <p>
 * There's no time to follow such ridiculous instructions on foot, though, so you take a moment and work out the destination. Given that you can only walk on the street grid of the city, how far is the shortest path to the destination?
 * <p>
 * For example:
 * <p>
 * Following R2, L3 leaves you 2 blocks East and 3 blocks North, or 5 blocks away.
 * R2, R2, R2 leaves you 2 blocks due South of your starting position, which is 2 blocks away.
 * R5, L5, R5, R3 leaves you 12 blocks away.
 * <p>
 * How many blocks away is Easter Bunny HQ?
 * <p>
 * --- Part Two ---
 * <p>
 * Then, you notice the instructions continue on the back of the Recruiting Document. Easter Bunny HQ is actually at the first location you visit twice.
 * <p>
 * For example, if your instructions are R8, R4, R4, R8, the first location you visit twice is 4 blocks away, due East.
 * <p>
 * How many blocks away is the first location you visit twice?
 */
public class Level01 {
	public static void main(String[] args) throws IOException {
		String content = new String(Files.readAllBytes(Paths.get("level_01/in1.txt")));

		content = content.replaceAll("\\s+", "");
		System.out.println("read: " + content);

		Integer direction = 0;
		Integer xpos = 0;
		Integer ypos = 0;

		boolean secondSolved = false;
		Integer secondResult = null;
		Set<Pair<Integer>> visited = new HashSet<>();

		final String[] actions = content.split(",");
		System.out.println(Arrays.toString(actions));

		for (String cmd : actions) {
			String rot = cmd.substring(0, 1).toUpperCase();
			Integer dist = Integer.parseInt(cmd.substring(1));

			if ("R".equals(rot)) {
				direction++;
				if (direction > 3)
					direction = 0;
			} else if ("L".equals(rot)) {
				direction--;
				if (direction < 0)
					direction = 3;
			}

			Pair<Integer> step = null;

			switch (direction) {
				case 0:
					step = new Pair<>(0, -1);
					break;
				case 1:
					step = new Pair<>(1, 0);
					break;
				case 2:
					step = new Pair<>(0, 1);
					break;
				case 3:
					step = new Pair<>(-1, 0);
					break;
			}

			for (int i = 0; i < dist; i++) {
				xpos += step.x;
				ypos += step.y;

				if (!secondSolved) {
					Pair<Integer> currentPosition = new Pair<>(xpos, ypos);
					System.out.println("curpos: " + currentPosition);
					if (visited.contains(currentPosition)) {
						System.out.println("DUPE!");
						secondResult = Math.abs(xpos) + Math.abs(ypos);
						secondSolved = true;
					}
					visited.add(currentPosition);
				}
			}

		}

		Integer distance = Math.abs(xpos) + Math.abs(ypos);
		System.out.println("result: " + distance);
		System.out.println("2nd result: " + secondResult);
	}

	static class Pair<T> {
		T x;
		T y;

		Pair(T x, T y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return "[x=" + x +
					", y=" + y +
					']';
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Pair<?> pair = (Pair<?>) o;

			return x.equals(pair.x) && y.equals(pair.y);
		}

		@Override
		public int hashCode() {
			int result = x.hashCode();
			result = 31 * result + y.hashCode();
			return result;
		}
	}
}