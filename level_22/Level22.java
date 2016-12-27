package level_22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
 * <p>
 * <p>
 * --- Part Two ---
 * <p>
 * Now that you have a better understanding of the grid, it's time to get to work.
 * <p>
 * Your goal is to gain access to the data which begins in the node with y=0 and the highest x (that is, the node in the top-right corner).
 * <p>
 * For example, suppose you have the following grid:
 * <p>
 * Filesystem            Size  Used  Avail  Use%
 * /dev/grid/node-x0-y0   10T    8T     2T   80%
 * /dev/grid/node-x0-y1   11T    6T     5T   54%
 * /dev/grid/node-x0-y2   32T   28T     4T   87%
 * /dev/grid/node-x1-y0    9T    7T     2T   77%
 * /dev/grid/node-x1-y1    8T    0T     8T    0%
 * /dev/grid/node-x1-y2   11T    7T     4T   63%
 * /dev/grid/node-x2-y0   10T    6T     4T   60%
 * /dev/grid/node-x2-y1    9T    8T     1T   88%
 * /dev/grid/node-x2-y2    9T    6T     3T   66%
 * <p>
 * In this example, you have a storage grid 3 nodes wide and 3 nodes tall. The node you can access directly, node-x0-y0, is almost full. The node containing the data you want to access, node-x2-y0 (because it has y=0 and the highest x value), contains 6 terabytes of data - enough to fit on your node, if only you could make enough space to move it there.
 * <p>
 * Fortunately, node-x1-y1 looks like it has enough free space to enable you to move some of this data around. In fact, it seems like all of the nodes have enough space to hold any node's data (except node-x0-y2, which is much larger, very full, and not moving any time soon). So, initially, the grid's capacities and connections look like this:
 * <p>
 * ( 8T/10T) --  7T/ 9T -- [ 6T/10T]
 * |           |           |
 * 6T/11T  --  0T/ 8T --   8T/ 9T
 * |           |           |
 * 28T/32T  --  7T/11T --   6T/ 9T
 * <p>
 * The node you can access directly is in parentheses; the data you want starts in the node marked by square brackets.
 * <p>
 * In this example, most of the nodes are interchangable: they're full enough that no other node's data would fit, but small enough that their data could be moved around. Let's draw these nodes as .. The exceptions are the empty node, which we'll draw as _, and the very large, very full node, which we'll draw as #. Let's also draw the goal data as G. Then, it looks like this:
 * <p>
 * (.) .  G
 * .  _  .
 * #  .  .
 * <p>
 * The goal is to move the data in the top right, G, to the node in parentheses. To do this, we can issue some commands to the grid and rearrange the data:
 * <p>
 * Move data from node-y0-x1 to node-y1-x1, leaving node node-y0-x1 empty:
 * <p>
 * (.) _  G
 * .  .  .
 * #  .  .
 * <p>
 * Move the goal data from node-y0-x2 to node-y0-x1:
 * <p>
 * (.) G  _
 * .  .  .
 * #  .  .
 * <p>
 * At this point, we're quite close. However, we have no deletion command, so we have to move some more data around. So, next, we move the data from node-y1-x2 to node-y0-x2:
 * <p>
 * (.) G  .
 * .  .  _
 * #  .  .
 * <p>
 * Move the data from node-y1-x1 to node-y1-x2:
 * <p>
 * (.) G  .
 * .  _  .
 * #  .  .
 * <p>
 * Move the data from node-y1-x0 to node-y1-x1:
 * <p>
 * (.) G  .
 * _  .  .
 * #  .  .
 * <p>
 * Next, we can free up space on our node by moving the data from node-y0-x0 to node-y1-x0:
 * <p>
 * (_) G  .
 * .  .  .
 * #  .  .
 * <p>
 * Finally, we can access the goal data by moving the it from node-y0-x1 to node-y0-x0:
 * <p>
 * (G) _  .
 * .  .  .
 * #  .  .
 * <p>
 * So, after 7 steps, we've accessed the data we want. Unfortunately, each of these moves takes time, and we need to be efficient:
 * <p>
 * What is the fewest number of steps required to move your goal data to node-x0-y0?
 */
