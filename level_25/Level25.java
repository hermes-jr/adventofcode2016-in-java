package level_25;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * --- Day 25: Clock Signal ---
 * <p>
 * You open the door and find yourself on the roof. The city sprawls away from you for miles and miles.
 * <p>
 * There's not much time now - it's already Christmas, but you're nowhere near the North Pole, much too far to deliver these stars to the sleigh in time.
 * <p>
 * However, maybe the huge antenna up here can offer a solution. After all, the sleigh doesn't need the stars, exactly; it needs the timing data they provide, and you happen to have a massive signal generator right here.
 * <p>
 * You connect the stars you have to your prototype computer, connect that to the antenna, and begin the transmission.
 * <p>
 * Nothing happens.
 * <p>
 * You call the service number printed on the side of the antenna and quickly explain the situation. "I'm not sure what kind of equipment you have connected over there," he says, "but you need a clock signal." You try to explain that this is a signal for a clock.
 * <p>
 * "No, no, a clock signal - timing information so the antenna computer knows how to read the data you're sending it. An endless, alternating pattern of 0, 1, 0, 1, 0, 1, 0, 1, 0, 1...." He trails off.
 * <p>
 * You ask if the antenna can handle a clock signal at the frequency you would need to use for the data from the stars. "There's no way it can! The only antenna we've installed capable of that is on top of a top-secret Easter Bunny installation, and you're definitely not-" You hang up the phone.
 * <p>
 * You've extracted the antenna's clock signal generation assembunny code (your puzzle input); it looks mostly compatible with code you worked on just recently.
 * <p>
 * This antenna code, being a signal generator, uses one extra instruction:
 * <p>
 * out x transmits x (either an integer or the value of a register) as the next value for the clock signal.
 * <p>
 * The code takes a value (via register a) that describes the signal to generate, but you're not sure how it's used. You'll have to find the input to produce the right signal through experimentation.
 * <p>
 * What is the lowest positive integer that can be used to initialize register a and cause the code to output a clock signal of 0, 1, 0, 1... repeating forever?
 * <p>
 * Your puzzle answer was 175.
 * <p>
 * The first half of this puzzle is complete! It provides one gold star: *
 * --- Part Two ---
 * <p>
 * The antenna is ready. Now, all you need is the fifty stars required to generate the signal for the sleigh, but you don't have enough.
 * <p>
 * You look toward the sky in desperation... suddenly noticing that a lone star has been installed at the top of the antenna! Only 49 more to go.
 */
public class Level25 {

	private static List<String> program;
	private static Pattern cpyPattern = Pattern.compile("^cpy ([a-d]|-?[0-9]+) ([a-d])$");
	private static Pattern jnzPattern = Pattern.compile("^jnz ([a-d]|-?[0-9]+) ([a-d]|-?[0-9]+)$");

	public static void main(String[] args) throws IOException {
		program = Files.readAllLines(Paths.get("level_25/in1.txt"));

		int seed = 1;
		while (seed++ > 0) {
			String res = calc(seed);
			if (res.startsWith("0101010101010101010101")) {
				System.out.println("result: " + seed);
				break;
			}
			System.out.println(res);
		}
		//System.out.println("result 1: " + result1);

/*
		program = Files.readAllLines(Paths.get("level_23/in1.txt")); // reset program, as the previous run modified it
		int result2 = calc(12);
		System.out.println("result 2: " + result2);
*/
	}

	private static String calc(int seed) {
		HashMap<Character, Integer> registers = new HashMap<>();
		registers.put('a', seed);
		registers.put('b', 0);
		registers.put('c', 0);
		registers.put('d', 0);

		int ip = 0;
		int ctr = 0;
		StringBuilder clockSignal = new StringBuilder();

		while (ip < program.size()) {
			final String instruction = program.get(ip);
			//System.out.println("Processing: " + ip + " " + instruction);

			Matcher cpyMatcher = cpyPattern.matcher(instruction);
			Matcher jnzMatcher = jnzPattern.matcher(instruction);

			if (instruction.startsWith("out ")) {
				int outVal = intOrRegister(instruction.substring(3).trim(), registers);
				clockSignal.append(outVal);
				if (ctr++ > 30)
					return clockSignal.toString();
			} else if (instruction.startsWith("tgl")) {
				// TGL
				Integer offset = intOrRegister(instruction.substring(4), registers);
				if (ip + offset < program.size() && ip + offset >= 0) {
					String afterToggle;
					String beforeToggle = program.get(ip + offset);
					if (beforeToggle.indexOf(' ') == beforeToggle.lastIndexOf(' ')) {
						// one argument
						if (beforeToggle.startsWith("inc")) {
							afterToggle = "dec" + beforeToggle.substring(3);
						} else {
							afterToggle = "inc" + beforeToggle.substring(3);
						}
					} else {
						// two argument
						if (beforeToggle.startsWith("jnz")) {
							afterToggle = "cpy" + beforeToggle.substring(3);
						} else {
							afterToggle = "jnz" + beforeToggle.substring(3);
						}
					}

					program.set(ip + offset, afterToggle);
					ip++;
					continue;
				}
			} else if (instruction.startsWith("inc")) {
				// INC
				char reg = instruction.charAt(4);
				registers.put(reg, registers.get(reg) + 1);
			} else if (instruction.startsWith("dec")) {
				// DEC
				char reg = instruction.charAt(4);
				registers.put(reg, registers.get(reg) - 1);
			} else if (cpyMatcher.matches()) {
				// CPY
				char destReg = cpyMatcher.group(2).charAt(0);
				Integer lop = intOrRegister(cpyMatcher.group(1), registers);
				registers.put(destReg, lop);
			} else if (jnzMatcher.matches()) {
				// JNZ
				Integer lop = intOrRegister(jnzMatcher.group(1), registers);
				Integer rop = intOrRegister(jnzMatcher.group(2), registers);

				if (!lop.equals(0)) {
					ip += rop;
					continue;
				}
			} else {
				throw new RuntimeException(String.format("Unknown instruction at line %d: \"%s\"", ip, instruction));
			}
			ip++;
		}

		System.out.println("Done!");
		System.out.println(registers);
		return "ERROR - there should be an endless loop in program";
	}

	private static Integer intOrRegister(String in, Map<Character, Integer> registers) {
		Integer result;
		try {
			result = Integer.parseInt(in);
		} catch (NumberFormatException e) {
			result = registers.get(in.charAt(0));
		}
		return result;
	}
}
