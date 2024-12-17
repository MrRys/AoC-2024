import java.io.IOException;
import java.util.*;

public class Day12 extends Day {

    final static private String inputFile = "inputs/day12.txt";
    private final List<Region> regions;
    private char[][] grid;

    public Day12() {
        regions = new ArrayList<>();

        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day12 solution = new Day12();
        double time = System.currentTimeMillis();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s");
    }

    void parseInput() {
        grid = getInput().stream().map(String::toCharArray).toArray(char[][]::new);
    }


    private Region mapRegion(Position initPosition, Set<Position> inSomeRegion) {
        char plantType = grid[initPosition.row][initPosition.col];
        Region region = new Region(plantType);

        Stack<Position> stack = new Stack<>();
        stack.push(initPosition);
        region.addPosition(initPosition);

        while (!stack.isEmpty()) {
            Position currPos = stack.pop();

            for (int dRow = -1; dRow <= 1; dRow++) {
                for (int dCol = -1; dCol <= 1; dCol++) {
                    if ((dRow == dCol) || (dRow != 0 && dCol != 0)) {
                        continue;
                    }

                    Position newPosition = new Position(currPos.row + dRow, currPos.col + dCol);
                    if (newPosition.row < 0 || newPosition.row >= grid.length || newPosition.col < 0 || newPosition.col >= grid[0].length) {
                        continue;
                    }

                    if (region.containsPosition(newPosition)) {
                        continue;
                    }

                    if (grid[newPosition.row][newPosition.col] == plantType) {
                        stack.push(newPosition);
                        region.addPosition(newPosition);
                    }
                }
            }
        }

        inSomeRegion.addAll(region.getPositions());
        return region;
    }

    private void mapAllRegions() {
        Set<Position> inSomeRegion = new HashSet<>();

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                Position initPosition = new Position(row, col);

                if (inSomeRegion.contains(initPosition)) {
                    continue;
                }

                regions.add(mapRegion(initPosition, inSomeRegion));
            }
        }
    }

    public long part1() {
        if (regions.isEmpty()) {
            mapAllRegions();
        }

        long result = 0;
        for (Region region : regions) {
            result += region.getArea() * region.getPerimeter();
        }
        return result;
    }

    public long part2() {
        if (regions.isEmpty()) {
            mapAllRegions();
        }

        long result = 0;
        for (Region region : regions) {
            result += region.getArea() * region.getSides();
        }
        return result;
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

    private class Region {
        private final char plantType;
        private final Set<Position> positions;

        private int minRow;
        private int maxRow;
        private int minCol;
        private int maxCol;

        public Region(char plantType) {
            this.plantType = plantType;
            this.positions = new HashSet<>();
        }

        public Set<Position> getPositions() {
            return positions;
        }

        public boolean containsPosition(Position position) {
            return positions.contains(position);
        }

        public void addPosition(Position position) {
            positions.add(position);

            minRow = Math.min(minRow, position.row);
            maxRow = Math.max(maxRow, position.row);
            minCol = Math.min(minCol, position.col);
            maxCol = Math.max(maxCol, position.col);
        }

        public long getArea() {
            return positions.size();
        }

        public long getPerimeter() {
            long perimeter = 0;

            for (Position position : positions) {
                if (position.row - 1 < 0 || grid[position.row - 1][position.col] != plantType) {
                    perimeter++;
                }
                if (position.row + 1 >= grid.length || grid[position.row + 1][position.col] != plantType) {
                    perimeter++;
                }
                if (position.col - 1 < 0 || grid[position.row][position.col - 1] != plantType) {
                    perimeter++;
                }
                if (position.col + 1 >= grid[0].length || grid[position.row][position.col + 1] != plantType) {
                    perimeter++;
                }
            }
            return perimeter;
        }

        private long countCorners(Position position) {
            int same = 0;
            for (int dRow = 0; dRow <= 1; dRow++) {
                for (int dCol = 0; dCol <= 1; dCol++) {
                    Position newPosition = new Position(position.row + dRow, position.col + dCol);
                    if (positions.contains(newPosition)) {
                        same++;
                    }
                }
            }

            boolean posDiagonal = positions.contains(position) && positions.contains(new Position(position.row + 1, position.col + 1));
            boolean negDiagonal = !positions.contains(position) && !positions.contains(new Position(position.row + 1, position.col + 1));
            return (same == 1 ? 1 : 0) + (same == 3 ? 1 : 0) + (same == 2 && (posDiagonal || negDiagonal) ? 2 : 0);
        }

        public long getSides() {
            long sides = 0;
            for (int row = minRow - 1; row <= maxRow; row++) {
                for (int col = minCol - 1; col <= maxCol; col++) {
                    sides += countCorners(new Position(row, col));
                }
            }
            return sides;
        }

        public String toString() {
            return "{Plant: " + plantType + ", Positions: " + positions + "}";
        }
    }
}