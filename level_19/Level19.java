package level_19;

/**
 * --- Day 19: An Elephant Named Joseph ---
 * <p>
 * The Elves contact you over a highly secure emergency channel. Back at the North Pole, the Elves are busy misunderstanding White Elephant parties.
 * <p>
 * Each Elf brings a present. They all sit in a circle, numbered starting with position 1. Then, starting with the first Elf, they take turns stealing all the presents from the Elf to their left. An Elf with no presents is removed from the circle and does not take turns.
 * <p>
 * For example, with five Elves (numbered 1 to 5):
 * <p>
 * 1
 * 5   2
 * 4 3
 * <p>
 * Elf 1 takes Elf 2's present.
 * Elf 2 has no presents and is skipped.
 * Elf 3 takes Elf 4's present.
 * Elf 4 has no presents and is also skipped.
 * Elf 5 takes Elf 1's two presents.
 * Neither Elf 1 nor Elf 2 have any presents, so both are skipped.
 * Elf 3 takes Elf 5's three presents.
 * <p>
 * So, with five Elves, the Elf that sits starting in position 3 gets all the presents.
 * <p>
 * With the number of Elves given in your puzzle input, which Elf gets all the presents?
 * <p>
 * Your puzzle input is 3005290.
 * <p>
 * --- Part Two ---
 * <p>
 * Realizing the folly of their present-exchange rules, the Elves agree to instead steal presents from the Elf directly across the circle. If two Elves are across the circle, the one on the left (from the perspective of the stealer) is stolen from. The other rules remain unchanged: Elves with no presents are removed from the circle entirely, and the other elves move in slightly to keep the circle evenly spaced.
 * <p>
 * For example, with five Elves (again numbered 1 to 5):
 * <p>
 * The Elves sit in a circle; Elf 1 goes first:
 * <p>
 * 1
 * 5   2
 * 4 3
 * <p>
 * Elves 3 and 4 are across the circle; Elf 3's present is stolen, being the one to the left. Elf 3 leaves the circle, and the rest of the Elves move in:
 * <p>
 * 1           1
 * 5   2  -->  5   2
 * 4 -          4
 * <p>
 * Elf 2 steals from the Elf directly across the circle, Elf 5:
 * <p>
 * 1         1
 * -   2  -->     2
 * 4         4
 * <p>
 * Next is Elf 4 who, choosing between Elves 1 and 2, steals from Elf 1:
 * <p>
 * -          2
 * 2  -->
 * 4          4
 * <p>
 * Finally, Elf 2 steals from Elf 4:
 * <p>
 * 2
 * -->  2
 * -
 * <p>
 * So, with five Elves, the Elf that sits starting in position 2 gets all the presents.
 * <p>
 * With the number of Elves given in your puzzle input, which Elf now gets all the presents?
 * <p>
 * Your puzzle input is still 3005290.
 */
public class Level19 {

	public static void main(String[] args) {

		//int target = 5; // test case
		int target = 3005290;
		int maxpow = 1;
		for (int i = 1; i < target; i *= 2) {
			if (i < target) {
				maxpow = i;
			}
		}
		int result1 = (target - maxpow) * 2 + 1;
		System.out.println("result1: " + result1);

		int result2 = 1;
		for (int i = 1; i < target; i++) {
			result2 %= i;
			result2++;
			if ((i + 1) / 2 < result2) {
				result2++;
			}
		}
		System.out.println("result2: " + result2);
	}

}
