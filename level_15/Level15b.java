package level_15;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * --- Day 15: Timing is Everything ---
 * <p>
 * --- Part Two ---
 * <p>
 * After getting the first capsule (it contained a star! what great fortune!), the machine detects your success and begins to rearrange itself.
 * <p>
 * When it's done, the discs are back in their original configuration as if it were time=0 again, but a new disc with 11 positions and starting at position 0 has appeared exactly one second below the previously-bottom disc.
 * <p>
 * With this new disc, and counting again starting from time=0 with the configuration in your puzzle input, what is the first time you can press the button to get another capsule?
 */
public class Level15b {

	public static void main(String[] args) throws IOException {
		HashMap<Integer, DiskWithSlot> disks = new HashMap<>();
		disks.put(1, new DiskWithSlot(17, 1));
		disks.put(2, new DiskWithSlot(7, 0));
		disks.put(3, new DiskWithSlot(19, 2));
		disks.put(4, new DiskWithSlot(5, 0));
		disks.put(5, new DiskWithSlot(3, 0));
		disks.put(6, new DiskWithSlot(13, 5));
		disks.put(7, new DiskWithSlot(11, 0));

		// With this all disks will be in such positions as if button were pressed at time=0
		for (Map.Entry<Integer, DiskWithSlot> entry : disks.entrySet()) {
			for (int i = 0; i < entry.getKey(); i++) {
				entry.getValue().move();
			}
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
		}

	}
}
