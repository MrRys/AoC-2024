import java.io.IOException;
import java.util.*;

public class Day07 extends Day {

    final static private String inputFile = "inputs/day07.txt";

    public Day07() {
        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day07 solution = new Day07();
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