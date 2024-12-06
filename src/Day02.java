import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class Day02 extends Day {

    final static private String inputFile = "inputs/day02.txt";

    private List<List<Integer>> reports;

    public Day02() {
        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day02 solution = new Day02();
        double time = System.currentTimeMillis();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s");
    }

    void parseInput() {
        reports = getInput().stream()
                .map(line -> Stream.of(line.split(" "))
                        .map(Integer::parseInt)
                        .toList())
                .toList();
    }

    private boolean isSafeIncrease(int a, int b) {
        return a < b && a + 3 >= b;
    }


    private boolean isSafeDecrease(int a, int b) {
        return a > b && b + 3 >= a;
    }

    private boolean isSafe(List<Integer> report) {
        boolean isSafeIncreaseResult = true;
        boolean isSafeDecreaseResult = true;
        for (int i = 0, j = 1; j < report.size(); i++, j++) {
            if (!isSafeIncrease(report.get(i), report.get(j))) {
                isSafeIncreaseResult = false;
            }

            if (!isSafeDecrease(report.get(i), report.get(j))) {
                isSafeDecreaseResult = false;
            }
        }
        return isSafeIncreaseResult || isSafeDecreaseResult;
    }

    public long part1() {
        long result = 0;
        for (List<Integer> report : reports) {
            result += isSafe(report) ? 1 : 0;
        }
        return result;
    }

    public long part2() {
        long result = 0;
        for (List<Integer> report : reports) {
            for (int i = 0; i < report.size(); i++) {
                if (isSafe(Stream.of(report.subList(0, i), report.subList(i + 1, report.size())).flatMap(Collection::stream).toList())) {
                    result++;
                    break;
                }
            }
        }
        return result;
    }
}