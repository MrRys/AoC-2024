import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class Day05 extends Day {

    final static private String inputFile = "inputs/day05.txt";

    private final Map<Integer, Set<Integer>> beforeMap;
    private final Map<Integer, Set<Integer>> afterMap;
    private final List<List<Integer>> updates;

    public Day05() {
        beforeMap = new HashMap<>();
        afterMap = new HashMap<>();
        updates = new ArrayList<>();

        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day05 solution = new Day05();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
    }

    void parseInput() {
        List<String> input = this.getInput();
        boolean parseRules = true;

        for (String line : input) {
            if (line.isEmpty()) {
                parseRules = false;
                continue;
            }

            if (parseRules) {
                Integer[] parts = Stream.of(line.split("\\|")).map(Integer::valueOf).toArray(Integer[]::new);

                if (beforeMap.containsKey(parts[0])) {
                    beforeMap.get(parts[0]).add(parts[1]);
                } else {
                    Set<Integer> set = new HashSet<>();
                    set.add(parts[1]);
                    beforeMap.put(parts[0], set);
                }

                if (afterMap.containsKey(parts[1])) {
                    afterMap.get(parts[1]).add(parts[0]);
                } else {
                    Set<Integer> set = new HashSet<>();
                    set.add(parts[0]);
                    afterMap.put(parts[1], set);
                }
            } else {
                List<Integer> update = Stream.of(line.split(",")).map(Integer::valueOf).toList();
                updates.add(update);
            }
        }
    }

    private boolean isCorrectOrder(List<Integer> update) {
        Set<Integer> seen = new HashSet<>();
        for (Integer page : update) {
            /*
             * This assumes that all pages have fully defined order in the rule set.
             * Note: They do in the AoC provided inputs.
             * */
            if ((beforeMap.containsKey(page) && !Collections.disjoint(beforeMap.get(page), seen)) ||
                    (afterMap.containsKey(page) && !afterMap.get(page).containsAll(seen))) {
                return false;
            }
            seen.add(page);
        }
        return true;
    }

    public long part1() {
        long result = 0;

        for (List<Integer> update : updates) {
            if (isCorrectOrder(update)) {
                result += update.get(update.size() / 2);
            }
        }
        return result;
    }

    public long part2() {
        long result = 0;

        for (List<Integer> update : updates) {
            if (!isCorrectOrder(update)) {
                List<Integer> updateCopy = new ArrayList<>(update);

                /*
                 * This assumes that all pages have fully defined order in the rule set.
                 * Note: They do in the AoC provided inputs.
                 * */
                updateCopy.sort((a, b) -> {
                    if (beforeMap.containsKey(a) && beforeMap.get(a).contains(b)) {
                        return -1;
                    }
                    if (afterMap.containsKey(a) && afterMap.get(a).contains(b)) {
                        return 1;
                    }
                    return 0;
                });
                result += updateCopy.get(updateCopy.size() / 2);
            }
        }
        return result;
    }
}