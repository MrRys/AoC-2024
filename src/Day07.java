import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Day07 extends Day {

    final static private String inputFile = "inputs/day07.txt";

    private List<Long[]> equations;

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
        double time = System.currentTimeMillis();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s");
    }

    void parseInput() {
        equations = getInput().parallelStream().map(
                line -> Arrays.stream(line.replace(":", "").split(" "))
                        .parallel().map(Long::parseLong).toArray(Long[]::new))
                .toList();
    }

    private long add(long valueA, long valueB) {
        return valueA + valueB;
    }

    private long mul(long valueA, long valueB) {
        return valueA * valueB;
    }

    private long concat(long valueA, long valueB) {
        long length = (long) (Math.log10(valueB) + 1);
        return valueA * (long) Math.pow(10, length) + valueB;
    }

    private boolean isComputablePart1(long currValue, long result, Long[] equation, int index) {
        if (currValue == result) {
            return true;
        }

        if (currValue > result || index >= equation.length) {
            return false;
        }

        long nextValue = equation[index];
        return isComputablePart1(add(currValue, nextValue), result, equation, index + 1)
                || isComputablePart1(mul(currValue, nextValue), result, equation, index + 1);
    }

    private boolean isComputablePart2(long currValue, long result, Long[] equation, int index) {
        if (currValue == result) {
            return true;
        }

        if (currValue > result || index >= equation.length) {
            return false;
        }

        long nextValue = equation[index];
        return isComputablePart2(add(currValue, nextValue), result, equation, index + 1)
                || isComputablePart2(mul(currValue, nextValue), result, equation, index + 1)
                || isComputablePart2(concat(currValue, nextValue), result, equation, index + 1);
    }

    public long part1() {
        return equations.parallelStream().mapToLong(equation ->
                isComputablePart1(equation[1], equation[0], equation, 2) ? equation[0] : 0
        ).sum();
    }

    public long part2() {
        return equations.parallelStream().mapToLong(equation ->
                isComputablePart2(equation[1], equation[0], equation, 2) ? equation[0] : 0
        ).sum();
    }
}