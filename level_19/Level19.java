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
 */
public class Level19 {

	public static void main(String[] args) {

		//int target = 5; // test case
		int target = 3005290;
		int maxpow = 1;
		for(int i = 1; i < target; i*=2) {
			if(i < target) {
				maxpow = i;
			}
		}
		int result1 = (target - maxpow) * 2 + 1;
		System.out.println("result1: " + result1);

	}

}
