import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day20 extends Day {

    final static private String inputFile = "inputs/day20.txt";

    private char[][] grid;
    private Position startPosition;
    private List<Position> path;

    public Day20() {
        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day20 solution = new Day20();
        double time = System.currentTimeMillis();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s");
    }

    void parseInput() {
        grid = getInput().stream().map(String::toCharArray).toArray(char[][]::new);

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == 'S') {
                    startPosition = new Position(row, col);
                    break;
                }
            }
        }
    }

    private List<Position> findPath() {
        List<Position> path = new ArrayList<>();

        Position currentPosition = startPosition;
        while (grid[currentPosition.row][currentPosition.col] != 'E') {
            path.add(currentPosition);
            for (int dRow = -1; dRow <= 1; dRow++) {
                for (int dCol = -1; dCol <= 1; dCol++) {
                    Position newPosition = new Position(currentPosition.row + dRow, currentPosition.col + dCol);
                    if (newPosition.row < 0 || newPosition.row >= grid.length ||
                            newPosition.col < 0 || newPosition.col >= grid[0].length ||
                            dRow == dCol || dRow == -dCol ||
                            grid[newPosition.row][newPosition.col] == '#' ||
                            path.size() > 1 && path.get(path.size() - 2).equals(newPosition)) {
                        continue;
                    }

                    currentPosition = newPosition;
                    dRow = 2;
                    break;
                }
            }
        }
        path.add(currentPosition);

        return path;
    }

    private int distance(Position p1, Position p2) {
        return Math.abs(p1.row - p2.row) + Math.abs(p1.col - p2.col);
    }

    private long findAllCheats(int skipLimit, int timeSaveMinimum) {
        if (path == null) {
            path = findPath();
        }

        long cheatCount = 0;
        for (int pathIndex1 = 0; pathIndex1 < path.size() - 1; pathIndex1++) {
            for (int pathIndex2 = pathIndex1 + 1; pathIndex2 < path.size(); pathIndex2++) {
                int distance = distance(path.get(pathIndex1), path.get(pathIndex2));
                if (distance > skipLimit) {
                    continue;
                }
                if (pathIndex2 - pathIndex1 >= timeSaveMinimum + distance) {
                    cheatCount++;
                }
            }
        }
        return cheatCount;
    }

    public long part1() {
        return findAllCheats(2, 100);
    }

    public long part2() {
        return findAllCheats(20, 100);
    }

    private static class Position {
        int row;
        int col;

        public Position(int row, int col) {
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

            if (!(o instanceof Position other)) {
                return false;
            }

            return this.row == other.row && this.col == other.col;
        }

        public String toString() {
            return "(" + row + ", " + col + ")";
        }
    }
}