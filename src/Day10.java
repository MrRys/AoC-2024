import java.io.IOException;
import java.util.*;

public class Day10 extends Day {

    final static private String inputFile = "inputs/day10.txt";
    private final Map<Location, Set<Location>> trailheadDestinations;
    private final Map<Location, Integer> trailheadPaths;
    private Integer[][] topologicalMap;

    public Day10() {
        trailheadDestinations = new HashMap<>();
        trailheadPaths = new HashMap<>();

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
        List<String> input = getInput();
        topologicalMap = getInput().stream()
                .map(line -> Arrays.stream(line.split(""))
                        .map(Integer::parseInt).toArray(Integer[]::new))
                .toArray(Integer[][]::new);

        for (int i = 0; i < topologicalMap.length; i++) {
            for (int j = 0; j < topologicalMap[i].length; j++) {
                if (topologicalMap[i][j] == 0) {
                    trailheadDestinations.put(new Location(i, j), new HashSet<>());
                    trailheadPaths.put(new Location(i, j), 0);
                }
            }
        }
    }

    private boolean isHighestPoint(Location location) {
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

    private long countTrailheadPaths() {
        long result = 0;
        for (Integer score : trailheadPaths.values()) {
            result += score;
        }
        return result;
    }

    private long countTrailheadDestinations() {
        long result = 0;

        for (Set<Location> trailheadDestination : trailheadDestinations.values()) {
            result += trailheadDestination.size();
        }
        return result;
    }

    public long part1() {
        Stack<Location> stack = new Stack<>();

        for (Location trailhead : trailheadDestinations.keySet()) {
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
                            trailheadDestinations.get(trailhead).add(nextLocation);
                            continue;
                        }

                        stack.push(nextLocation);
                    }
                }
            }
        }

        return countTrailheadDestinations();
    }

    public long part2() {
        Stack<Location> stack = new Stack<>();

        for (Location trailhead : trailheadPaths.keySet()) {
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
                            trailheadPaths.put(trailhead, trailheadPaths.get(trailhead) + 1);
                            continue;
                        }

                        stack.push(nextLocation);
                    }
                }
            }
        }

        return countTrailheadPaths();
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