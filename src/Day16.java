import java.io.IOException;
import java.util.*;

public class Day16 extends Day {

    final static private String inputFile = "inputs/day16.txt";

    private Position startPosition;
    private Position endPosition;

    private char[][] grid;

    private List<Node> shortestPaths;

    public Day16() {
        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day16 solution = new Day16();
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
                } else if (grid[row][col] == 'E') {
                    endPosition = new Position(row, col);
                }
            }
        }
    }

    private boolean isEmptyTile(int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[row].length && grid[row][col] != '#';
    }

    private long[][][] generateVisited() {
        long[][][] visited = new long[grid.length][grid[0].length][4];
        for (long[][] row : visited) {
            for (long[] dirs : row) {
                Arrays.fill(dirs, Long.MAX_VALUE);
            }
        }
        return visited;
    }

    private List<Node> findShortestPaths() {
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> Math.toIntExact(a.fScore() - b.fScore()));

        long[][][] visited = generateVisited();

        Node startNode = new Node(startPosition);
        pq.add(startNode);
        visited[startNode.getRow()][startNode.getCol()][startNode.getDirId()] = startNode.fScore();

        long bestScore = Long.MAX_VALUE;
        List<Node> shortestPaths = new ArrayList<>();

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (current.isEnd()) {
                if (current.fScore() > bestScore) {
                    return shortestPaths;
                }

                shortestPaths.add(current);
                bestScore = current.fScore();
            }

            if (current.canMove()) {
                Node nextNode = current.move();
                if (checkAndAdd(visited, nextNode)) {
                    pq.add(nextNode);
                }
            }

            if (current.canTurnLeft()) {
                Node nextNode = current.turnLeft();
                if (checkAndAdd(visited, nextNode)) {
                    pq.add(nextNode);
                }
            }

            if (current.canTurnRight()) {
                Node nextNode = current.turnRight();
                if (checkAndAdd(visited, nextNode)) {
                    pq.add(nextNode);
                }
            }
        }

        return shortestPaths;
    }

    private boolean checkAndAdd(long[][][] visited, Node node) {
        if (visited == null || node == null) {
            return false;
        }

        if (visited[node.getRow()][node.getCol()][node.getDirId()] < node.fScore()) {
            return false;
        }

        visited[node.getRow()][node.getCol()][node.getDirId()] = node.fScore();
        return true;
    }

    public long part1() {
        if (shortestPaths == null) {
            shortestPaths = findShortestPaths();
        }

        if (shortestPaths.isEmpty()) {
            return -1;
        }

        return shortestPaths.getFirst().gScore();
    }

    private long countUniquePath(List<Node> shortestPaths) {
        Set<Position> uniquePath = new HashSet<>();
        for (Node node : shortestPaths) {
            while (node != null) {
                uniquePath.add(node.position);
                node = node.parentNode;
            }
        }
        return uniquePath.size();
    }

    public long part2() {
        if (shortestPaths == null) {
            shortestPaths = findShortestPaths();
        }

        return countUniquePath(shortestPaths);
    }

    private static class Position {
        int row;
        int col;

        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public Position(Position other) {
            row = other.row;
            col = other.col;
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
        Node parentNode;
        char direction;
        long gScore;

        public Node(Position position) {
            this.position = position;
            this.parentNode = null;
            this.direction = '>';
            this.gScore = 0;
        }

        public Node(Node parentNode) {
            this.position = new Position(parentNode.position);
            this.parentNode = parentNode;
            this.direction = parentNode.direction;
            this.gScore = parentNode.gScore;
        }

        public int getRow() {
            return position.row;
        }

        public int getCol() {
            return position.col;
        }

        public int getDirId() {
            switch (direction) {
                case '>' -> {
                    return 0;
                }
                case '<' -> {
                    return 1;
                }
                case '^' -> {
                    return 2;
                }
                case 'v' -> {
                    return 3;
                }
                default -> {
                    return -1;
                }
            }
        }

        public Node turnLeft() {
            Node newNode = new Node(this);
            switch (newNode.direction) {
                case '>' -> newNode.direction = '^';
                case '<' -> newNode.direction = 'v';
                case '^' -> newNode.direction = '<';
                case 'v' -> newNode.direction = '>';
            }
            newNode.gScore += 1000;
            return newNode;
        }

        public Node turnRight() {
            Node newNode = new Node(this);
            switch (newNode.direction) {
                case '>' -> newNode.direction = 'v';
                case '<' -> newNode.direction = '^';
                case '^' -> newNode.direction = '>';
                case 'v' -> newNode.direction = '<';
            }
            newNode.gScore += 1000;
            return newNode;
        }

        public Node move() {
            Node newNode = new Node(this);
            switch (newNode.direction) {
                case '>' -> newNode.position.col++;
                case '<' -> newNode.position.col--;
                case '^' -> newNode.position.row--;
                case 'v' -> newNode.position.row++;
            }

            newNode.gScore++;
            return newNode;
        }

        public boolean isEnd() {
            return position.equals(endPosition);
        }

        public boolean canMove() {
            switch (direction) {
                case '>' -> {
                    return isEmptyTile(position.row, position.col + 1);
                }
                case '<' -> {
                    return isEmptyTile(position.row, position.col - 1);
                }
                case '^' -> {
                    return isEmptyTile(position.row - 1, position.col);
                }
                case 'v' -> {
                    return isEmptyTile(position.row + 1, position.col);
                }
                default -> {
                    return false;
                }
            }
        }

        public boolean canTurnLeft() {
            switch (direction) {
                case '>' -> {
                    return isEmptyTile(position.row - 1, position.col);
                }
                case '<' -> {
                    return isEmptyTile(position.row + 1, position.col);
                }
                case '^' -> {
                    return isEmptyTile(position.row, position.col - 1);
                }
                case 'v' -> {
                    return isEmptyTile(position.row, position.col + 1);
                }
                default -> {
                    return false;
                }
            }
        }

        public boolean canTurnRight() {
            switch (direction) {
                case '>' -> {
                    return isEmptyTile(position.row + 1, position.col);
                }
                case '<' -> {
                    return isEmptyTile(position.row - 1, position.col);
                }
                case '^' -> {
                    return isEmptyTile(position.row, position.col + 1);
                }
                case 'v' -> {
                    return isEmptyTile(position.row, position.col - 1);
                }
                default -> {
                    return false;
                }
            }
        }

        public long gScore() {
            return gScore;
        }

        public long hScore() {
            return Math.abs(position.row - endPosition.row) + Math.abs(position.col - endPosition.col);
        }

        public long fScore() {
            return gScore() + hScore();
        }

        public String toString() {
            return "{" + position + ", " + direction + ", " + gScore + "}";
        }
    }
}