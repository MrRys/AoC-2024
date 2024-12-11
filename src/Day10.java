import java.io.IOException;
import java.util.*;

public class Day10 extends Day {

    final static private String inputFile = "inputs/day10.txt";
    private final List<Location> trailheads;
    private Byte[][] topologicalMap;

    public Day10() {
        trailheads = new ArrayList<>();

        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day10 solution = new Day10();
        double time = System.currentTimeMillis();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s");
    }

    void parseInput() {
        topologicalMap = getInput().stream()
                .map(line -> Arrays.stream(line.split(""))
                        .map(Byte::parseByte).toArray(Byte[]::new))
                .toArray(Byte[][]::new);

        for (int i = 0; i < topologicalMap.length; i++) {
            for (int j = 0; j < topologicalMap[i].length; j++) {
                if (topologicalMap[i][j] == 0) {
                    trailheads.add(new Location(i, j));
                }
            }
        }
    }

    private boolean isHighestPoint(Location location) {
        if (location.row < 0 || location.row >= topologicalMap.length
                || location.col < 0 || location.col >= topologicalMap[0].length) {
            return false;
        }
        return topologicalMap[location.row][location.col] == 9;
    }

    private boolean notValidMove(int dRow, int dCol) {
        return (dRow == dCol) || (dRow != 0 && dCol != 0);
    }

    private boolean notNextStep(Location currLocation, Location nextLocation) {
        return nextLocation.row < 0 || nextLocation.row >= topologicalMap.length
                || nextLocation.col < 0 || nextLocation.col >= topologicalMap[nextLocation.row].length
                || topologicalMap[currLocation.row][currLocation.col] != topologicalMap[nextLocation.row][nextLocation.col] - 1;
    }

    public long part1() {
        long result = 0;
        Stack<Location> stack = new Stack<>();
        Set<Location> found = new HashSet<>();

        for (Location trailhead : trailheads) {
            stack.push(trailhead);

            while (!stack.empty()) {
                Location currLocation = stack.pop();

                for (int dRow = -1; dRow <= 1; dRow++) {
                    for (int dCol = -1; dCol <= 1; dCol++) {
                        Location nextLocation = new Location(currLocation.row + dRow, currLocation.col + dCol);

                        if (notValidMove(dRow, dCol) || notNextStep(currLocation, nextLocation)) {
                            continue;
                        }

                        if (isHighestPoint(nextLocation) && !found.contains(nextLocation)) {
                            result++;
                            found.add(nextLocation);
                            continue;
                        }

                        stack.push(nextLocation);
                    }
                }
            }
            found.clear();
        }

        return result;
    }

    public long part2() {
        long result = 0;
        Stack<Location> stack = new Stack<>();

        for (Location trailhead : trailheads) {
            stack.push(trailhead);

            while (!stack.empty()) {
                Location currLocation = stack.pop();

                for (int dRow = -1; dRow <= 1; dRow++) {
                    for (int dCol = -1; dCol <= 1; dCol++) {
                        Location nextLocation = new Location(currLocation.row + dRow, currLocation.col + dCol);

                        if (notValidMove(dRow, dCol) || notNextStep(currLocation, nextLocation)) {
                            continue;
                        }

                        if (isHighestPoint(nextLocation)) {
                            result++;
                            continue;
                        }

                        stack.push(nextLocation);
                    }
                }
            }
        }

        return result;
    }

    private static class Location {
        int row;
        int col;

        public Location(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (!(o instanceof Location other)) {
                return false;
            }

            return this.row == other.row && this.col == other.col;
        }

        public String toString() {
            return "(" + row + ", " + col + ")";
        }
    }
}