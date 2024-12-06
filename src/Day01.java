import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day01 extends Day {

    final static private String inputFile = "inputs/day01.txt";

    private final List<Integer> group1;
    private final List<Integer> group2;

    public Day01() {
        group1 = new ArrayList<>();
        group2 = new ArrayList<>();

        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day01 solution = new Day01();
        double time = System.currentTimeMillis();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s");
    }

    void parseInput() {
        for (String line : getInput()) {
            String[] parts = line.split(" +");
            group1.add(Integer.parseInt(parts[0]));
            group2.add(Integer.parseInt(parts[1]));
        }
    }

    public long part1() {
        List<Integer> group1Sorted = group1.stream().sorted().toList();
        List<Integer> group2Sorted = group2.stream().sorted().toList();

        long result = 0;
        for (int i = 0; i < group1Sorted.size(); i++) {
            result += Math.abs(group1Sorted.get(i) - group2Sorted.get(i));
        }
        return result;
    }

    public long part2() {
        long result = 0;
        for (Integer id : group1) {
            result += (long) id * Collections.frequency(group2, id);
        }
        return result;
    }
}