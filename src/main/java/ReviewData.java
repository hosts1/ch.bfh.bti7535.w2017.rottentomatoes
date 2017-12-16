import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.charset.StandardCharsets;

public class ReviewData {
    private List<String> pos = Collections.emptyList();
    private List<String> neg = Collections.emptyList();

    public ReviewData(String path) {
        this.pos = getRawData(path + "//pos");
        this.neg = getRawData(path + "//neg");
    }

    private List<String> getRawData(String path) {
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
        try{
            return Utils.readFile(path.toString(),StandardCharsets.UTF_8);
        }catch(IOException ex){
            return "";
        }
    }

    public List<String> getPositiveData(){
        return this.pos;
    }

    public List<String> getNegativeData(){
        return this.neg;
    }
}
