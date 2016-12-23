package level_23;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * --- Day 23: Safe Cracking ---
 * <p>
 * This is one of the top floors of the nicest tower in EBHQ. The Easter Bunny's private office is here, complete with a safe hidden behind a painting, and who wouldn't hide a star in a safe behind a painting?
 * <p>
 * The safe has a digital screen and keypad for code entry. A sticky note attached to the safe has a password hint on it: "eggs". The painting is of a large rabbit coloring some eggs. You see 7.
 * <p>
 * When you go to type the code, though, nothing appears on the display; instead, the keypad comes apart in your hands, apparently having been smashed. Behind it is some kind of socket - one that matches a connector in your prototype computer! You pull apart the smashed keypad and extract the logic circuit, plug it into your computer, and plug your computer into the safe.
 * <p>
 * Now, you just need to figure out what output the keypad would have sent to the safe. You extract the assembunny code from the logic chip (your puzzle input).
 * <p>
 * The code looks like it uses almost the same architecture and instruction set that the monorail computer used! You should be able to use the same assembunny interpreter for this as you did there, but with one new instruction:
 * <p>
 * tgl x toggles the instruction x away (pointing at instructions like jnz does: positive means forward; negative means backward):
 * <p>
 * For one-argument instructions, inc becomes dec, and all other one-argument instructions become inc.
 * For two-argument instructions, jnz becomes cpy, and all other two-instructions become jnz.
 * The arguments of a toggled instruction are not affected.
 * If an attempt is made to toggle an instruction outside the program, nothing happens.
 * If toggling produces an invalid instruction (like cpy 1 2) and an attempt is later made to execute that instruction, skip it instead.
 * If tgl toggles itself (for example, if a is 0, tgl a would target itself and become inc a), the resulting instruction is not executed until the next time it is reached.
 * <p>
 * For example, given this program:
 * <p>
 * cpy 2 a
 * tgl a
 * tgl a
 * tgl a
 * cpy 1 a
 * dec a
 * dec a
 * <p>
 * cpy 2 a initializes register a to 2.
 * The first tgl a toggles an instruction a (2) away from it, which changes the third tgl a into inc a.
 * The second tgl a also modifies an instruction 2 away from it, which changes the cpy 1 a into jnz 1 a.
 * The fourth line, which is now inc a, increments a to 3.
 * Finally, the fifth line, which is now jnz 1 a, jumps a (3) instructions ahead, skipping the dec a instructions.
 * <p>
 * In this example, the final value in register a is 3.
 * <p>
 * The rest of the electronics seem to place the keypad entry (the number of eggs, 7) in register a, run the code, and then send the value left in register a to the safe.
 * <p>
 * What value should be sent to the safe?
 */
public class Level23 {

	private static List<String> program;
	private static Pattern cpyPattern = Pattern.compile("^cpy ([a-d]|-?[0-9]+) ([a-d])$");
	private static Pattern jnzPattern = Pattern.compile("^jnz ([a-d]|-?[0-9]+) ([a-d]|-?[0-9]+)$");

	public static void main(String[] args) throws IOException {
		program = Files.readAllLines(Paths.get("level_23/in1.txt"));
		//program = Files.readAllLines(Paths.get("level_23/testin.txt"));

		int result1 = calc(7);
		System.out.println("result 1: " + result1);

		program = Files.readAllLines(Paths.get("level_23/in1.txt")); // reset program, as the previous run modified it
		int result2 = calc(12);
		System.out.println("result 2: " + result2);
	}

	private static int calc(int level) {
		HashMap<Character, Integer> registers = new HashMap<>();
		registers.put('a', level);
		registers.put('b', 0);
		registers.put('c', 0);
		registers.put('d', 0);

		int ip = 0;
		while (ip < program.size()) {
			final String instruction = program.get(ip);
			//System.out.println("Processing: " + instruction);

			Matcher cpyMatcher = cpyPattern.matcher(instruction);
			Matcher jnzMatcher = jnzPattern.matcher(instruction);
			/*
			WARNING! fits only for in1.txt puzzle input, which contains a nested loop:
			4: cpy b c
			5: inc a
			6: dec c
			7: jnz c -2
			8: dec d
			9: jnz d -5
			...
			which means register A will be increased by 1 B*D times, so we will simply
			skip instruction number 4 putting correct values into registers
			 */
			if (level == 12 && ip == 4) {
				int newA = registers.get('d') * registers.get('b');
				registers.put('d', 0);
				registers.put('c', 0);
				registers.put('a', newA);
				ip = 10;
			} else {
				if (instruction.startsWith("tgl")) {
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
					//System.out.println("line: " + ip + ", lop: " + lop + ", rop: " + destReg);
				} else if (jnzMatcher.matches()) {
					// JNZ
					Integer lop = intOrRegister(jnzMatcher.group(1), registers);
					Integer rop = intOrRegister(jnzMatcher.group(2), registers);
					//System.out.println("line: " + ip + ", lop: " + lop + ", rop: " + rop);

					if (!lop.equals(0)) {
						ip += rop;
						//System.out.println("jumping to " + ip);
						continue;
					}
				} else {
					throw new RuntimeException(String.format("Unknown instruction at line %d: \"%s\"", ip, instruction));
				}
				ip++;
			}
		}

		System.out.println("Done!");
		System.out.println(registers);
		return registers.get('a');
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
