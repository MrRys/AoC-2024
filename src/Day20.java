import java.io.IOException;
import java.util.*;

public class Day20 extends Day {

    final static private String inputFile = "inputs/day20.txt";

    private char[][] grid;
    private Position startPosition;
    private Position endPosition;
    private List<Position> breakableWalls;
    private long defaultShortestPathLength = 0;

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
        breakableWalls = new ArrayList<>();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                switch (grid[row][col]) {
                    case 'S' -> startPosition = new Position(row, col);
                    case 'E' -> endPosition = new Position(row, col);
                    case '#' -> {
                        if (isBreakableWall(row, col)) {
                            breakableWalls.add(new Position(row, col));
                        }
                    }
                }
            }
        }
    }

    private boolean isBreakableWall(int row, int col) {
        return upEmpty(row, col) && downEmpty(row, col) && !leftEmpty(row, col) && !rightEmpty(row, col) ||
                !upEmpty(row, col) && !downEmpty(row, col) && leftEmpty(row, col) && rightEmpty(row, col) ||
                upEmpty(row, col) && !downEmpty(row, col) && leftEmpty(row, col) && rightEmpty(row, col) ||
                !upEmpty(row, col) && downEmpty(row, col) && leftEmpty(row, col) && rightEmpty(row, col) ||
                upEmpty(row, col) && downEmpty(row, col) && !leftEmpty(row, col) && rightEmpty(row, col) ||
                upEmpty(row, col) && downEmpty(row, col) && leftEmpty(row, col) && !rightEmpty(row, col) ||
                upEmpty(row, col) && leftEmpty(row, col) && !upLeftEmpty(row, col) ||
                upEmpty(row, col) && rightEmpty(row, col) && !upRightEmpty(row, col);
    }

    private boolean upEmpty(int row, int col) {
        return row - 1 >= 0 && grid[row - 1][col] != '#';
    }

    private boolean downEmpty(int row, int col) {
        return row + 1 < grid.length && grid[row + 1][col] != '#';
    }

    private boolean leftEmpty(int row, int col) {
        return col - 1 >= 0 && grid[row][col - 1] != '#';
    }

    private boolean rightEmpty(int row, int col) {
        return col + 1 < grid[0].length && grid[row][col + 1] != '#';
    }

    private boolean upLeftEmpty(int row, int col) {
        return row - 1 >= 0 && col - 1 >= 0 && grid[row - 1][col - 1] != '#';
    }

    private boolean upRightEmpty(int row, int col) {
        return row - 1 >= 0 && col + 1 < grid[0].length && grid[row - 1][col + 1] != '#';
    }

    private long findShortestPathLength() {
        Queue<Node> queue = new LinkedList<>();
        Set<Position> visited = new HashSet<>();

        Node startNode = new Node(startPosition);
        queue.add(startNode);
        visited.add(startNode.position);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();

            if (currentNode.isEnd()) {
                return currentNode.gScore();
            }

            if (currentNode.canMoveUp()) {
                Node newNode = currentNode.moveUp();
                if (!visited.contains(newNode.position)) {
                    visited.add(newNode.position);
                    queue.add(newNode);
                }
            }

            if (currentNode.canMoveDown()) {
                Node newNode = currentNode.moveDown();
                if (!visited.contains(newNode.position)) {
                    visited.add(newNode.position);
                    queue.add(newNode);
                }
            }

            if (currentNode.canMoveLeft()) {
                Node newNode = currentNode.moveLeft();
                if (!visited.contains(newNode.position)) {
                    visited.add(newNode.position);
                    queue.add(newNode);
                }
            }

            if (currentNode.canMoveRight()) {
                Node newNode = currentNode.moveRight();
                if (!visited.contains(newNode.position)) {
                    visited.add(newNode.position);
                    queue.add(newNode);
                }
            }
        }

        return -1;
    }

    public long part1() {
        if (defaultShortestPathLength == 0) {
            defaultShortestPathLength = findShortestPathLength();
        }

        long result = 0;
        for (Position breakableWall : breakableWalls) {
            grid[breakableWall.row][breakableWall.col] = '.';
            if (defaultShortestPathLength - findShortestPathLength() >= 100) {
                result++;
            }
            grid[breakableWall.row][breakableWall.col] = '#';
        }
        return result;
    }

    public long part2() {
        return 0;
    }

    private static class Position {
        int row;
        int col;

        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public Position(Position other) {
            this.row = other.row;
            this.col = other.col;
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

    private class Node {
        Position position;
        long pathLength;

        public Node(Position position) {
            this.position = position;
            this.pathLength = 0;
        }

        public Node(Node other) {
            this.position = new Position(other.position);
            this.pathLength = other.pathLength;
        }

        public Node moveUp() {
            Node newNode = new Node(this);
            newNode.position.row--;
            newNode.pathLength++;
            return newNode;
        }

        public Node moveDown() {
            Node newNode = new Node(this);
            newNode.position.row++;
            newNode.pathLength++;
            return newNode;
        }

        public Node moveLeft() {
            Node newNode = new Node(this);
            newNode.position.col--;
            newNode.pathLength++;
            return newNode;
        }

        public Node moveRight() {
            Node newNode = new Node(this);
            newNode.position.col++;
            newNode.pathLength++;
            return newNode;
        }

        public boolean canMoveUp() {
            return position.row - 1 >= 0 && grid[position.row - 1][position.col] != '#';
        }

        public boolean canMoveDown() {
            return position.row + 1 < grid.length && grid[position.row + 1][position.col] != '#';
        }

        public boolean canMoveLeft() {
            return position.col - 1 >= 0 && grid[position.row][position.col - 1] != '#';
        }

        public boolean canMoveRight() {
            return position.col + 1 < grid[0].length && grid[position.row][position.col + 1] != '#';
        }

        public boolean isEnd() {
            return position.equals(endPosition);
        }

        public long gScore() {
            return pathLength;
        }

        public String toString() {
            return "{" + position + ", " + pathLength + "}";
        }
    }
}