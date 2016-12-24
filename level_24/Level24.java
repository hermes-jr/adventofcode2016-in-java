package level_24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * --- Day 24: Air Duct Spelunking ---
 * <p>
 * You've finally met your match; the doors that provide access to the roof are locked tight, and all of the controls and related electronics are inaccessible. You simply can't reach them.
 * <p>
 * The robot that cleans the air ducts, however, can.
 * <p>
 * It's not a very fast little robot, but you reconfigure it to be able to interface with some of the exposed wires that have been routed through the HVAC system. If you can direct it to each of those locations, you should be able to bypass the security controls.
 * <p>
 * You extract the duct layout for this area from some blueprints you acquired and create a map with the relevant locations marked (your puzzle input). 0 is your current location, from which the cleaning robot embarks; the other numbers are (in no particular order) the locations the robot needs to visit at least once each. Walls are marked as #, and open passages are marked as .. Numbers behave like open passages.
 * <p>
 * For example, suppose you have a map like the following:
 * <p>
 * ###########
 * #0.1.....2#
 * #.#######.#
 * #4.......3#
 * ###########
 * <p>
 * To reach all of the points of interest as quickly as possible, you would have the robot take the following path:
 * <p>
 * 0 to 4 (2 steps)
 * 4 to 1 (4 steps; it can't move diagonally)
 * 1 to 2 (6 steps)
 * 2 to 3 (2 steps)
 * <p>
 * Since the robot isn't very fast, you need to find it the shortest route. This path is the fewest steps (in the above example, a total of 14) required to start at 0 and then visit every other location at least once.
 * <p>
 * Given your actual map, and starting from location 0, what is the fewest number of steps required to visit every non-0 number marked on the map at least once?
 * <p>
 * <p>
 * --- Part Two ---
 * <p>
 * Of course, if you leave the cleaning robot somewhere weird, someone is bound to notice.
 * <p>
 * What is the fewest number of steps required to start at 0, visit every non-0 number marked on the map at least once, and then return to 0?
 */
public class Level24 {

	public static void main(String[] args) throws IOException {
		long result1 = solveLevel(1);
		long result2 = solveLevel(2);
		System.out.println("result1: " + result1);
		System.out.println("result2: " + result2);
	}

	private static Long solveLevel(int levelNum) throws IOException {
		List<String> input = Files.readAllLines(Paths.get("level_24/in1.txt"));
		//List<String> input = Files.readAllLines(Paths.get("level_24/testin.txt")); // test case

		int MAX_X = input.get(0).length();
		int MAX_Y = input.size();

		boolean[][] generatedMaze = new boolean[MAX_Y][MAX_X];
		Integer[][] graph = new Integer[MAX_X * MAX_Y][MAX_X * MAX_Y];

		// STEP1. scan input, convert it to boolean two-dimensional array where thrue = passage, false = wall
		// Detect "relevant locations" 0 to (?) and remember their node IDs.
		Map<Integer, Integer> specialNodes = new TreeMap<>();
		//HashMap<Integer, Long> paths = new HashMap<>();
		int curNodeNumber = 0;
		int f_y = 0;
		for (String line : input) {
			int f_x = 0;
			for (Character curChar : line.toCharArray()) {
				// Everything that is not a wall - is a passage
				generatedMaze[f_y][f_x] = !curChar.equals('#');
				if (curChar >= '0' && curChar <= '9') {
					specialNodes.put(Integer.parseInt(String.valueOf(curChar)), curNodeNumber);
				}
				curNodeNumber++;
				f_x++;
			}
			f_y++;
		}
		System.out.println("Detected special nodes (with IDs): " + specialNodes);

		// STEP2. convert previously generated array to graph/matrix
		// Array to adjacency matrix conversion algorithm from level 13
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
					// There's an edge to the right cell
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

		// STEP3. get all possible permutations of known "relevant locations" starting with 0
		// eq. 0->1->2->3, 0->2->3->1, 0->3->1->2 etc.
		List<List<Integer>> bypassPossibilities = getPermutations(new ArrayList<>(specialNodes.values()).subList(1, specialNodes.keySet().size()));
		for (List<Integer> possibleBypass : bypassPossibilities) {
			possibleBypass.add(0, specialNodes.get(0));
			if (levelNum == 2) {
				possibleBypass.add(specialNodes.get(0));
			}
		}
		System.out.println(bypassPossibilities);

		// STEP4. find the shortest of them all.
		// gonna use some caching to speedup
		Map<Pair<Integer>, Long> pathsCache = new HashMap<>();
		Long shortestPath = null;
		bypassCycle:
		for (List<Integer> possibleBypass : bypassPossibilities) {
			long pathLen = 0L;
			for (int i = 0; i < possibleBypass.size() - 1; i++) {
				Pair<Integer> fromTo = new Pair<>(possibleBypass.get(i), possibleBypass.get(i + 1));
				if (!pathsCache.containsKey(fromTo)) {
					pathsCache.put(fromTo, getPathLengthFromTo(fromTo.x, fromTo.y, graph));
				}
				pathLen += pathsCache.get(fromTo);
				if (shortestPath != null && pathLen > shortestPath)
					continue bypassCycle;
			}
			if (shortestPath == null || pathLen <= shortestPath)
				shortestPath = pathLen;

			System.out.println("Found new shortest path " + possibleBypass
					+ "\nwith total length of: " + pathLen);
		}

		return shortestPath;
	}

	private static Long getPathLengthFromTo(Integer startNodeId, Integer endNodeId, Integer[][] graph) {
		Long result;

		System.out.println("searching path from node " + startNodeId + " to node " + endNodeId);
		HashMap<Integer, Long> paths = new HashMap<>();

		paths.put(startNodeId, 0L);

		ArrayList<Integer> visited = new ArrayList<>();
		Queue<Integer> queue = new LinkedList<>();
		queue.add(startNodeId);
		visited.add(startNodeId);

		longWhile:
		while (!queue.isEmpty()) {
			final Integer cnode = queue.remove();

			Integer crutch = 0;
			for (Integer t_c : graph[cnode]) {
				if (t_c != null && !visited.contains(crutch) && !crutch.equals(cnode)) {
					visited.add(crutch);
					queue.add(crutch);
					paths.put(crutch, (paths.get(cnode) + 1L));
					if (endNodeId.equals(crutch))
						break longWhile;
				}
				crutch++;
			}
		}

		result = paths.get(endNodeId);
		if (result == null)
			throw new RuntimeException("Couldn't find path");

		return result;
	}

	private static List<List<Integer>> getPermutations(List<Integer> source) {
		List<List<Integer>> result = new ArrayList<>();
		if (source.isEmpty()) {
			result.add(new ArrayList<>());
			return result;
		}
		List<Integer> list = new ArrayList<>(source);
		Integer currentFirstItem = list.get(0);
		List<Integer> nextItems = list.subList(1, list.size());
		for (List<Integer> permutations : getPermutations(nextItems)) {
			List<List<Integer>> subLists = new ArrayList<>();
			for (int i = 0; i <= permutations.size(); i++) {
				List<Integer> subList2 = new ArrayList<>();
				subList2.addAll(permutations);
				subList2.add(i, currentFirstItem);
				subLists.add(subList2);
			}
			result.addAll(subLists);
		}
		return result;
	}

	private static class Pair<T> {
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
