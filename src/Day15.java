import java.io.IOException;
import java.util.*;

public class Day15 extends Day {

    final static private String inputFile = "inputs/day15.txt";

    private char[][] gridP1;
    private Position robotPositionP1;

    private char[][] gridP2;
    private Position robotPositionP2;
    private String commands;

    public Day15() {
        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day15 solution = new Day15();
        double time = System.currentTimeMillis();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s");
    }

    void parseInput() {
        List<String> input = getInput();
        int gridEnd = input.indexOf("");
        gridP1 = input.subList(0, gridEnd).stream().map(String::toCharArray).toArray(char[][]::new);

        gridP2 = new char[gridP1.length][gridP1[0].length * 2];
        for (int row = 0; row < gridP1.length; row++) {
            for (int col = 0; col < gridP1[row].length; col++) {
                if (gridP1[row][col] == '#' || gridP1[row][col] == '.') {
                    gridP2[row][col * 2] = gridP1[row][col];
                    gridP2[row][col * 2 + 1] = gridP1[row][col];
                } else if (gridP1[row][col] == 'O') {
                    gridP2[row][col * 2] = '[';
                    gridP2[row][col * 2 + 1] = ']';
                } else {
                    gridP2[row][col * 2] = gridP1[row][col];
                    gridP2[row][col * 2 + 1] = '.';

                    robotPositionP1 = new Position(row, col);
                    robotPositionP2 = new Position(row, col * 2);
                }
            }
        }

        commands = String.join("", input.subList(gridEnd + 1, input.size()));
    }

