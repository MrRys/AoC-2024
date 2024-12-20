import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 extends Day {

    final static private String inputFile = "inputs/day13.txt";

    private final List<ClawMachine> machines;

    public Day13() {
        machines = new ArrayList<>();

        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day13 solution = new Day13();
        double time = System.currentTimeMillis();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s");
    }

    void parseInput() {
        List<String> input = getInput().stream().filter(line -> !line.isEmpty()).toList();
        Pattern pattern = Pattern.compile("^.*?X[\\+=]([0-9]+), Y[\\+=]([0-9]+)$");

        for (int i = 0; i < input.size(); i += 3) {
            Pair buttonA = null, buttonB = null, prize = null;

            Matcher matcher = pattern.matcher(input.get(i));
            if (matcher.find()) {
                buttonA = new Pair(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            }

            matcher = pattern.matcher(input.get(i + 1));
            if (matcher.find()) {
                buttonB = new Pair(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            }

            matcher = pattern.matcher(input.get(i + 2));
            if (matcher.find()) {
                prize = new Pair(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            }

            machines.add(new ClawMachine(buttonA, buttonB, prize));
        }
    }

    public long part1() {
        long result = 0;
        for (ClawMachine machine : machines) {
            result += machine.getPrice(0);
        }
        return result;
    }

    public long part2() {
        long result = 0;
        for (ClawMachine machine : machines) {
            result += machine.getPrice(10000000000000L);
        }
        return result;
    }

    private static class Pair {
        double xAxis;
        double yAxis;

        public Pair(double xAxis, double yAxis) {
            this.xAxis = xAxis;
            this.yAxis = yAxis;
        }

        public Pair add(Pair other) {
            return new Pair(xAxis + other.xAxis, yAxis + other.yAxis);
        }

        public String toString() {
            return "(" + xAxis + ", " + yAxis + ")";
        }
    }

    private record ClawMachine(Pair buttonA, Pair buttonB, Pair prize) {

        public long getPrice(double prizeOffset) {
            Pair newPrize = prize.add(new Pair(prizeOffset, prizeOffset));

            double buttonAPresses = (newPrize.xAxis * buttonB.yAxis - newPrize.yAxis * buttonB.xAxis) /
                    (buttonA.xAxis * buttonB.yAxis - buttonA.yAxis * buttonB.xAxis);
            double buttonBPresses = (newPrize.xAxis - buttonA.xAxis * buttonAPresses) /
                    buttonB.xAxis;
            return buttonAPresses % 1 == 0 && buttonBPresses % 1 == 0 ? (long) (buttonAPresses * 3 + buttonBPresses) : 0;
        }

        public String toString() {
            return "{Button A=" + buttonA + ", Button B=" + buttonB + ", Prize at " + prize + "}";
        }

    }
}