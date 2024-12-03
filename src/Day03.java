import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 extends Day {

    final static private String inputFile = "inputs/day03.txt";

    private String memory;

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
        this.memory = String.join("", this.getInput());
    }

    private long executeMulInstructions(String memory) {
        long result = 0;
        Matcher matcher = Pattern.compile("mul\\(([0-9]+),([0-9]+)\\)").matcher(memory);
        while (matcher.find()) {
            result += Long.parseLong(matcher.group(1)) * Long.parseLong(matcher.group(2));
        }
        return result;
    }

    public long part1() {
        return executeMulInstructions(this.memory);
    }

    public long part2() {
        String doOnlyMemory = this.memory.replaceAll("don't\\(\\).*?do\\(\\)|don't\\(\\).*", "");
        return executeMulInstructions(doOnlyMemory);
    }
}