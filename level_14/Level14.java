package level_14;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * --- Day 14: One-Time Pad ---
 * <p>
 * In order to communicate securely with Santa while you're on this mission, you've been using a one-time pad that you generate using a pre-agreed algorithm. Unfortunately, you've run out of keys in your one-time pad, and so you need to generate some more.
 * <p>
 * To generate keys, you first get a stream of random data by taking the MD5 of a pre-arranged salt (your puzzle input) and an increasing integer index (starting with 0, and represented in decimal); the resulting MD5 hash should be represented as a string of lowercase hexadecimal digits.
 * <p>
 * However, not all of these MD5 hashes are keys, and you need 64 new keys for your one-time pad. A hash is a key only if:
 * <p>
 * It contains three of the same character in a row, like 777. Only consider the first such triplet in a hash.
 * One of the next 1000 hashes in the stream contains that same character five times in a row, like 77777.
 * <p>
 * Considering future hashes for five-of-a-kind sequences does not cause those hashes to be skipped; instead, regardless of whether the current hash is a key, always resume testing for keys starting with the very next hash.
 * <p>
 * For example, if the pre-arranged salt is abc:
 * <p>
 * The first index which produces a triple is 18, because the MD5 hash of abc18 contains ...cc38887a5.... However, index 18 does not count as a key for your one-time pad, because none of the next thousand hashes (index 19 through index 1018) contain 88888.
 * The next index which produces a triple is 39; the hash of abc39 contains eee. It is also the first key: one of the next thousand hashes (the one at index 816) contains eeeee.
 * None of the next six triples are keys, but the one after that, at index 92, is: it contains 999 and index 200 contains 99999.
 * Eventually, index 22728 meets all of the criteria to generate the 64th key.
 * <p>
 * So, using our example salt of abc, index 22728 produces the 64th key.
 * <p>
 * Given the actual salt in your puzzle input, what index produces your 64th one-time pad key?
 * <p>
 * Your puzzle input is qzyelonm.
 * <p>
 * <p>
 * --- Part Two ---
 * <p>
 * Of course, in order to make this process even more secure, you've also implemented key stretching.
 * <p>
 * Key stretching forces attackers to spend more time generating hashes. Unfortunately, it forces everyone else to spend more time, too.
 * <p>
 * To implement key stretching, whenever you generate a hash, before you use it, you first find the MD5 hash of that hash, then the MD5 hash of that hash, and so on, a total of 2016 additional hashings. Always use lowercase hexadecimal representations of hashes.
 * <p>
 * For example, to find the stretched hash for index 0 and salt abc:
 * <p>
 * Find the MD5 hash of abc0: 577571be4de9dcce85a041ba0410f29f.
 * Then, find the MD5 hash of that hash: eec80a0c92dc8a0777c619d9bb51e910.
 * Then, find the MD5 hash of that hash: 16062ce768787384c81fe17a7a60c7e3.
 * ...repeat many times...
 * Then, find the MD5 hash of that hash: a107ff634856bb300138cac6568c0f24.
 * <p>
 * So, the stretched hash for index 0 in this situation is a107ff.... In the end, you find the original hash (one use of MD5), then find the hash-of-the-previous-hash 2016 times, for a total of 2017 uses of MD5.
 * <p>
 * The rest of the process remains the same, but now the keys are entirely different. Again for salt abc:
 * <p>
 * The first triple (222, at index 5) has no matching 22222 in the next thousand hashes.
 * The second triple (eee, at index 10) hash a matching eeeee at index 89, and so it is the first key.
 * Eventually, index 22551 produces the 64th key (triple fff with matching fffff at index 22859.
 * <p>
 * Given the actual salt in your puzzle input and using 2016 extra MD5 calls of key stretching, what index now produces your 64th one-time pad key?
 */
public class Level14 {
	private static final String salt = "qzyelonm";
	//private static final String salt = "abc";
	private static MessageDigest m;
	private static final Pattern triplet = Pattern.compile("(.)\\1\\1");

	public static void main(String[] args) throws NoSuchAlgorithmException {
		m = MessageDigest.getInstance("MD5");

		int result1 = calculate(0); // part1
		int result2 = calculate(2016); // part2

		System.out.println("result1: " + result1); // test case: 22728 (correct), my case: 15168 (correct)
		System.out.println("result2: " + result2); // test case: 22551 (correct), my case: 20316 (INCORRECT?! too low)
	}

	private static int calculate(int rotations) {
		HashMap<Integer, String> knownHashes = new HashMap<>();
		int counter = 0;
		int foundHashes = 0;

		outerLoop:
		while (true) {
			String strDigest;
			if (knownHashes.containsKey(counter)) {
				strDigest = knownHashes.get(counter);
			} else {
				strDigest = hashIt(salt + counter, rotations);
				knownHashes.put(counter, strDigest);
			}

			Matcher tripletMatcher = triplet.matcher(strDigest);
			if (tripletMatcher.find(0)) {
				String strTriplet = tripletMatcher.group(0);
				tripletMatcher.reset();
				//System.out.println(strTriplet + " found in in " + strDigest);

				// Try to find quintet in following 1000 hashes
				for (int i = counter + 1; i <= counter + 1001; i++) {
					String nextStrDigest;
					if (knownHashes.containsKey(i)) {
						nextStrDigest = knownHashes.get(i);
					} else {
						nextStrDigest = hashIt(salt + i, rotations);
						knownHashes.put(i, nextStrDigest);
					}

					String quintet = (strTriplet + strTriplet).substring(0, 5); // Same character repeating five times
					// System.out.println("searching for " + quintet + " in " + nextStrDigest);

					if (nextStrDigest.contains(quintet)) {
						foundHashes++;

						System.out.println("number " + foundHashes + " digest found: " + strDigest + " at " + counter);

						if (foundHashes == 64)
							break outerLoop;

						counter++;
						continue outerLoop;
					}
				}
			}

			counter++;
		}
		return counter;
	}

	private static String hashIt(String plaintext, int rotations) {
		String result;
		m.reset();
		m.update(plaintext.getBytes(Charset.forName("UTF-8")));

		byte[] digest = m.digest();

		result = md5BtoS(digest);

		for (int stretch = 0; stretch < rotations; stretch++) {
			m.reset();
			m.update(result.getBytes(Charset.forName("UTF-8")));
			digest = m.digest();
			result = md5BtoS(digest);
		}

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
