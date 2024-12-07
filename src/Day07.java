import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Day07 extends Day {

    final static private String inputFile = "inputs/day07.txt";

    private List<List<Long>> equations;

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
        equations = getInput().stream()
                .map(line -> Arrays.stream(line.replace(":", "").split(" "))
                        .map(Long::parseLong).toList())
                .toList();
    }

    private boolean isComputablePart1(ComputeNode node, Long result, List<Long> equation, int index) {
        if (Objects.equals(node.value, result)) {
            return true;
        }

        if (node.value > result || index >= equation.size()) {
            return false;
        }

        return isComputablePart1(node.add(equation.get(index)), result, equation, index + 1)
                || isComputablePart1(node.mul(equation.get(index)), result, equation, index + 1);
    }

    private boolean isComputablePart2(ComputeNode node, Long result, List<Long> equation, int index) {
        if (Objects.equals(node.value, result)) {
            return true;
        }

        if (node.value > result || index >= equation.size()) {
            return false;
        }

        return isComputablePart2(node.add(equation.get(index)), result, equation, index + 1)
                || isComputablePart2(node.mul(equation.get(index)), result, equation, index + 1)
                || isComputablePart2(node.concat(equation.get(index)), result, equation, index + 1);
    }

    public long part1() {
        long result = 0;
        for (List<Long> equation : equations) {
            ComputeNode root = new ComputeNode(equation.get(1));
            if (isComputablePart1(root, equation.getFirst(), equation, 2)) {
                result += equation.getFirst();
            }
        }
        return result;
    }

    public long part2() {
        long result = 0;
        for (List<Long> equation : equations) {
            ComputeNode root = new ComputeNode(equation.get(1));
            if (isComputablePart2(root, equation.getFirst(), equation, 2)) {
                result += equation.getFirst();
            }
        }
        return result;
    }

    private static class ComputeNode {
        Long value;

        ComputeNode(Long value) {
            this.value = value;
        }

        ComputeNode add(Long value) {
            return new ComputeNode(this.value + value);
        }

        ComputeNode mul(Long value) {
            return new ComputeNode(this.value * value);
        }

        ComputeNode concat(Long value) {
            long length = (long) (Math.log10(value) + 1);
            return new ComputeNode(this.value * (long) Math.pow(10, length) + value);
        }
    }
}