import utils.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.charset.StandardCharsets;

public class ReviewData {
    private List<Pair<String,String>> reviews = new ArrayList();

    public ReviewData(String path) {
        this.reviews.addAll(getRawData(path + "//pos"));
        this.reviews.addAll(getRawData(path + "//neg"));
    }

    private List<Pair<String,String>> getRawData(String path) {
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

    private Pair<String,String> readTrainingFile(Path path)
    {
        try{
            Boolean pos = path.toString().contains("pos");
            return new Pair<String, String>((pos == true) ? "positive" : "negative", Utils.readFile(path.toString(),StandardCharsets.UTF_8));
        }catch(IOException ex){
            return new Pair<String, String>("","");
        }
    }

    public List<Pair<String,String>> getReviews(){
        return this.reviews;
    }
}