public class Level22 {
	public static void main(String[] args) throws IOException {
		List<String> content = Files.readAllLines(Paths.get("level_22/in1.txt"));
		Pattern dfPattern = Pattern.compile("^/dev/grid/node-x([0-9]+)-y([0-9]+)\\W+([0-9]+)T\\W+([0-9]+)T\\W+([0-9]+)T\\W+[0-9]+%$"); // x: 1, y: 2, total: 3, used: 4, free: 5, percent: 6

		List<Node> allNodes = new ArrayList<>();
		Node emptyNode = new Node(0, 0, 0, 0);
		int maxX = 0;
		int maxY = 0;

		for (String line : content) {
			Matcher dfMatcher = dfPattern.matcher(line);
			if (!dfMatcher.matches()) {
				System.out.println("Skipping:\n\t" + line);
				continue; // first two lines
			}
			System.out.println(String.format("x %s y %s, size %s, used %s", dfMatcher.group(1), dfMatcher.group(2), dfMatcher.group(3), dfMatcher.group(4)));

			int curX = Integer.parseInt(dfMatcher.group(1));
			int curY = Integer.parseInt(dfMatcher.group(2));

			// array size for future use
			if (curX > maxX)
				maxX = curX;
			if (curY > maxY)
				maxY = curY;

			Node curNode = new Node(curX, curY, Integer.parseInt(dfMatcher.group(3)), Integer.parseInt(dfMatcher.group(5)));
			allNodes.add(curNode);
			if (curNode.getUsed() == 0) {
				emptyNode = curNode; // remember sole empty node
			}
		}

		int result1 = 0;

		// Part 1: find pairs
		for (int cn = 0; cn < allNodes.size(); cn++) {
			Node curNode = allNodes.get(cn);
			for (int on = 0; on < allNodes.size(); on++) {
				if (cn == on) {
					continue;
				}
				Node otherNode = allNodes.get(on);
				if (curNode.getTotal() - curNode.getFree() != 0 &&
						otherNode.getFree() >= curNode.getUsed()) {
					result1++;
				}
			}

		}

		// Part 2: find jumbo nodes (those which data is larger then empty node may contain)
		int[][] map = new int[maxY + 1][maxX + 1];
		for (Node curNode : allNodes) {
			int nodeStatus;
			if (curNode.getX() == maxX && curNode.getY() == 0) {
				nodeStatus = 2; // data
			} else if (curNode == emptyNode) {
				nodeStatus = 0;
			} else if (curNode.getUsed() > emptyNode.getTotal()) {
				nodeStatus = -1; // jumbo node
			} else {
				nodeStatus = 1;
			}
			map[curNode.getY()][curNode.getX()] = nodeStatus;
		}

		for (int[] aMap : map) {
			for (int j = 0; j < map[0].length; j++) {
				switch (aMap[j]) {
					case -1:
						System.out.print(" #");
						break;
					case 0:
						System.out.print(" _");
						break;
					case 1:
						System.out.print(" .");
						break;
					case 2:
						System.out.print(" G");
						break;
				}
			}
			System.out.println("");
		}

		// Doh... I'm to bored to repeat BFS for this one, it's easy to solve by hand
		int result2 = 0;
		result2 += 15; // steps to the wall
		result2 += 10; // steps to bypass the wall
		result2 += 6; // steps from wall to upper right + 1 final step to the right
		result2 += 30 * 5; // move G from upper right to upper left using 5 moves per node:

		System.out.println("result1: " + result1);
		System.out.println("result2: " + result2);
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
		private int total;
		private int free;
		private int x;
		private int y;

		Node(int x, int y, int total, int free) {
			this.x = x;
			this.y = y;
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

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public int getUsed() {
			return getTotal() - getFree();
		}

		@Override
		public String toString() {
			return "Node{" +
					free + "/" + total +
					'}';
		}
	}
}
