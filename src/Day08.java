import java.io.IOException;

public class Day08 extends Day {

    final static private String inputFile = "inputs/day08.txt";

    public Day08() {
        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day08 solution = new Day08();
        double time = System.currentTimeMillis();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s");
    }

    void parseInput() {
    }

    public long part1() {
        return 0;
    }

    public long part2() {
        return 0;
    }
}