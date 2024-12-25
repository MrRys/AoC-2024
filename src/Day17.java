import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day17 extends Day {

    final static private String inputFile = "inputs/day17.txt";
    private static List<Integer> initProgram;
    private long initRegisterA;
    private long initRegisterB;
    private long initRegisterC;

    public Day17() {
        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day17 solution = new Day17();
        double time = System.currentTimeMillis();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s");
    }

    void parseInput() {
        List<String> input = getInput();
        initRegisterA = Long.parseLong(input.get(0).split(": ")[1]);
        initRegisterB = Long.parseLong(input.get(1).split(": ")[1]);
        initRegisterC = Long.parseLong(input.get(2).split(": ")[1]);
        initProgram = Arrays.stream(input.get(4).split(": ")[1].split(",")).map(Integer::parseInt).toList();
    }

    public long part1() {
        Computer computer = new Computer(initRegisterA, initRegisterB, initRegisterC, initProgram);
        computer.runProgram();
        computer.printOutput();
        return 0;
    }


    public long part2() {
        return Computer.findQuineProgram(initProgram.size() - 1, 0);
    }

    private static class Computer {
        private final List<Integer> program;
        private final List<Integer> output;
        private long registerA;
        private long registerB;
        private long registerC;
        private int programCounter;

        public Computer(long registerA, long registerB, long registerC, List<Integer> program) {
            this.registerA = registerA;
            this.registerB = registerB;
            this.registerC = registerC;
            this.program = program;
            this.programCounter = 0;
            this.output = new ArrayList<>();
        }

        public static long findQuineProgram(int target, long initialValue) {
            if (target < 0) {
                return initialValue;
            }

            for (int possibleRemainder = 0; possibleRemainder < 8; possibleRemainder++) {
                Computer computer = new Computer(initialValue << 3 | possibleRemainder, 0, 0, initProgram);

                long result = computer.runSingleIteration(target);
                if (result >= 0) {
                    return result;
                }
            }

            return -1;
        }

        public void runProgram() {
            while (programCounter < program.size()) {
                int opcode = program.get(programCounter);
                int operand = program.get(programCounter + 1);
                executeInstruction(opcode, operand);
            }
        }

        public void printOutput() {
            System.out.print(output.getFirst());
            for (int i = 1; i < output.size(); i++) {
                System.out.print("," + output.get(i));
            }
            System.out.println();
        }

        private long runSingleIteration(int target) {
            for (int programCounter = 0; programCounter < program.size() - 2; programCounter += 2) {
                int opcode = program.get(programCounter);
                int operand = program.get(programCounter + 1);

                if (opcode == 0) {
                    continue;
                }

                executeInstruction(opcode, operand);

                if (opcode == 5 && output.getLast().intValue() == program.get(target).intValue()) {
                    long result = findQuineProgram(target - 1, registerA);
                    if (result > 0) {
                        return result;
                    }
                }
            }

            return -1;
        }

        private void executeInstruction(int opcode, int operand) {
            switch (opcode) {
                case 0 -> adv(operand);
                case 1 -> bxl(operand);
                case 2 -> bst(operand);
                case 3 -> jnz(operand);
                case 4 -> bxc();
                case 5 -> out(operand);
                case 6 -> bdv(operand);
                case 7 -> cdv(operand);
                default -> throw new IllegalStateException("Unexpected opcode: " + opcode);
            }
        }

        private long getComboOperand(int operand) {
            return switch (operand) {
                case 0, 1, 2, 3 -> operand;
                case 4 -> registerA;
                case 5 -> registerB;
                case 6 -> registerC;
                default -> throw new IllegalStateException("Unexpected operand: " + operand);
            };
        }

        private void adv(int operand) {
            long numerator = registerA;
            long denominator = getComboOperand(operand);
            registerA = numerator >> denominator;
            programCounter += 2;
        }

        private void bxl(int operand) {
            registerB = registerB ^ (long) operand;
            programCounter += 2;
        }

        private void bst(int operand) {
            registerB = getComboOperand(operand) & 7;
            programCounter += 2;
        }

        private void jnz(int operand) {
            if (registerA == 0) {
                programCounter += 2;
                return;
            }
            programCounter = operand;
        }

        private void bxc() {
            registerB = registerB ^ registerC;
            programCounter += 2;
        }

        private void out(int operand) {
            output.add((int) (getComboOperand(operand) & 7));
            programCounter += 2;
        }

        private void bdv(int operand) {
            long numerator = registerA;
            long denominator = getComboOperand(operand);
            registerB = numerator >> denominator;
            programCounter += 2;
        }

        private void cdv(int operand) {
            long numerator = registerA;
            long denominator = getComboOperand(operand);
            registerC = numerator >> denominator;
            programCounter += 2;
        }

    }
}