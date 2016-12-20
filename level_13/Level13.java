package level_13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * --- Day 13: A Maze of Twisty Little Cubicles ---
 * <p>
 * You arrive at the first floor of this new building to discover a much less welcoming environment than the shiny atrium of the last one. Instead, you are in a maze of twisty little cubicles, all alike.
 * <p>
 * Every location in this area is addressed by a pair of non-negative integers (x,y). Each such coordinate is either a wall or an open space. You can't move diagonally. The cube maze starts at 0,0 and seems to extend infinitely toward positive x and y; negative values are invalid, as they represent a location outside the building. You are in a small waiting area at 1,1.
 * <p>
 * While it seems chaotic, a nearby morale-boosting poster explains, the layout is actually quite logical. You can determine whether a given x,y coordinate will be a wall or an open space using a simple system:
 * <p>
 * Find x*x + 3*x + 2*x*y + y + y*y.
 * Add the office designer's favorite number (your puzzle input).
 * Find the binary representation of that sum; count the number of bits that are 1.
 * If the number of bits that are 1 is even, it's an open space.
 * If the number of bits that are 1 is odd, it's a wall.
 * <p>
 * For example, if the office designer's favorite number were 10, drawing walls as # and open spaces as ., the corner of the building containing 0,0 would look like this:
 * <p>
 * 0123456789
 * 0 .#.####.##
 * 1 ..#..#...#
 * 2 #....##...
 * 3 ###.#.###.
 * 4 .##..#..#.
 * 5 ..##....#.
 * 6 #...##.###
 * <p>
 * Now, suppose you wanted to reach 7,4. The shortest route you could take is marked as O:
 * <p>
 * 0123456789
 * 0 .#.####.##
 * 1 .O#..#...#
 * 2 #OOO.##...
 * 3 ###O#.###.
 * 4 .##OO#OO#.
 * 5 ..##OOO.#.
 * 6 #...##.###
 * <p>
 * Thus, reaching 7,4 would take a minimum of 11 steps (starting from your current location, 1,1).
 * <p>
 * What is the fewest number of steps required for you to reach 31,39?
 * <p>
 * Your puzzle input is 1350.
 */
public class Level13 {

	// personal input
	private static final int MAX_X = 50;
	private static final int MAX_Y = 60;
	private static final int SEED = 1350;
	private static final int TARGET_X = 31;
	private static final int TARGET_Y = 39;

/*
	// test input
	private static final int MAX_X = 10;
	private static final int MAX_Y = 7;
	private static final int SEED = 10;
	private static final int TARGET_X = 7;
	private static final int TARGET_Y = 4;
*/


	public static void main(String[] args) {
		boolean[][] generatedMaze = new boolean[MAX_Y][MAX_X];

		/*
		I'll try fast and dumb solution: convert our generated maze to adjacency matrix.
		Each cell may be a node, but walls will be unreachable nodes. Undirected edges will have
		a weight of 1. Using BFS we'll find a shortest path from given node to.
		As long as I know 50*40 matrix is enough, I'll number nodes 0-2000 starting from upper left corner.
		That way our waiting area would be at node 1*50+1 and end would be at node 1981 (39*50+31)
		*/
		Integer[][] graph = new Integer[MAX_X * MAX_Y][MAX_X * MAX_Y];
		HashMap<Integer, Long> paths = new HashMap<>();
		Integer targetNodeNumber = TARGET_Y * MAX_X + TARGET_X;

		// generate maze
		for (int y = 0; y < MAX_Y; y++) {
			for (int x = 0; x < MAX_X; x++) {
				Integer decider = SEED + x * x + 3 * x + 2 * x * y + y + y * y;
				generatedMaze[y][x] = (Integer.bitCount(decider) % 2 == 0);
			}
		}

		printMaze(generatedMaze);

		for (int y = 0; y < MAX_Y - 1; y++) {
			for (int x = 0; x < MAX_X - 1; x++) {
				if (!generatedMaze[y][x])
					continue; // Current cell is a wall

				/*
				 Not a wall, find out neighbours. Since we're visiting all cells from left to right,
				 there's probably no point in checking all directions, right and down should be sufficient.
				 */
				Integer currentNodeNumber = y * MAX_X + x;
				if (generatedMaze[y][x + 1]) {
					// There's [AARRGH, WHATEVER] an edge to the right cell
					Integer rightNodeNumber = currentNodeNumber + 1;
					graph[currentNodeNumber][rightNodeNumber] = 1;
					graph[rightNodeNumber][currentNodeNumber] = 1;
				}
				if (generatedMaze[y + 1][x]) {
					// There's an edge to the bottom cell
					Integer bottomNodeNumber = (y + 1) * MAX_X + x;
					graph[currentNodeNumber][bottomNodeNumber] = 1;
					graph[bottomNodeNumber][currentNodeNumber] = 1;
				}
			}
		}

		Integer startNodeNumber = MAX_X + 1;
		for (int p_i = 0; p_i < MAX_X * MAX_Y; p_i++) {
			Long w = null;
			if (p_i == startNodeNumber) w = 0L;
			paths.put(p_i, w);
		}

		ArrayList<Integer> visited = new ArrayList<>();
		Queue<Integer> queue = new LinkedList<>();
		queue.add(startNodeNumber);
		visited.add(startNodeNumber);
		paths.put(startNodeNumber, 0L);
		while (!queue.isEmpty()) {
			Integer cnode = queue.remove();

			Integer crutch = 0;
			for (Integer t_c : graph[cnode]) {
				if (t_c != null && !visited.contains(crutch) && !crutch.equals(cnode)) {
					visited.add(crutch);
					queue.add(crutch);
					//Long cpath = paths.get(crutch);
					Long cpath = paths.get(cnode) + 1L;
					paths.put(crutch, cpath);

				}
				crutch++;
			}
		}

		System.out.println("Result 1: " + paths.get(targetNodeNumber));

	}

	private static void printMaze(boolean[][] generatedMaze) {
		for (int y = 0; y < MAX_Y; y++) {
			for (int x = 0; x < MAX_X; x++) {
				if (x == 1 && y == 1) {
					System.out.print("I");
					continue;
				}

				if (x == TARGET_X && y == TARGET_Y) {
					System.out.print("O");
					continue;
				}

				if (generatedMaze[y][x])
					System.out.print(".");
				else
					System.out.print("#");
			}
			System.out.println("");
		}
	}
}