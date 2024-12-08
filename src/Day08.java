import java.io.IOException;
import java.util.*;

public class Day08 extends Day {

    final static private String inputFile = "inputs/day08.txt";
    private final Map<Character, List<Location>> antennas;
    private int gridRows;
    private int gridCols;

    public Day08() {
        antennas = new HashMap<>();

        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day08 solution = new Day08();
        double time = System.currentTimeMillis();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s");
    }

    void parseInput() {
        List<String> input = getInput();

        gridRows = input.size();
        gridCols = input.getFirst().length();
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                char tile = input.get(row).charAt(col);
                if (tile != '.') {
                    if (!antennas.containsKey(tile)) {
                        antennas.put(tile, new ArrayList<>());
                    }
                    antennas.get(tile).add(new Location(row, col));
                }
            }
        }
    }

    private boolean addAntinode(Set<Location> antinodes, Location antinode) {
        if (antinode.row < 0 || antinode.row >= gridRows || antinode.col < 0 || antinode.col >= gridCols) {
            return false;
        }
        antinodes.add(antinode);
        return true;
    }

    private void generateAntinodes(Set<Location> antinodes, List<Location> antennaLocations) {
        for (int i = 0; i < antennaLocations.size() - 1; i++) {
            for (int j = i + 1; j < antennaLocations.size(); j++) {
                Location antennaLoc1 = antennaLocations.get(i);
                Location antennaLoc2 = antennaLocations.get(j);
                Location difference = antennaLoc1.sub(antennaLoc2);

                addAntinode(antinodes, antennaLoc1.add(difference));
                addAntinode(antinodes, antennaLoc2.sub(difference));
            }
        }
    }

    private void generateAntinodesOnRepeat(Set<Location> antinodes, List<Location> antennaLocations) {
        for (int i = 0; i < antennaLocations.size() - 1; i++) {
            for (int j = i + 1; j < antennaLocations.size(); j++) {
                Location antennaLoc1 = antennaLocations.get(i);
                Location antennaLoc2 = antennaLocations.get(j);
                Location difference = antennaLoc1.sub(antennaLoc2);

                Location antinodeLocation = antennaLoc1.sub(difference);
                while (addAntinode(antinodes, antinodeLocation)) {
                    antinodeLocation = antinodeLocation.sub(difference);
                }

                antinodeLocation = antennaLoc2.add(difference);
                while (addAntinode(antinodes, antinodeLocation)) {
                    antinodeLocation = antinodeLocation.add(difference);
                }
            }
        }
    }

    public long part1() {
        Set<Location> antinodes = new HashSet<>();

        for (List<Location> antennaLocations : antennas.values()) {
            generateAntinodes(antinodes, antennaLocations);
        }

        return antinodes.size();
    }

    public long part2() {
        Set<Location> antinodes = new HashSet<>();

        for (List<Location> antennaLocations : antennas.values()) {
            generateAntinodesOnRepeat(antinodes, antennaLocations);
        }

        return antinodes.size();
    }

    private static class Location {
        int row;
        int col;

        Location(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            return "(" + row + ", " + col + ")";
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

            if (!(o instanceof Location other)) {
                return false;
            }

            return this.row == other.row && this.col == other.col;
        }

        public Location add(Location loc2) {
            return new Location(row + loc2.row, col + loc2.col);
        }

        public Location sub(Location loc2) {
            return new Location(row - loc2.row, col - loc2.col);
        }

    }
}