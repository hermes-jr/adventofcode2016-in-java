package level_03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * --- Part Two ---
 * <p>
 * Now that you've helpfully marked up their design documents, it occurs to you that triangles are specified in groups of three vertically. Each set of three numbers in a column specifies a triangle. Rows are unrelated.
 * <p>
 * For example, given the following specification, numbers with the same hundreds digit would be part of the same triangle:
 * <p>
 * 101 301 501
 * 102 302 502
 * 103 303 503
 * 201 401 601
 * 202 402 602
 * 203 403 603
 * <p>
 * In your puzzle input, and instead reading by columns, how many of the listed triangles are possible?
 */
public class Level03b {

	public static void main(String[] args) throws IOException {
		List<String> content = Files.readAllLines(Paths.get("level_03/in1.txt"));

		int total = 0;
		int correct = 0;

		assert content.size() % 3 == 0;

		for (int i = 0; i < content.size(); i += 3) {
			total += 3;

			for (int j = 0; j < 3; j++) {
				String[] split1 = content.get(i).trim().split("\\W+");
				String[] split2 = content.get(i + 1).trim().split("\\W+");
				String[] split3 = content.get(i + 2).trim().split("\\W+");

				List<String> columnTriang = new ArrayList<>();
				columnTriang.add(split1[j]);
				columnTriang.add(split2[j]);
				columnTriang.add(split3[j]);

				List<Integer> triang = columnTriang.stream().map(Integer::parseInt).collect(Collectors.toList());

				Collections.sort(triang);
				System.out.println("analyzing triangle " + triang);
				if (triang.get(0) + triang.get(1) > triang.get(2))
					correct++;

			}
		}

		System.out.println("total: " + total);
		System.out.println("correct: " + correct);
	}

}
