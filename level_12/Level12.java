package level_12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * --- Day 12: Leonardo's Monorail ---
 * <p>
 * You finally reach the top floor of this building: a garden with a slanted glass ceiling. Looks like there are no more stars to be had.
 * <p>
 * While sitting on a nearby bench amidst some tiger lilies, you manage to decrypt some of the files you extracted from the servers downstairs.
 * <p>
 * According to these documents, Easter Bunny HQ isn't just this building - it's a collection of buildings in the nearby area. They're all connected by a local monorail, and there's another building not far from here! Unfortunately, being night, the monorail is currently not operating.
 * <p>
 * You remotely connect to the monorail control systems and discover that the boot sequence expects a password. The password-checking logic (your puzzle input) is easy to extract, but the code it uses is strange: it's assembunny code designed for the new computer you just assembled. You'll have to execute the code and get the password.
 * <p>
 * The assembunny code you've extracted operates on four registers (a, b, c, and d) that start at 0 and can hold any integer. However, it seems to make use of only a few instructions:
 * <p>
 * cpy x y copies x (either an integer or the value of a register) into register y.
 * inc x increases the value of register x by one.
 * dec x decreases the value of register x by one.
 * jnz x y jumps to an instruction y away (positive means forward; negative means backward), but only if x is not zero.
 * <p>
 * The jnz instruction moves relative to itself: an offset of -1 would continue at the previous instruction, while an offset of 2 would skip over the next instruction.
 * <p>
 * For example:
 * <p>
 * cpy 41 a
 * inc a
 * inc a
 * dec a
 * jnz a 2
 * dec a
 * <p>
 * The above code would set register a to 41, increase its value by 2, decrease its value by 1, and then skip the last dec a (because a is not zero, so the jnz a 2 skips it), leaving register a at 42. When you move past the last instruction, the program halts.
 * <p>
 * After executing the assembunny code in your puzzle input, what value is left in register a?
 * <p>
 * <p>
 * --- Part Two ---
 * <p>
 * As you head down the fire escape to the monorail, you notice it didn't start; register c needs to be initialized to the position of the ignition key.
 * <p>
 * If you instead initialize register c to be 1, what value is now left in register a?
 */
public class Level12 {
	private static List<String> program;
	private static Pattern cpyPattern = Pattern.compile("^cpy ([a-d]|-?[0-9]+) ([a-d])$");
	private static Pattern jnzPattern = Pattern.compile("^jnz ([a-d]|-?[0-9]+) (-?[0-9]+)$");

	public static void main(String[] args) throws IOException {
		program = Files.readAllLines(Paths.get("level_12/in1.txt"));

		int result1 = calc(0);
		int result2 = calc(1);

		System.out.println("result 1: " + result1);
		System.out.println("result 2: " + result2);
	}

	private static int calc(int level) {
		HashMap<Character, Integer> registers = new HashMap<>();
		registers.put('a', 0);
		registers.put('b', 0);
		registers.put('c', level);
		registers.put('d', 0);

		int ip = 0;
		while (ip < program.size()) {
			final String instruction = program.get(ip);
			//System.out.println("Processing: " + instruction);

			Matcher cpyMatcher = cpyPattern.matcher(instruction);
			Matcher jnzMatcher = jnzPattern.matcher(instruction);
			if (instruction.startsWith("inc")) {
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
				Integer lop;
				try {
					lop = Integer.parseInt(cpyMatcher.group(1));
				} catch (NumberFormatException e) {
					lop = registers.get(cpyMatcher.group(1).charAt(0));
				}
				registers.put(destReg, lop);
				//System.out.println("line: " + ip + ", lop: " + lop + ", rop: " + destReg);
			} else if (jnzMatcher.matches()) {
				// JNZ
				Integer lop;
				try {
					lop = Integer.parseInt(jnzMatcher.group(1));
				} catch (NumberFormatException e) {
					lop = registers.get(jnzMatcher.group(1).charAt(0));
				}
				Integer rop = Integer.parseInt(jnzMatcher.group(2));
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

		System.out.println("Done!");
		System.out.println(registers);
		return registers.get('a');
	}


}
