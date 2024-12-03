import java.io.IOException;

public class Day03 extends Day {

    final static private String inputFile = "inputs/day03.txt";

    public Day03() {

        try {
            this.readFile(inputFile);
            this.parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }

    }

    public static void main(String[] args) {
        Day03 solution = new Day03();
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