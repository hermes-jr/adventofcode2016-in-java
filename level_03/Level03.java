package level_03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * --- Day 3: Squares With Three Sides ---
 * <p>
 * Now that you can think clearly, you move deeper into the labyrinth of hallways and office furniture that makes up this part of Easter Bunny HQ. This must be a graphic design department; the walls are covered in specifications for triangles.
 * <p>
 * Or are they?
 * <p>
 * The design document gives the side lengths of each triangle it describes, but... 5 10 25? Some of these aren't triangles. You can't help but mark the impossible ones.
 * <p>
 * In a valid triangle, the sum of any two sides must be larger than the remaining side. For example, the "triangle" given above is impossible, because 5 + 10 is not larger than 25.
 * <p>
 * In your puzzle input, how many of the listed triangles are possible?
 */
public class Level03 {
	public static void main(String[] args) throws IOException {
		List<String> content = Files.readAllLines(Paths.get("level_03/in1.txt"));

		int total = 0;
		int correct = 0;

		for (String codeLine : content) {
			total++;

			List<String> split = Arrays.asList(codeLine.trim().split("\\W+"));

			List<Integer> mapped = split.stream().map(Integer::parseInt).collect(Collectors.toList());

			Collections.sort(mapped);
			if (mapped.get(0) + mapped.get(1) > mapped.get(2))
				correct++;

			System.out.println(mapped);
		}

		System.out.println("total: " + total);
		System.out.println("correct: " + correct);
	}
}

