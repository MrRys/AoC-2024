import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day14 extends Day {

    final static private String inputFile = "inputs/day14.txt";

    private static final int sizeX = 101;
    private static final int sizeY = 103;
    private List<Robot> initRobots;
    private List<Robot> workRobots;

    public Day14() {
        initRobots = new ArrayList<>();

        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day14 solution = new Day14();
        double time = System.currentTimeMillis();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s");
    }

    void parseInput() {
        initRobots = getInput().stream()
                .map(line -> Arrays.stream(line.replaceAll("[^0-9, -]", "").split("[, ]"))
                        .map(Integer::parseInt).toList())
                .map(Robot::new).toList();
    }

    private long runSimulation(int time) {
        workRobots.forEach(robot -> robot.simulate(time));
        long upLeft = workRobots.stream().filter(Robot::inFirstQuadrant).count();
        long upRight = workRobots.stream().filter(Robot::inSecondQuadrant).count();
        long downLeft = workRobots.stream().filter(Robot::inThirdQuadrant).count();
        long downRight = workRobots.stream().filter(Robot::inFourthQuadrant).count();

        return upLeft * upRight * downLeft * downRight;
    }

    private void resetRobots() {
        workRobots = initRobots.stream().map(Robot::new).toList();
    }

    public long part1() {
        resetRobots();
        return runSimulation(100);
    }

    public long part2() {
        long result = 0;

        long minSafety = Long.MAX_VALUE;
        for (int time = 0; time < sizeX * sizeY; time++) {
            resetRobots();

            long safety = runSimulation(time);
            if (safety < minSafety) {
                minSafety = safety;
                result = time;
            }
        }

        return result;
    }

    private static class Robot {
        int posX;
        int posY;
        int velX;
        int velY;

        public Robot(List<Integer> parameters) {
            if (parameters.size() != 4) {
                throw new IllegalArgumentException();
            }

            posX = parameters.get(0);
            posY = parameters.get(1);
            velX = parameters.get(2);
            velY = parameters.get(3);
        }

        public Robot(Robot other) {
            posX = other.posX;
            posY = other.posY;
            velX = other.velX;
            velY = other.velY;
        }

        public void simulate(int time) {
            posX = (posX + velX * time) % sizeX;
            posY = (posY + velY * time) % sizeY;
            posX = posX >= 0 ? posX : posX + sizeX;
            posY = posY >= 0 ? posY : posY + sizeY;
        }

        public boolean inFirstQuadrant() {
            return posX < sizeX / 2 && posY < sizeY / 2;
        }

        public boolean inSecondQuadrant() {
            return posX > sizeX / 2 && posY < sizeY / 2;
        }

        public boolean inThirdQuadrant() {
            return posX < sizeX / 2 && posY > sizeY / 2;
        }

        public boolean inFourthQuadrant() {
            return posX > sizeX / 2 && posY > sizeY / 2;
        }

        public String toString() {
            return "(" + posX + ", " + posY + ", " + velX + ", " + velY + ")";
        }

    }

}