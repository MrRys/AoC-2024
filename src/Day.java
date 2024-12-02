import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Day {

    List<String> input = null;

    void readFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        this.input = new ArrayList<>();
        while(reader.ready()) {
            input.add(reader.readLine());
        }
        reader.close();
    }

    abstract void parseInput();
    abstract long part1();
    abstract long part2();
}
