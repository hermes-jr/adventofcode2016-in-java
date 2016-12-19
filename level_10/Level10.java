package level_10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * --- Day 10: Balance Bots ---
 * <p>
 * You come upon a factory in which many robots are zooming around handing small microchips to each other.
 * <p>
 * Upon closer examination, you notice that each bot only proceeds when it has two microchips, and once it does, it gives each one to a different bot or puts it in a marked "output" bin. Sometimes, bots take microchips from "input" bins, too.
 * <p>
 * Inspecting one of the microchips, it seems like they each contain a single number; the bots must use some logic to decide what to do with each chip. You access the local control computer and download the bots' instructions (your puzzle input).
 * <p>
 * Some of the instructions specify that a specific-valued microchip should be given to a specific bot; the rest of the instructions indicate what a given bot should do with its lower-value or higher-value chip.
 * <p>
 * For example, consider the following instructions:
 * <p>
 * value 5 goes to bot 2
 * bot 2 gives low to bot 1 and high to bot 0
 * value 3 goes to bot 1
 * bot 1 gives low to output 1 and high to bot 0
 * bot 0 gives low to output 2 and high to output 0
 * value 2 goes to bot 2
 * <p>
 * Initially, bot 1 starts with a value-3 chip, and bot 2 starts with a value-2 chip and a value-5 chip.
 * Because bot 2 has two microchips, it gives its lower one (2) to bot 1 and its higher one (5) to bot 0.
 * Then, bot 1 has two microchips; it puts the value-2 chip in output 1 and gives the value-3 chip to bot 0.
 * Finally, bot 0 has two microchips; it puts the 3 in output 2 and the 5 in output 0.
 * <p>
 * In the end, output bin 0 contains a value-5 microchip, output bin 1 contains a value-2 microchip, and output bin 2 contains a value-3 microchip. In this configuration, bot number 2 is responsible for comparing value-5 microchips with value-2 microchips.
 * <p>
 * Based on your instructions, what is the number of the bot that is responsible for comparing value-61 microchips with value-17 microchips?
 * <p>
 * <p>
 * --- Part Two ---
 * <p>
 * What do you get if you multiply together the values of one chip in each of outputs 0, 1, and 2?
 */
public class Level10 {
	private static HashMap<Integer, ChipReceiver> receivers;

	public static void main(String[] args) throws IOException {
		List<String> content = Files.readAllLines(Paths.get("level_10/in1.txt"));
		Pattern botToSomethingPattern = Pattern.compile("^bot ([0-9]+) gives low to (bot|output) ([0-9]+) and high to (bot|output) ([0-9]+)$");
		Pattern inToBotPattern = Pattern.compile("^value ([0-9]+) goes to bot ([0-9]+)$");

		receivers = new HashMap<>();

		// process only bot connections first
		for (String line : content) {
			Matcher botToSomethingMatcher = botToSomethingPattern.matcher(line);
			if (botToSomethingMatcher.matches()) {
				Integer botId = Integer.parseInt(botToSomethingMatcher.group(1));
				Integer lowId = Integer.parseInt(botToSomethingMatcher.group(3));
				Integer hiId = Integer.parseInt(botToSomethingMatcher.group(5));
				String lowEntity = botToSomethingMatcher.group(2);
				String hiEntity = botToSomethingMatcher.group(4);
				if ("output".equals(lowEntity)) {
					lowId = (lowId * -1) - 1;
				}
				if ("output".equals(hiEntity))
					hiId = (hiId * -1) - 1;
				ChipReceiver currentBot = getOrCreateReceiver(botId);
				currentBot.setLowReceiver(getOrCreateReceiver(lowId));
				currentBot.setHiReceiver(getOrCreateReceiver(hiId));
			}
		}

		// process only inputs
		for (String line : content) {
			Matcher inToBotMatcher = inToBotPattern.matcher(line);

			if (inToBotMatcher.matches()) {
				Integer chipId = Integer.parseInt(inToBotMatcher.group(1));
				Integer botId = Integer.parseInt(inToBotMatcher.group(2));
				getOrCreateReceiver(botId).take(chipId);
			}
		}

		int result2 = getOrCreateReceiver(-1).data.get(0) * getOrCreateReceiver(-2).data.get(0) * getOrCreateReceiver(-3).data.get(0);
		System.out.println("result 2: " + result2);
	}

	/**
	 * Thing that accepts chips - either bot or output.
	 * Outputs get negative indices minus 1 (output 0 = -1, -1 = -2 etc).
	 * Bots get links to two neighbours.
	 * After getting both inputs, bot sorts them and passes further.
	 * Output has both neighbours = null and only one value in data.
	 */
	private static ChipReceiver getOrCreateReceiver(Integer receiverId) {
		ChipReceiver newborn = new ChipReceiver(receiverId);
		if (receivers.containsKey(receiverId))
			return receivers.get(receiverId);
		receivers.put(receiverId, newborn);
		return newborn;
	}

	private static class ChipReceiver {
		private ChipReceiver low = null;
		private ChipReceiver hi = null;
		List<Integer> data;
		Integer id = null;

		ChipReceiver(Integer id) {
			this.id = id;
			data = new ArrayList<>();
		}

		void take(Integer chip) {
			data.add(chip);
			if (data.size() == 2) {
				Collections.sort(data);

				// part 1 condition
				if (data.get(0).equals(17) && data.get(1).equals(61)) {
					System.out.println("result 1: " + this.id);
				}

				if (low != null && hi != null) {
					low.take(data.get(0));
					hi.take(data.get(1));
				} else {
					System.out.println("bot " + this.id + " is stuck");
				}
			}
		}

		void setHiReceiver(ChipReceiver hi) {
			this.hi = hi;
		}

		void setLowReceiver(ChipReceiver low) {
			this.low = low;
		}
	}
}
