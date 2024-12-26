import java.io.IOException;
import java.util.*;

public class Day18 extends Day {

    final static private String inputFile = "inputs/day18.txt";
    private final int gridSize = 71;
    private final Position startPosition = new Position(0, 0);
    private final Position endPosition = new Position(gridSize - 1, gridSize - 1);
    private List<Position> bytes;
    private char[][] workingGrid;

    public Day18() {
        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day18 solution = new Day18();
        double time = System.currentTimeMillis();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s");
    }

    void parseInput() {
        bytes = getInput().stream().map(line ->
                        Arrays.stream(line.split(","))
                                .map(Integer::parseInt).toList())
                .map(Position::new).toList();
    }

    private char[][] generateGrid(int byteCount) {
        char[][] grid = new char[gridSize][gridSize];
        for (int byteIndex = 0; byteIndex < byteCount; byteIndex++) {
            Position position = bytes.get(byteIndex);
            grid[position.row][position.col] = '#';
        }
        return grid;
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
        workingGrid = generateGrid(1024);
        return findShortestPathLength();
    }

    public long part2() {
        for (int byteCount = 1025; byteCount < bytes.size(); byteCount++) {
            workingGrid = generateGrid(byteCount);
            if (findShortestPathLength() < 0) {
                Position breakingByte = bytes.get(byteCount - 1);
                System.out.println(breakingByte.col + "," + breakingByte.row);
                return byteCount - 1;
            }
        }
        return -1;
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

        public Position(List<Integer> parameters) {
            this.row = parameters.getLast();
            this.col = parameters.getFirst();
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
            return workingGrid != null && position.row - 1 >= 0 && workingGrid[position.row - 1][position.col] != '#';
        }

        public boolean canMoveDown() {
            return workingGrid != null && position.row + 1 < gridSize && workingGrid[position.row + 1][position.col] != '#';
        }

        public boolean canMoveLeft() {
            return workingGrid != null && position.col - 1 >= 0 && workingGrid[position.row][position.col - 1] != '#';
        }

        public boolean canMoveRight() {
            return workingGrid != null && position.col + 1 < gridSize && workingGrid[position.row][position.col + 1] != '#';
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