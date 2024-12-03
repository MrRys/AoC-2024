import java.io.IOException;

public class Day04 extends Day {

    final static private String inputFile = "inputs/day04.txt";

    public Day04() {
        try {
            this.readFile(inputFile);
            this.parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day04 solution = new Day04();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
    }

    void parseInput() {
    }

    public long part1() {
        long result = 0;
        return result;
    }

    public long part2() {
        long result = 0;
        return result;
    }
}