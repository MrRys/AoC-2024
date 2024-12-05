import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Day {

    private List<String> input = null;

    List<String> getInput() {
        return input;
    }

    void readFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        input = new ArrayList<>();
        while (reader.ready()) {
            input.add(reader.readLine());
        }
        reader.close();
    }

    abstract void parseInput();

    abstract public long part1();

    abstract public long part2();
}
