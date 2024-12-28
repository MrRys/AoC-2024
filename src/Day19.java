import java.io.IOException;
import java.util.*;

public class Day19 extends Day {

    final static private String inputFile = "inputs/day19.txt";
    private final Map<String, Long> seenDesigns;
    private List<String> designs;
    private TowelTrieNode towelTrieRoot;

    public Day19() {
        seenDesigns = new HashMap<>();

        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day19 solution = new Day19();
        double time = System.currentTimeMillis();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s");
    }

    void parseInput() {
        List<String> input = getInput();
        List<String> towels = Arrays.stream(input.getFirst().split(", ")).toList();
        towelTrieRoot = TowelTrieNode.generateTowelTrie(towels);
        designs = input.subList(2, input.size());
    }

    private long checkPossibleDesign(String design) {
        if (design.isEmpty()) {
            return 1;
        }

        if (seenDesigns.containsKey(design)) {
            return seenDesigns.get(design);
        }

        long possibleDesignCount = 0;
        List<Integer> possibleTowelLengths = towelTrieRoot.findAllPossibleTowels(design);
        for (Integer towelLength : possibleTowelLengths) {
            String newDesign = design.substring(towelLength);
            possibleDesignCount += checkPossibleDesign(newDesign);
        }

        seenDesigns.put(design, possibleDesignCount);
        return possibleDesignCount;
    }

    public long part1() {
        long result = 0;
        for (String design : designs) {
            result += checkPossibleDesign(design) > 0 ? 1 : 0;
        }
        return result;
    }

    public long part2() {
        long result = 0;
        for (String design : designs) {
            result += checkPossibleDesign(design);
        }
        return result;
    }

    private static class TowelTrieNode {
        private static final Map<String, List<Integer>> possibleTowelLengths = new HashMap<>();

        private final int colorsCount = 5;
        private final TowelTrieNode[] colors;
        private final int depth;
        private boolean isEnd;

        public TowelTrieNode() {
            this.colors = new TowelTrieNode[colorsCount];
            this.depth = 0;
            this.isEnd = false;
        }

        public TowelTrieNode(int depth) {
            this.colors = new TowelTrieNode[colorsCount];
            this.depth = depth;
            this.isEnd = false;
        }

        public static TowelTrieNode generateTowelTrie(List<String> towels) {
            TowelTrieNode root = new TowelTrieNode();
            for (String towel : towels) {
                TowelTrieNode current = root;
                for (char color : towel.toCharArray()) {
                    current = current.addColor(color);
                }
                current.setIsEnd();
            }
            return root;
        }

        public TowelTrieNode addColor(char color) {
            int colorCode = getColorCode(color);
            if (colors[colorCode] == null) {
                colors[colorCode] = new TowelTrieNode(depth + 1);
            }
            return colors[colorCode];
        }

        public List<Integer> findAllPossibleTowels(String design) {
            if (possibleTowelLengths.containsKey(design)) {
                return possibleTowelLengths.get(design);
            }

            List<Integer> towelLengths = new ArrayList<>();
            TowelTrieNode current = this;
            for (char color : design.toCharArray()) {
                current = current.colors[getColorCode(color)];
                if (current == null) {
                    break;
                }
                if (current.isEnd()) {
                    towelLengths.add(current.depth);
                }
            }

            possibleTowelLengths.put(design, towelLengths);
            return towelLengths;
        }

        private boolean isEnd() {
            return isEnd;
        }

        private void setIsEnd() {
            this.isEnd = true;
        }

        private int getColorCode(char color) {
            int colorCode = -1;
            switch (color) {
                case 'w' -> colorCode = 0;
                case 'u' -> colorCode = 1;
                case 'b' -> colorCode = 2;
                case 'r' -> colorCode = 3;
                case 'g' -> colorCode = 4;
            }
            return colorCode;
        }
    }
}