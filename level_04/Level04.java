package level_04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * --- Day 4: Security Through Obscurity ---
 * <p>
 * Finally, you come across an information kiosk with a list of rooms. Of course, the list is encrypted and full of decoy data, but the instructions to decode the list are barely hidden nearby. Better remove the decoy data first.
 * <p>
 * Each room consists of an encrypted name (lowercase letters separated by dashes) followed by a dash, a sector ID, and a checksum in square brackets.
 * <p>
 * A room is real (not a decoy) if the checksum is the five most common letters in the encrypted name, in order, with ties broken by alphabetization. For example:
 * <p>
 * aaaaa-bbb-z-y-x-123[abxyz] is a real room because the most common letters are a (5), b (3), and then a tie between x, y, and z, which are listed alphabetically.
 * a-b-c-d-e-f-g-h-987[abcde] is a real room because although the letters are all tied (1 of each), the first five are listed alphabetically.
 * not-a-real-room-404[oarel] is a real room.
 * totally-real-room-200[decoy] is not.
 * <p>
 * Of the real rooms from the list above, the sum of their sector IDs is 1514.
 * <p>
 * What is the sum of the sector IDs of the real rooms?
 * <p>
 * <p>
 * --- Part Two ---
 * <p>
 * With all the decoy data out of the way, it's time to decrypt this list and get moving.
 * <p>
 * The room names are encrypted by a state-of-the-art shift cipher, which is nearly unbreakable without the right software. However, the information kiosk designers at Easter Bunny HQ were not expecting to deal with a master cryptographer like yourself.
 * <p>
 * To decrypt a room name, rotate each letter forward through the alphabet a number of times equal to the room's sector ID. A becomes B, B becomes C, Z becomes A, and so on. Dashes become spaces.
 * <p>
 * For example, the real name for qzmt-zixmtkozy-ivhz-343 is very encrypted name.
 * <p>
 * What is the sector ID of the room where North Pole objects are stored?
 */
public class Level04 {

	public static void main(String[] args) throws IOException {
		List<String> content = Files.readAllLines(Paths.get("level_04/in1.txt"));

		Pattern checksumPattern = Pattern.compile("\\[([a-z]+)\\]");
		Pattern roomNumberPattern = Pattern.compile("([0-9]+)\\[[a-z]+\\]$");

		int result1 = 0;

		Map<String, Integer> cleanList = new HashMap<>();

		lineLoop:
		for (String line : content) {
			System.out.println("Processing: " + line);

			Matcher checksumMatcher = checksumPattern.matcher(line);
			if (!checksumMatcher.find())
				continue;
			Matcher roomNumberMatcher = roomNumberPattern.matcher(line);
			if (!roomNumberMatcher.find())
				continue;

			Integer roomNumber = Integer.parseInt(roomNumberMatcher.group(1));
			String checksum = checksumMatcher.group(1);
			String cleanLine = line.substring(0, line.indexOf("[")).replaceAll("[^a-z]", "");

			System.out.println("\tcleaned: " + cleanLine +
					"\n\tchecksum: " + checksum +
					"\n\tnumber: " + roomNumber);

			Map<Character, Integer> freqs = new TreeMap<>();
			for (char a = 'a'; a < 'z' + 1; a++) {
				freqs.put(a, 0);
			}
			for (Character countMe : cleanLine.toCharArray()) {
				freqs.put(countMe, freqs.get(countMe) + 1);
			}

			List<Map.Entry<Character, Integer>> sorted = new ArrayList<>(freqs.entrySet());
			sorted.sort(new ValueThenKeyComparator<>());

			System.out.println("\tsorted freqs: " + sorted);

			for (int checkIdx = 0; checkIdx < checksum.length(); checkIdx++) {
				if (checksum.toCharArray()[checkIdx] != sorted.get(checkIdx).getKey())
					continue lineLoop;
			}

			System.out.println("YEEEHAAA");
			cleanList.put(line.substring(0, line.indexOf("[")), roomNumber);
			result1 += roomNumber;
		}

		System.out.println("result1 : " + result1);

		// System.out.println("test: " + rotX("qzmt-zixmtkozy-ivhz", 343));

		Integer result2 = 0;
		for (Map.Entry<String, Integer> checkMe : cleanList.entrySet()) {
			if (rotX(checkMe.getKey(), checkMe.getValue()).contains("north")) {
				result2 = checkMe.getValue();
				break;
			}
		}
		System.out.println("result2 : " + result2);
	}

	private static class ValueThenKeyComparator<K extends Comparable<? super K>,
			V extends Comparable<? super V>>
			implements Comparator<Map.Entry<K, V>> {

		public int compare(Map.Entry<K, V> a, Map.Entry<K, V> b) {
			int cmp1 = a.getValue().compareTo(b.getValue());
			if (cmp1 != 0) {
				return -1 * cmp1;
			} else {
				return a.getKey().compareTo(b.getKey());
			}
		}

	}

	private static String rotX(String source, Integer rotations) {
		StringBuilder result = new StringBuilder();
		for (char rotMe : source.toCharArray()) {
			for (int i = 0; i < rotations; i++) {
				if (rotMe == '-' || rotMe == ' ') {
					rotMe = ' ';
					continue;
				}
				rotMe++;
				if (rotMe == 'z' + 1)
					rotMe = 'a';
			}
			result.append(rotMe);
		}
		return result.toString();
	}
}
