import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day01 extends Day {

    final static private String inputFile = "inputs/day01.txt";

    private final List<Integer> group1;
    private final List<Integer> group2;

    public Day01() {
        this.group1 = new ArrayList<>();
        this.group2 = new ArrayList<>();

        try {
            this.readFile(inputFile);
            this.parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }

    }

    public static void main(String[] args) {
        Day01 solution = new Day01();
        long resultPart1 = solution.part1();
        long resultPart2 = solution.part2();
        System.out.println("Part 1: " + resultPart1);
        System.out.println("Part 2: " + resultPart2);
    }

    void parseInput() {
        for (String line : this.input) {
            String[] parts = line.split(" +");
            group1.add(Integer.parseInt(parts[0]));
            group2.add(Integer.parseInt(parts[1]));
        }
    }

    long part1() {
        group1.sort(null);
        group2.sort(null);
        long result = 0;
        for (int i = 0; i < group1.size(); i++) {
            result += Math.abs(group1.get(i) - group2.get(i));
        }
        return result;
    }

    long part2() {
        long result = 0;
        for (Integer id : group1) {
            result += (long) id * Collections.frequency(group2, id);
        }
        return result;
    }
}