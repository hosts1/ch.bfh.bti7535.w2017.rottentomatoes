import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrainingData {
    public List<String> pos = Collections.emptyList();
    public List<String> neg = Collections.emptyList();

    public TrainingData(String path) {
        this.pos = getRawData(path + "//pos");
        this.neg = getRawData(path + "//neg");
    }

    public List<String> getRawData(String path) {
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(this::readTrainingFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private String readTrainingFile(Path path)
    {
        String fileName = path.getFileName().toString();
        System.out.println(fileName);
        // Todo: read training file
        return new String(fileName);
    }
}
