package level_04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
public class Level04 {

	public static void main(String[] args) throws IOException {
		List<String> content = Files.readAllLines(Paths.get("level_04/in1.txt"));

		Pattern abbas = Pattern.compile("^.*(.)((?!\\1).)\\2\\1.*$");
		Pattern checksumPattern = Pattern.compile("\\[([a-z]+)\\]");
		Pattern roomNumberPattern = Pattern.compile("([0-9]+)\\[[a-z]+\\]$");

		int result1 = 0;

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
			result1 += roomNumber;
		}

		System.out.println("result1 : " + result1);
	}

	public static class ValueThenKeyComparator<K extends Comparable<? super K>,
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
}
