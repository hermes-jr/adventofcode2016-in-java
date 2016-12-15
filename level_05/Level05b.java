package level_05;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * --- Part Two ---
 * <p>
 * As the door slides open, you are presented with a second door that uses a slightly more inspired security mechanism. Clearly unimpressed by the last version (in what movie is the password decrypted in order?!), the Easter Bunny engineers have worked out a better solution.
 * <p>
 * Instead of simply filling in the password from left to right, the hash now also indicates the position within the password to fill. You still look for hashes that begin with five zeroes; however, now, the sixth character represents the position (0-7), and the seventh character is the character to put in that position.
 * <p>
 * A hash result of 000001f means that f is the second character in the password. Use only the first result for each position, and ignore invalid positions.
 * <p>
 * For example, if the Door ID is abc:
 * <p>
 * The first interesting hash is from abc3231929, which produces 0000015...; so, 5 goes in position 1: _5______.
 * In the previous method, 5017308 produced an interesting hash; however, it is ignored, because it specifies an invalid position (8).
 * The second interesting hash is at index 5357525, which produces 000004e...; so, e goes in position 4: _5__e___.
 * <p>
 * You almost choke on your popcorn as the final character falls into place, producing the password 05ace8e3.
 * <p>
 * Given the actual Door ID and this new method, what is the password? Be extra proud of your solution if it uses a cinematic "decrypting" animation.
 */
public class Level05b {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		long counter = 0L;

		String prefix = "ugkcyxxp";
		MessageDigest m = MessageDigest.getInstance("MD5");

		StringBuilder result = new StringBuilder("________");

		while (true) {
			m.reset();

			String nextTry = prefix + counter;

			m.update(nextTry.getBytes(Charset.forName("UTF-8")));
			byte[] digest = m.digest();

			byte third = digest[2];
			int low = (third & 0xf0) >> 4;

			if (digest[0] == 0 && digest[1] == 0 && low == 0) {
				int fifth = (third & 0xf);

				// If fifth symbol in hash is a legit position and if it has not been found yet
				if (fifth >= 0 && fifth < 8 && result.toString().charAt(fifth) == '_') {
					int sixth = (digest[3] & 0xf0) >> 4;
					char nextChar = Character.forDigit(sixth, 16);
					System.out.println("char found: " + nextChar);
					result.setCharAt(fifth, nextChar);
					System.out.println("subresult: " + result.toString());
				}
			}

			counter++;
			if (!result.toString().contains("_"))
				break;
		}

		System.out.println("result: " + result.toString());
	}

}
