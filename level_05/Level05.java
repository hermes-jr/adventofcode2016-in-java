package level_05;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * --- Day 5: How About a Nice Game of Chess? ---
 * <p>
 * You are faced with a security door designed by Easter Bunny engineers that seem to have acquired most of their security knowledge by watching hacking movies.
 * <p>
 * The eight-character password for the door is generated one character at a time by finding the MD5 hash of some Door ID (your puzzle input) and an increasing integer index (starting with 0).
 * <p>
 * A hash indicates the next character in the password if its hexadecimal representation starts with five zeroes. If it does, the sixth character in the hash is the next character of the password.
 * <p>
 * For example, if the Door ID is abc:
 * <p>
 * The first index which produces a hash that starts with five zeroes is 3231929, which we find by hashing abc3231929; the sixth character of the hash, and thus the first character of the password, is 1.
 * 5017308 produces the next interesting hash, which starts with 000008f82..., so the second character of the password is 8.
 * The third time a hash starts with five zeroes is for abc5278568, discovering the character f.
 * <p>
 * In this example, after continuing this search a total of eight times, the password is 18f47a30.
 * <p>
 * Given the actual Door ID, what is the password?
 * <p>
 * Your puzzle input is ugkcyxxp.
 */
public class Level05 {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		long counter = 0L;

		String prefix = "ugkcyxxp";
		MessageDigest m = MessageDigest.getInstance("MD5");

		StringBuilder result = new StringBuilder("result: ");

		int codelen = 0;

		while (true) {
			m.reset();

			String nextTry = prefix + counter;

			m.update(nextTry.getBytes(Charset.forName("UTF-8")));
			byte[] digest = m.digest();

			byte third = digest[2];
			int low = (third & 0xf0) >> 4;

			if (digest[0] == 0 && digest[1] == 0 && low == 0) {
				int fifth = (third & 0xf);
				char nextChar = Character.forDigit(fifth, 16);
				System.out.println("char found: " + nextChar);
				result.append(nextChar);
				codelen++;
			}

			counter++;
			if (codelen == 8)
				break;
		}

		System.out.println(result.toString());
	}

}
