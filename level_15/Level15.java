package level_15;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * --- Day 15: Timing is Everything ---
 * <p>
 * The halls open into an interior plaza containing a large kinetic sculpture. The sculpture is in a sealed enclosure and seems to involve a set of identical spherical capsules that are carried to the top and allowed to bounce through the maze of spinning pieces.
 * <p>
 * Part of the sculpture is even interactive! When a button is pressed, a capsule is dropped and tries to fall through slots in a set of rotating discs to finally go through a little hole at the bottom and come out of the sculpture. If any of the slots aren't aligned with the capsule as it passes, the capsule bounces off the disc and soars away. You feel compelled to get one of those capsules.
 * <p>
 * The discs pause their motion each second and come in different sizes; they seem to each have a fixed number of positions at which they stop. You decide to call the position with the slot 0, and count up for each position it reaches next.
 * <p>
 * Furthermore, the discs are spaced out so that after you push the button, one second elapses before the first disc is reached, and one second elapses as the capsule passes from one disc to the one below it. So, if you push the button at time=100, then the capsule reaches the top disc at time=101, the second disc at time=102, the third disc at time=103, and so on.
 * <p>
 * The button will only drop a capsule at an integer time - no fractional seconds allowed.
 * <p>
 * For example, at time=0, suppose you see the following arrangement:
 * <p>
 * Disc #1 has 5 positions; at time=0, it is at position 4.
 * Disc #2 has 2 positions; at time=0, it is at position 1.
 * <p>
 * If you press the button exactly at time=0, the capsule would start to fall; it would reach the first disc at time=1. Since the first disc was at position 4 at time=0, by time=1 it has ticked one position forward. As a five-position disc, the next position is 0, and the capsule falls through the slot.
 * <p>
 * Then, at time=2, the capsule reaches the second disc. The second disc has ticked forward two positions at this point: it started at position 1, then continued to position 0, and finally ended up at position 1 again. Because there's only a slot at position 0, the capsule bounces away.
 * <p>
 * If, however, you wait until time=5 to push the button, then when the capsule reaches each disc, the first disc will have ticked forward 5+1 = 6 times (to position 0), and the second disc will have ticked forward 5+2 = 7 times (also to position 0). In this case, the capsule would fall through the discs and come out of the machine.
 * <p>
 * However, your situation has more than two discs; you've noted their positions in your puzzle input. What is the first time you can press the button to get a capsule?
 */
public class Level15 {

	public static void main(String[] args) throws IOException {
		List<String> content = Files.readAllLines(Paths.get("level_15/in1.txt"));
		Pattern diskPattern = Pattern.compile("^Disc #([0-9]+) has ([0-9]+) positions; at time=0, it is at position ([0-9]+)\\.$");
		HashMap<Integer, DiskWithSlot> disks = new HashMap<>();

		for (String line : content) {
			Matcher diskMatcher = diskPattern.matcher(line);
			if (!diskMatcher.matches())
				throw new RuntimeException("Can't parse data: " + line);

			int diskNum = Integer.parseInt(diskMatcher.group(1));
			final DiskWithSlot synchronizedDisk = new DiskWithSlot(Integer.parseInt(diskMatcher.group(2)),
					Integer.parseInt(diskMatcher.group(3)));

			// With this all disks will be in such positions as if button were pressed at time=0
			for (int i = 0; i < diskNum; i++) {
				synchronizedDisk.move();
			}
			disks.put(diskNum, synchronizedDisk);
		}

		System.out.println("initial state: " + disks);

		long time = 1L;
		while (true) {
			int sum = 0;
			for (DiskWithSlot disk : disks.values()) {
				disk.move();
				sum += disk.getCurrentPosition();
			}
			if (sum == 0) {
				System.out.println("result: " + time);
				break;
			}

			time++;

			//System.out.println("current state: " + disks);

		}
	}
}
