import java.io.IOException;
import java.util.*;

public class Day11 extends Day {

    final static private String inputFile = "inputs/day11.txt";
    private final Map<State, Long> memory;
    private List<Long> stones;

    public Day11() {
        memory = new HashMap<>();
        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day11 solution = new Day11();
        double time = System.currentTimeMillis();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s");
    }

    void parseInput() {
        stones = Arrays.stream(getInput().getFirst().split(" ")).map(Long::parseLong).toList();
    }

    private long countStones(long stone, int blinksLeft) {
        if (blinksLeft == 0) {
            return 1;
        }

        State state = new State(stone, blinksLeft);
        if (memory.containsKey(state)) {
            return memory.get(state);
        }

        long result;
        long digits = (long) Math.log10(stone) + 1;
        if (stone == 0) {
            result = countStones(1, blinksLeft - 1);
        } else if (digits % 2 == 0) {
            long div = (long) Math.pow(10, digits / 2.0);
            result = countStones(stone / div, blinksLeft - 1)
                    + countStones(stone % div, blinksLeft - 1);
        } else {
            result = countStones(stone * 2024, blinksLeft - 1);
        }

        memory.put(state, result);
        return result;
    }

    public long part1() {
        long result = 0;
        for (long stone : stones) {
            result += countStones(stone, 25);
        }
        return result;
    }

    public long part2() {
        long result = 0;
        for (long stone : stones) {
            result += countStones(stone, 75);
        }
        return result;
    }

    private static class State {
        long stone;
        int blink;

        public State(long stone, int blink) {
            this.stone = stone;
            this.blink = blink;
        }

        @Override
        public int hashCode() {
            return Objects.hash(stone, blink);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (!(o instanceof State other)) {
                return false;
            }

            return this.stone == other.stone && this.blink == other.blink;
        }
    }
}