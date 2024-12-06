import java.io.IOException;
import java.util.List;

public class Day04 extends Day {

    final static private String inputFile = "inputs/day04.txt";

    private List<String> grid;

    public Day04() {
        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day04 solution = new Day04();
        double time = System.currentTimeMillis();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s");
    }

    void parseInput() {
        grid = getInput();
    }

    private boolean isXmasHorizontal(int row, int col) {
        if (grid.get(row).length() - 4 < col) {
            return false;
        }

        String horizontal = grid.get(row);
        return horizontal.startsWith("XMAS", col) || horizontal.startsWith("SAMX", col);
    }

    private boolean isXmasVertical(int row, int col) {
        if (grid.size() - 4 < row) {
            return false;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int r = row; r < row + 4; r++) {
            stringBuilder.append(grid.get(r).charAt(col));
        }

        String vertical = stringBuilder.toString();
        return vertical.equals("XMAS") || vertical.equals("SAMX");
    }

    private boolean isXmasPosDiagonal(int row, int col) {
        if (grid.size() - 4 < row || grid.get(row).length() - 4 < col) {
            return false;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int r = row, c = col; r < row + 4; r++, c++) {
            stringBuilder.append(grid.get(r).charAt(c));
        }

        String posDiagonal = stringBuilder.toString();
        return posDiagonal.equals("XMAS") || posDiagonal.equals("SAMX");
    }

    private boolean isXmasNegDiagonal(int row, int col) {
        if (grid.size() - 4 < row || grid.get(row).length() - 4 < col) {
            return false;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int r = row, c = col + 3; r < row + 4; r++, c--) {
            stringBuilder.append(grid.get(r).charAt(c));
        }

        String negDiagonal = stringBuilder.toString();
        return negDiagonal.equals("XMAS") || negDiagonal.equals("SAMX");
    }

    private boolean isX_mas(int row, int col) {
        if (grid.size() - 3 < row || grid.get(row).length() - 3 < col) {
            return false;
        }

        StringBuilder posBuilder = new StringBuilder();
        for (int r = row, c = col; r < row + 3; r++, c++) {
            posBuilder.append(grid.get(r).charAt(c));
        }

        StringBuilder negBuilder = new StringBuilder();
        for (int r = row, c = col + 2; r < row + 3; r++, c--) {
            negBuilder.append(grid.get(r).charAt(c));
        }

        String posDiagonal = posBuilder.toString();
        String negDiagonal = negBuilder.toString();
        return (posDiagonal.equals("MAS") || posDiagonal.equals("SAM")) && (negDiagonal.equals("MAS") || negDiagonal.equals("SAM"));
    }

    private long countXmas(int row, int col) {
        return (isXmasHorizontal(row, col) ? 1 : 0) + (isXmasVertical(row, col) ? 1 : 0) + (isXmasPosDiagonal(row, col) ? 1 : 0) + (isXmasNegDiagonal(row, col) ? 1 : 0);
    }

    public long part1() {
        long result = 0;

        int rows = grid.size();
        int cols = grid.getFirst().length();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                result += countXmas(row, col);
            }
        }
        return result;
    }

    public long part2() {
        long result = 0;

        int rows = grid.size();
        int cols = grid.getFirst().length();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (isX_mas(row, col)) {
                    result++;
                }
            }
        }
        return result;
    }
}