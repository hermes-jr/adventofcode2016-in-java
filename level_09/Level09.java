package level_09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Level09 {
	public static void main(String[] args) throws IOException {
		List<String> content = Files.readAllLines(Paths.get("level_09/in1.txt"));

		System.out.println(content.get(0).length());
	}
}
