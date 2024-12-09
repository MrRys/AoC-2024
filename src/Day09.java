import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day09 extends Day {

    final static private String inputFile = "inputs/day09.txt";

    private final List<Block> blocks;

    public Day09() {
        blocks = new ArrayList<>();

        try {
            readFile(inputFile);
            parseInput();
        } catch (IOException e) {
            System.out.println("Error: Input file not found.");
        }
    }

    public static void main(String[] args) {
        Day09 solution = new Day09();
        double time = System.currentTimeMillis();
        System.out.println("Part 1: " + solution.part1());
        System.out.println("Part 2: " + solution.part2());
        System.out.println("Time: " + (System.currentTimeMillis() - time) / 1000 + "s");
    }

    void parseInput() {
        String input = getInput().getFirst();
        for (int i = 0; i < input.length(); i++) {
            int blockCount = Integer.parseInt(input.substring(i, i + 1));
            if (i % 2 == 0) {
                blocks.add(new Block(i / 2, blockCount));
            } else {
                blocks.add(new Block(-1, blockCount));
            }
        }
    }

    private long checkSum(List<Block> localBlocks) {
        long sum = 0;
        int idx = 0;
        for (Block currBlock : localBlocks) {
            for (int i = 0; i < currBlock.size; i++, idx++) {
                if (currBlock.id > 0) {
                    sum += (long) idx * currBlock.id;
                }
            }
        }
        return sum;
    }

    public long part1() {
        List<Block> localBlocks = blocks.stream().map(Block::new).collect(Collectors.toList());
        int offset = localBlocks.size() % 2 == 1 ? 1 : 2;

        for (int freeIdx = 1, fullIdx = localBlocks.size() - offset; freeIdx <= fullIdx; ) {
            Block fullBlock = localBlocks.get(fullIdx);
            Block freeBlock = localBlocks.get(freeIdx);

            if (freeBlock.size > fullBlock.size) {
                int diff = freeBlock.size - fullBlock.size;
                freeBlock.size = fullBlock.size;
                freeBlock.id = fullBlock.id;
                fullBlock.id = -1;
                localBlocks.add(freeIdx + 1, new Block(-1, diff));
                freeIdx++;
                fullIdx--;
            } else if (freeBlock.size < fullBlock.size) {
                int diff = fullBlock.size - freeBlock.size;
                freeBlock.id = fullBlock.id;
                fullBlock.size = diff;
                freeIdx += 2;
            } else {
                freeBlock.id = fullBlock.id;
                fullBlock.id = -1;
                freeIdx += 2;
                fullIdx -= 2;
            }
        }

        return checkSum(localBlocks);
    }

    public long part2() {
        List<Block> localBlocks = blocks.stream().map(Block::new).collect(Collectors.toList());
        int offset = localBlocks.size() % 2 == 1 ? 1 : 2;

        for (int fullIdx = localBlocks.size() - offset; fullIdx >= 0; fullIdx--) {
            Block fullBlock = localBlocks.get(fullIdx);

            if (fullBlock.id < 0) {
                continue;
            }

            for (int freeIdx = 1; freeIdx <= fullIdx; freeIdx++) {
                Block freeBlock = localBlocks.get(freeIdx);

                if (freeBlock.id > 0) {
                    continue;
                }

                if (freeBlock.size > fullBlock.size) {
                    int diff = freeBlock.size - fullBlock.size;
                    freeBlock.size = fullBlock.size;
                    freeBlock.id = fullBlock.id;
                    fullBlock.id = -1;
                    localBlocks.add(freeIdx + 1, new Block(-1, diff));
                    break;
                }

                if (freeBlock.size == fullBlock.size) {
                    freeBlock.id = fullBlock.id;
                    fullBlock.id = -1;
                    break;
                }
            }
        }

        return checkSum(localBlocks);
    }

    private static class Block {
        int id;
        int size;

        public Block(Block block) {
            this.id = block.id;
            this.size = block.size;
        }

        public Block(int id, int size) {
            this.id = id;
            this.size = size;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            String c = id < 0 ? "." : String.valueOf(id);
            builder.append(size > 0 ? c.repeat(size) : "|");
            return builder.toString();
        }
    }
}