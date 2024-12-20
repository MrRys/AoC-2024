import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day14 extends Day {

    final static private String inputFile = "inputs/day14.txt";

    private static final int gridSizeX = 101;
    private static final int gridSizeY = 103;
    private List<Robot> initRobots;

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
        List<Robot> robots = getRobots();

        robots.forEach(robot -> robot.simulate(time));
        long firstQuadrant = robots.stream().filter(Robot::inFirstQuadrant).count();
        long secondQuadrant = robots.stream().filter(Robot::inSecondQuadrant).count();
        long thirdQuadrant = robots.stream().filter(Robot::inThirdQuadrant).count();
        long fourthQuadrant = robots.stream().filter(Robot::inFourthQuadrant).count();

        return firstQuadrant * secondQuadrant * thirdQuadrant * fourthQuadrant;
    }

    private List<Robot> getRobots() {
        return initRobots.stream().map(Robot::new).toList();
    }

    public long part1() {
        return runSimulation(100);
    }

    public long part2() {
        long result = 0;

        long minSafety = Long.MAX_VALUE;
        for (int time = 0; time < gridSizeX * gridSizeY; time++) {
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
            if (parameters == null || parameters.size() != 4) {
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
            posX = (posX + velX * time) % gridSizeX;
            posY = (posY + velY * time) % gridSizeY;
            posX = posX >= 0 ? posX : posX + gridSizeX;
            posY = posY >= 0 ? posY : posY + gridSizeY;
        }

        public boolean inFirstQuadrant() {
            return posX < gridSizeX / 2 && posY < gridSizeY / 2;
        }

        public boolean inSecondQuadrant() {
            return posX > gridSizeX / 2 && posY < gridSizeY / 2;
        }

        public boolean inThirdQuadrant() {
            return posX < gridSizeX / 2 && posY > gridSizeY / 2;
        }

        public boolean inFourthQuadrant() {
            return posX > gridSizeX / 2 && posY > gridSizeY / 2;
        }

        public String toString() {
            return "(" + posX + ", " + posY + ", " + velX + ", " + velY + ")";
        }

    }

}