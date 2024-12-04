import java.io.IOException;
import java.util.List;

public class Day04 extends Day {

    final static private String inputFile = "inputs/day04.txt";

    private List<String> grid;

    public Day04() {
        try {
            this.readFile(inputFile);
            this.parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day04 solution = new Day04();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
    }

    void parseInput() {
        this.grid = this.getInput();
    }

    private long isXmasHorizontal(int row, int col) {
        if (this.grid.get(row).length() - 4 < col) {
            return 0;
        }

        String horizontal = this.grid.get(row);
        return horizontal.startsWith("XMAS", col) || horizontal.startsWith("SAMX", col) ? 1 : 0;
    }

    private long isXmasVertical(int row, int col) {
        if (this.grid.size() - 4 < row) {
            return 0;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = row; i < row + 4; i++) {
            stringBuilder.append(this.grid.get(i).charAt(col));
        }

        String vertical = stringBuilder.toString();
        return vertical.equals("XMAS") || vertical.equals("SAMX") ? 1 : 0;
    }

    private long isXmasPosDiagonal(int row, int col) {
        if (this.grid.size() - 4 < row || this.grid.get(row).length() - 4 < col) {
            return 0;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int r = row, c = col; r < row + 4; r++, c++) {
            stringBuilder.append(this.grid.get(r).charAt(c));
        }

        String diagonal = stringBuilder.toString();
        return diagonal.equals("XMAS") || diagonal.equals("SAMX") ? 1 : 0;
    }

    private long isXmasNegDiagonal(int row, int col) {
        if (this.grid.size() - 4 < row || this.grid.get(row).length() - 4 < col) {
            return 0;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int r = row, c = col + 3; r < row + 4; r++, c--) {
            stringBuilder.append(this.grid.get(r).charAt(c));
        }

        String diagonal = stringBuilder.toString();
        return diagonal.equals("XMAS") || diagonal.equals("SAMX") ? 1 : 0;
    }

    private long countXmas(int row, int col) {
        return isXmasHorizontal(row, col) + isXmasVertical(row, col) + isXmasPosDiagonal(row, col) + isXmasNegDiagonal(row, col);
    }

    public long part1() {
        long result = 0;

        int rows = this.grid.size();
        int cols = this.grid.getFirst().length();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                result += countXmas(row, col);
            }
        }
        return result;
    }

    private boolean isX_mas(int row, int col) {
        if (this.grid.size() - 3 < row || this.grid.get(row).length() - 3 < col) {
            return false;
        }

        StringBuilder posBuilder = new StringBuilder();
        for (int r = row, c = col; r < row + 3; r++, c++) {
            posBuilder.append(this.grid.get(r).charAt(c));
        }
        StringBuilder negBuilder = new StringBuilder();
        for (int r = row, c = col + 2; r < row + 3; r++, c--) {
            negBuilder.append(this.grid.get(r).charAt(c));
        }
        String pos = posBuilder.toString();
        String neg = negBuilder.toString();

        return (pos.equals("MAS") || pos.equals("SAM")) && (neg.equals("MAS") || neg.equals("SAM"));
    }

    public long part2() {
        long result = 0;

        int rows = this.grid.size();
        int cols = this.grid.getFirst().length();

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