    private long sumCoordinates(char[][] grid) {
        long sum = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == 'O' || grid[row][col] == '[') {
                    sum += row * 100L + col;
                }
            }
        }
        return sum;
    }

    private boolean moveUpP1(char[][] grid, Position position) {
        if (grid[position.row][position.col] == '#') {
            return false;
        }

        if (grid[position.row][position.col] == '.') {
            return true;
        }

        boolean canMove = moveUpP1(grid, new Position(position.row - 1, position.col));
        if (canMove) {
            grid[position.row - 1][position.col] = grid[position.row][position.col];
            grid[position.row][position.col] = '.';
        }

        return canMove;
    }

    private boolean moveDownP1(char[][] grid, Position position) {
        if (grid[position.row][position.col] == '#') {
            return false;
        }

        if (grid[position.row][position.col] == '.') {
            return true;
        }

        boolean canMove = moveDownP1(grid, new Position(position.row + 1, position.col));
        if (canMove) {
            grid[position.row + 1][position.col] = grid[position.row][position.col];
            grid[position.row][position.col] = '.';
        }

        return canMove;
    }

    private boolean moveLeft(char[][] grid, Position position) {
        if (grid[position.row][position.col] == '#') {
            return false;
        }

        if (grid[position.row][position.col] == '.') {
            return true;
        }

        boolean canMove = moveLeft(grid, new Position(position.row, position.col - 1));
        if (canMove) {
            grid[position.row][position.col - 1] = grid[position.row][position.col];
            grid[position.row][position.col] = '.';
        }

        return canMove;
    }

    private boolean moveRight(char[][] grid, Position position) {
        if (grid[position.row][position.col] == '#') {
            return false;
        }

        if (grid[position.row][position.col] == '.') {
            return true;
        }

        boolean canMove = moveRight(grid, new Position(position.row, position.col + 1));
        if (canMove) {
            grid[position.row][position.col + 1] = grid[position.row][position.col];
            grid[position.row][position.col] = '.';
        }

        return canMove;
    }

    private void moveP1(char command) {
        switch (command) {
            case '^' -> {
                if (moveUpP1(gridP1, robotPositionP1)) {
                    robotPositionP1.row--;
                }
            }
            case 'v' -> {
                if (moveDownP1(gridP1, robotPositionP1)) {
                    robotPositionP1.row++;
                }
            }
            case '<' -> {
                if (moveLeft(gridP1, robotPositionP1)) {
                    robotPositionP1.col--;
                }
            }
            case '>' -> {
                if (moveRight(gridP1, robotPositionP1)) {
                    robotPositionP1.col++;
                }
            }
        }
    }

    public long part1() {
        for (char command : commands.toCharArray()) {
            moveP1(command);
        }
        return sumCoordinates(gridP1);
    }

    private boolean moveUpP2(char[][] grid, Position position) {
        Queue<Position> queue = new LinkedList<>();
        Set<Position> toMove = new HashSet<>();
        queue.add(position);

        int minRow = Integer.MAX_VALUE;
        int minCol = Integer.MAX_VALUE;
        int maxRow = Integer.MIN_VALUE;
        int maxCol = Integer.MIN_VALUE;

        while (!queue.isEmpty()) {
            Position current = queue.poll();
            if (toMove.contains(current)) {
                continue;
            }
            if (grid[current.row][current.col] == '#') {
                return false;
            }
            if (grid[current.row][current.col] == '.') {
                continue;
            }

            toMove.add(current);
            minRow = Math.min(minRow, current.row);
            maxRow = Math.max(maxRow, current.row);
            minCol = Math.min(minCol, current.col);
            maxCol = Math.max(maxCol, current.col);

            if (grid[current.row][current.col] == '@') {
                queue.add(new Position(current.row - 1, current.col));
            } else if (grid[current.row][current.col] == '[') {
                queue.add(new Position(current.row, current.col + 1));
                queue.add(new Position(current.row - 1, current.col));
                queue.add(new Position(current.row - 1, current.col + 1));
            } else {
                queue.add(new Position(current.row, current.col - 1));
                queue.add(new Position(current.row - 1, current.col));
                queue.add(new Position(current.row - 1, current.col - 1));
            }
        }

        for (int row = minRow; row <= maxRow; row++) {
            for (int col = minCol; col <= maxCol; col++) {
                if (toMove.contains(new Position(row, col))) {
                    grid[row - 1][col] = grid[row][col];
                    grid[row][col] = '.';
                }
            }
        }
        return true;
    }

    private boolean moveDownP2(char[][] grid, Position position) {
        Queue<Position> queue = new LinkedList<>();
        Set<Position> toMove = new HashSet<>();
        queue.add(position);

        int minRow = Integer.MAX_VALUE;
        int minCol = Integer.MAX_VALUE;
        int maxRow = Integer.MIN_VALUE;
        int maxCol = Integer.MIN_VALUE;

        while (!queue.isEmpty()) {
            Position current = queue.poll();
            if (toMove.contains(current)) {
                continue;
            }
            if (grid[current.row][current.col] == '#') {
                return false;
            }
            if (grid[current.row][current.col] == '.') {
                continue;
            }

            toMove.add(current);
            minRow = Math.min(minRow, current.row);
            maxRow = Math.max(maxRow, current.row);
            minCol = Math.min(minCol, current.col);
            maxCol = Math.max(maxCol, current.col);

            if (grid[current.row][current.col] == '@') {
                queue.add(new Position(current.row + 1, current.col));
            } else if (grid[current.row][current.col] == '[') {
                queue.add(new Position(current.row, current.col + 1));
                queue.add(new Position(current.row + 1, current.col));
                queue.add(new Position(current.row + 1, current.col + 1));
            } else {
                queue.add(new Position(current.row, current.col - 1));
                queue.add(new Position(current.row + 1, current.col));
                queue.add(new Position(current.row + 1, current.col - 1));
            }
        }

        for (int row = maxRow; row >= minRow; row--) {
            for (int col = maxCol; col >= minCol; col--) {
                if (toMove.contains(new Position(row, col))) {
                    grid[row + 1][col] = grid[row][col];
                    grid[row][col] = '.';
                }
            }
        }
        return true;
    }

    private void moveP2(char command) {
        switch (command) {
            case '^' -> {
                if (moveUpP2(gridP2, robotPositionP2)) {
                    robotPositionP2.row--;
                }
            }
            case 'v' -> {
                if (moveDownP2(gridP2, robotPositionP2)) {
                    robotPositionP2.row++;
                }
            }
            case '<' -> {
                if (moveLeft(gridP2, robotPositionP2)) {
                    robotPositionP2.col--;
                }
            }
            case '>' -> {
                if (moveRight(gridP2, robotPositionP2)) {
                    robotPositionP2.col++;
                }
            }
        }
    }

    public long part2() {
        for (char command : commands.toCharArray()) {
            moveP2(command);
        }
        return sumCoordinates(gridP2);
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