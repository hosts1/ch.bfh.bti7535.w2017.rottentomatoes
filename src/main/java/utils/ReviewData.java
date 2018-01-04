package utils;

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
    private List<Pair<String,String>> positiveReviews = new ArrayList();
    private List<Pair<String,String>> negativeReviews = new ArrayList();

    private int folds = 0;
    private int foldSize = 0;

    public ReviewData(String path, int folds) {
        this.positiveReviews.addAll(getRawData(path + "//pos"));
        this.negativeReviews.addAll(getRawData(path + "//neg"));
        this.folds = folds;
        this.foldSize = (this.negativeReviews.size()/folds);
        this.shuffle();
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
        List<Pair<String,String>> reviews = new ArrayList();
        reviews.addAll(this.positiveReviews);
        reviews.addAll(this.negativeReviews);
        return reviews;
    }

    public void shuffle(){
        Collections.shuffle(this.positiveReviews);
        Collections.shuffle(this.negativeReviews);
    }


    public int getFoldSize(){
        return this.foldSize;
    }

    public int getTrainingSize(){
        return this.foldSize * (this.folds-1); // should be 900
    }

    public int getTestSize(){
        return this.foldSize;      // should be 100
    }

    public List<Pair<String,String>> getTrainingReviews(int k) {
        List<Pair<String,String>> reviews = new ArrayList();
        int numToTake = this.getFoldSize(); // should be 100

        for(int i = 1; i <= this.folds; i++){
            if(i == k)
                continue;
            reviews.addAll(this.positiveReviews.subList((i-1 )*numToTake, i*numToTake));
            reviews.addAll(this.negativeReviews.subList((i-1 )*numToTake, i*numToTake));
            //System.out.println("getTrainingReviews: pick from "+ (i-1) *numToTake + " to " +i*numToTake);
        }

        //System.out.println("getTrainingReviews done");
        assert(reviews.size() == this.getTrainingSize());
        return reviews;
    }

    public List<Pair<String,String>> getTestReviews(int k) {
        int numToTake = this.getTestSize(); // should be 100

        List<Pair<String,String>> reviews = new ArrayList();
        reviews.addAll(this.positiveReviews.subList((k-1 )*numToTake, k*numToTake));
        reviews.addAll(this.negativeReviews.subList((k-1 )*numToTake, k*numToTake));
        //System.out.println("getTestReviews: pick from "+ (k-1) *numToTake + " to " +k*numToTake);

        assert(reviews.size() == this.getTestSize());

        return reviews;
    }
}
