import java.io.IOException;
import java.util.*;

public class Day06 extends Day {

    final static private String inputFile = "inputs/day06.txt";

    final private char EMPTY = '.';
    final private char BLOCKED = '#';
    final private char WALKED = 'X';

    final private char UP = '^';
    final private char RIGHT = '>';
    final private char DOWN = 'v';
    final private char LEFT = '<';

    private char[][] initGrid;
    private int[] initGuardPos;

    private char[][] workGrid;
    private int[] guardPos;
    private char guardDir;
    private Map<Integer, Set<Character>> dirGrid;
    private Set<Integer> walkedPath;

    public Day06() {
        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day06 solution = new Day06();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
    }

    void parseInput() {
        initGrid = getInput().stream().map(String::toCharArray).toArray(char[][]::new);
        initGuardPos = new int[2];
        for (int row = 0; row < initGrid.length; row++) {
            for (int col = 0; col < initGrid[0].length; col++) {
                if (initGrid[row][col] != EMPTY && initGrid[row][col] != BLOCKED) {
                    initGuardPos[0] = row;
                    initGuardPos[1] = col;
                    break;
                }
            }
        }
        walkedPath = new HashSet<>();
    }

    private void resetGrid() {
        workGrid = Arrays.stream(initGrid).map(char[]::clone).toArray(char[][]::new);
        guardPos = initGuardPos.clone();
        guardDir = initGrid[initGuardPos[0]][initGuardPos[1]];
        if (dirGrid == null) {
            dirGrid = new HashMap<>();
        } else {
            dirGrid.clear();
        }
    }

    private MoveDirection getMoveDirection() {
        switch (guardDir) {
            case UP -> {
                return new MoveDirection(-1, 0);
            }
            case RIGHT -> {
                return new MoveDirection(0, 1);
            }
            case DOWN -> {
                return new MoveDirection(1, 0);
            }
            case LEFT -> {
                return new MoveDirection(0, -1);
            }
            default -> {
                return new MoveDirection(0, 0);
            }
        }
    }

    private void changeGuardDirection() {
        switch (guardDir) {
            case UP -> guardDir = RIGHT;
            case RIGHT -> guardDir = DOWN;
            case DOWN -> guardDir = LEFT;
            case LEFT -> guardDir = UP;
            default -> {
            }
        }
    }

    private boolean isNewDirection() {
        int pos = guardPos[0] * workGrid[0].length + guardPos[1];

        if (dirGrid.containsKey(pos) && dirGrid.get(pos).contains(guardDir)) {
            return false;
        }

        if (!dirGrid.containsKey(pos)) {
            dirGrid.put(pos, new HashSet<>());
        }

        dirGrid.get(pos).add(guardDir);
        return true;
    }

    private boolean canMove() {
        MoveDirection mDir = getMoveDirection();
        int newRow = guardPos[0] + mDir.dRow;
        int newCol = guardPos[1] + mDir.dCol;
        return newRow >= 0 && newRow < workGrid.length && newCol >= 0 && newCol < workGrid[newRow].length && workGrid[newRow][newCol] != BLOCKED;
    }

    private boolean isInGrid() {
        int row = guardPos[0];
        int col = guardPos[1];
        return row != 0 && col != 0 && row != workGrid.length - 1 && col != workGrid[0].length - 1;
    }

    private void moveGuard() {
        MoveDirection mDir = getMoveDirection();
        guardPos[0] += mDir.dRow;
        guardPos[1] += mDir.dCol;
    }

    private void setWalkedTile() {
        workGrid[guardPos[0]][guardPos[1]] = WALKED;
        walkedPath.add(guardPos[0] * initGrid.length + guardPos[1]);
    }

    public long part1() {
        resetGrid();
        setWalkedTile();

        while (isInGrid()) {
            if (canMove()) {
                moveGuard();
                setWalkedTile();
                continue;
            }

            changeGuardDirection();
        }

        return walkedPath.size();
    }

    public long part2() {
        resetGrid();

        if (walkedPath.isEmpty()) {
            part1();
        }

        long result = 0;

        walkedPath.remove(guardPos[0] * initGrid[0].length + guardPos[1]);
        for (Integer position : walkedPath) {
            int row = position / workGrid.length;
            int col = position % workGrid.length;

            workGrid[row][col] = BLOCKED;

            while (isInGrid()) {
                if (canMove()) {
                    moveGuard();
                    continue;
                }

                changeGuardDirection();
                if (!isNewDirection()) {
                    result++;
                    break;
                }
            }
            resetGrid();
        }

        return result;
    }

    private static class MoveDirection {
        int dRow;
        int dCol;

        MoveDirection(int dRow, int dCol) {
            this.dRow = dRow;
            this.dCol = dCol;
        }
    }
}