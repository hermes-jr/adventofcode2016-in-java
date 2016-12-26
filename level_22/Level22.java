package level_22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * --- Day 22: Grid Computing ---
 * <p>
 * You gain access to a massive storage cluster arranged in a grid; each storage node is only connected to the four nodes directly adjacent to it (three if the node is on an edge, two if it's in a corner).
 * <p>
 * You can directly access data only on node /dev/grid/node-x0-y0, but you can perform some limited actions on the other nodes:
 * <p>
 * You can get the disk usage of all nodes (via df). The result of doing this is in your puzzle input.
 * You can instruct a node to move (not copy) all of its data to an adjacent node (if the destination node has enough space to receive the data). The sending node is left empty after this operation.
 * <p>
 * Nodes are named by their position: the node named node-x10-y10 is adjacent to nodes node-x9-y10, node-x11-y10, node-x10-y9, and node-x10-y11.
 * <p>
 * Before you begin, you need to understand the arrangement of data on these nodes. Even though you can only move data between directly connected nodes, you're going to need to rearrange a lot of the data to get access to the data you need. Therefore, you need to work out how you might be able to shift data around.
 * <p>
 * To do this, you'd like to count the number of viable pairs of nodes. A viable pair is any two nodes (A,B), regardless of whether they are directly connected, such that:
 * <p>
 * Node A is not empty (its Used is not zero).
 * Nodes A and B are not the same node.
 * The data on node A (its Used) would fit on node B (its Avail).
 * <p>
 * How many viable pairs of nodes are there?
 */
public class Level22 {
	public static void main(String[] args) throws IOException {
		List<String> content = Files.readAllLines(Paths.get("level_22/in1.txt"));
		Pattern dfPattern = Pattern.compile("^\\/dev\\/grid\\/node-x([0-9]+)-y([0-9]+)\\W+([0-9]+)T\\W+([0-9]+)T\\W+([0-9]+)T\\W+[0-9]+%$"); // x: 1, y: 2, total: 3, used: 4, free: 5, percent: 6

		Node data[][] = new Node[30][32];
		List<Node> forPart1 = new ArrayList<>();

		for (String line : content) {
			Matcher dfMatcher = dfPattern.matcher(line);
			if (!dfMatcher.matches()) {
				System.out.println("Skipping:\n\t" + line);
				continue; // first two lines
			}
			System.out.println(String.format("x %s y %s, size %s, used %s", dfMatcher.group(1), dfMatcher.group(2), dfMatcher.group(3), dfMatcher.group(4)));

			int curX = Integer.parseInt(dfMatcher.group(1));
			int curY = Integer.parseInt(dfMatcher.group(2));

			Node curNode = new Node(Integer.parseInt(dfMatcher.group(3)), Integer.parseInt(dfMatcher.group(5)));
			data[curY][curX] = curNode;
			forPart1.add(curNode);
		}

		int result1 = 0;
		System.out.println(Arrays.deepToString(data));
		for (int cn = 0; cn < forPart1.size(); cn++) {
			Node curNode = forPart1.get(cn);
			for (int on = 0; on < forPart1.size(); on++) {
				if (cn == on) {
					continue;
				}
				Node otherNode = forPart1.get(on);
				if (curNode.getTotal() - curNode.getFree() != 0 &&
						otherNode.getFree() >= curNode.getTotal() - curNode.getFree()) {
					result1++;
				}
			}

		}

		System.out.println("result1: " + result1);
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


	private static class Node {
		int total;
		int free;

		Node(int total, int free) {
			this.total = total;
			this.free = free;
		}

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public int getFree() {
			return free;
		}

		public void setFree(int free) {
			this.free = free;
		}

		@Override
		public String toString() {
			return "Node{" +
					free + "/" + total +
					'}';
		}
	}
}
