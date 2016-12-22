package level_17;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * --- Day 17: Two Steps Forward ---
 * <p>
 * You're trying to access a secure vault protected by a 4x4 grid of small rooms connected by doors. You start in the top-left room (marked S), and you can access the vault (marked V) once you reach the bottom-right room:
 * <p>
 * #########
 * #S| | | #
 * #-#-#-#-#
 * # | | | #
 * #-#-#-#-#
 * # | | | #
 * #-#-#-#-#
 * # | | |
 * ####### V
 * <p>
 * Fixed walls are marked with #, and doors are marked with - or |.
 * <p>
 * The doors in your current room are either open or closed (and locked) based on the hexadecimal MD5 hash of a passcode (your puzzle input) followed by a sequence of uppercase characters representing the path you have taken so far (U for up, D for down, L for left, and R for right).
 * <p>
 * Only the first four characters of the hash are used; they represent, respectively, the doors up, down, left, and right from your current position. Any b, c, d, e, or f means that the corresponding door is open; any other character (any number or a) means that the corresponding door is closed and locked.
 * <p>
 * To access the vault, all you need to do is reach the bottom-right room; reaching this room opens the vault and all doors in the maze.
 * <p>
 * For example, suppose the passcode is hijkl. Initially, you have taken no steps, and so your path is empty: you simply find the MD5 hash of hijkl alone. The first four characters of this hash are ced9, which indicate that up is open (c), down is open (e), left is open (d), and right is closed and locked (9). Because you start in the top-left corner, there are no "up" or "left" doors to be open, so your only choice is down.
 * <p>
 * Next, having gone only one step (down, or D), you find the hash of hijklD. This produces f2bc, which indicates that you can go back up, left (but that's a wall), or right. Going right means hashing hijklDR to get 5745 - all doors closed and locked. However, going up instead is worthwhile: even though it returns you to the room you started in, your path would then be DU, opening a different set of doors.
 * <p>
 * After going DU (and then hashing hijklDU to get 528e), only the right door is open; after going DUR, all doors lock. (Fortunately, your actual passcode is not hijkl).
 * <p>
 * Passcodes actually used by Easter Bunny Vault Security do allow access to the vault if you know the right path. For example:
 * <p>
 * If your passcode were ihgpwlah, the shortest path would be DDRRRD.
 * With kglvqrro, the shortest path would be DDUDRLRRUDRD.
 * With ulqzkmiv, the shortest would be DRURDRUDDLLDLUURRDULRLDUUDDDRR.
 * <p>
 * Given your vault's passcode, what is the shortest path (the actual path, not just the length) to reach the vault?
 * <p>
 * Your puzzle input is pvhmgsws.
 */
public class Level17 {
	private static MessageDigest m;
	//private static final String salt = "ihgpwlah"; // test case, expecting min DDRRRD, max 370 steps
	//private static final String salt = "kglvqrro"; // test case, expecting min DDUDRLRRUDRD, max 492
	//private static final String salt = "ulqzkmiv"; // test case, expecting min DRURDRUDDLLDLUURRDULRLDUUDDDRR, max 830

	private static final String salt = "pvhmgsws"; // puzzle input
	private static String knownShortestPath = null;
	private static Integer knownLongestPathLength = null;

	public static void main(String[] args) throws NoSuchAlgorithmException {
		m = MessageDigest.getInstance("MD5");

		int curX = 0;
		int curY = 0;

		processStep(curX, curY, "");

		System.out.println("result 1: " + knownShortestPath);
		System.out.println("result 2: " + knownLongestPathLength);
	}

	private static void processStep(int x, int y, String newPath) {
		if (x == 3 && y == 3) {
			System.out.println("VAULT FOUND (" + newPath.length() + "): " + newPath);
			if (knownShortestPath == null || knownShortestPath.length() >= newPath.length()) {
				knownShortestPath = newPath;
			}
			if (knownLongestPathLength == null || knownLongestPathLength <= newPath.length()) {
				knownLongestPathLength = newPath.length();
			}
			return;
		}

		final byte[] digest = m.digest((salt + newPath).getBytes(Charset.forName("UTF-8")));
		final List<Character> options = getAvailableOptions(x, y, digest);
		if (options.size() == 0) {
			// Dead end, all doors closed
			return;
		}

		for (Character nextOption : options) {
			switch (nextOption) {
				case 'U':
					processStep(x, y - 1, newPath + "U");
					break;
				case 'D':
					processStep(x, y + 1, newPath + "D");
					break;
				case 'L':
					processStep(x - 1, y, newPath + "L");
					break;
				case 'R':
					processStep(x + 1, y, newPath + "R");
					break;
			}
		}
	}

	/**
	 * Given current position and a hash of current salt+path, returns a list of further movement options
	 *
	 * @param digest result of m.digest(salt + path)
	 * @return list of options: U, D, L, R (and/or)
	 */
	private static List<Character> getAvailableOptions(int x, int y, byte[] digest) {
		List<Character> result = new ArrayList<>();
		byte upByte = (byte) ((digest[0] & 0xF0) >> 4);
		byte downByte = (byte) (digest[0] & 0x0F);
		byte leftByte = (byte) ((digest[1] & 0xF0) >> 4);
		byte rightByte = (byte) (digest[1] & 0x0F);

		if (upByte > 10 && y > 0)
			result.add('U');
		if (downByte > 10 && y < 3)
			result.add('D');
		if (leftByte > 10 && x > 0)
			result.add('L');
		if (rightByte > 10 && x < 3)
			result.add('R');

		return result;
	}

	/**
	 * Convert MD5 digest to hex string
	 */
	private static String md5BtoS(byte[] digest) {
		StringBuilder sb = new StringBuilder();

		for (byte aDigest : digest) {
			if ((0xff & aDigest) < 0x10) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(0xff & aDigest));
		}

		return sb.toString();
	}
}
