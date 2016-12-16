package level_06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * --- Day 6: Signals and Noise ---
 * <p>
 * Something is jamming your communications with Santa. Fortunately, your signal is only partially jammed, and protocol in situations like this is to switch to a simple repetition code to get the message through.
 * <p>
 * In this model, the same message is sent repeatedly. You've recorded the repeating message signal (your puzzle input), but the data seems quite corrupted - almost too badly to recover. Almost.
 * <p>
 * All you need to do is figure out which character is most frequent for each position. For example, suppose you had recorded the following messages:
 * <p>
 * eedadn
 * drvtee
 * eandsr
 * raavrd
 * atevrs
 * tsrnev
 * sdttsa
 * rasrtv
 * nssdts
 * ntnada
 * svetve
 * tesnvt
 * vntsnd
 * vrdear
 * dvrsen
 * enarar
 * <p>
 * The most common character in the first column is e; in the second, a; in the third, s, and so on. Combining these characters returns the error-corrected message, easter.
 * <p>
 * Given the recording in your puzzle input, what is the error-corrected version of the message being sent?
 * <p>
 * --- Part Two ---
 * <p>
 * Of course, that would be the message - if you hadn't agreed to use a modified repetition code instead.
 * <p>
 * In this modified code, the sender instead transmits what looks like random data, but for each character, the character they actually want to send is slightly less likely than the others. Even after signal-jamming noise, you can look at the letter distributions in each column and choose the least common letter to reconstruct the original message.
 * <p>
 * In the above example, the least common character in the first column is a; in the second, d, and so on. Repeating this process for the remaining characters produces the original message, advent.
 * <p>
 * Given the recording in your puzzle input and this new decoding methodology, what is the original message that Santa is trying to send?
 */
public class Level06 {

	public static void main(String[] args) throws IOException {
		List<String> content = Files.readAllLines(Paths.get("level_06/in1.txt"));

		StringBuilder result = new StringBuilder("result: ");
		StringBuilder result2 = new StringBuilder("result part 2: ");
		int pwlen = content.get(0).length();

		for (int i = 0; i < pwlen; i++) {
			int[] alphabet = new int[128];
			// count freqs
			for (String str : content) {
				char countme = str.toCharArray()[i];
				alphabet[countme]++;
			}

			int mostFrequentIdx = 0;
			for (int j = 'a'; j < 'z' + 1; j++) {
				if (alphabet[j] > alphabet[mostFrequentIdx])
					mostFrequentIdx = j;
			}

			int leastFrequentIdx = mostFrequentIdx;
			for (int j = 'a'; j < 'z' + 1; j++) {
				if (alphabet[j] < alphabet[leastFrequentIdx] && alphabet[leastFrequentIdx] != 0)
					leastFrequentIdx = j;
			}

			result.append((char) mostFrequentIdx);
			result2.append((char) leastFrequentIdx);
		}

		System.out.println(result.toString());
		System.out.println(result2.toString());

	}
}
