package preprocessing;

import pipeline.Pipe;
import utils.FileReader;

import java.util.*;

public class NegationFilter implements Pipe<String, String> {
    @Override
    public String process(String text){
        List<String> negations = new ArrayList<String>(Arrays.asList("not", "no", "don't", "n't"));

        for(String neg: negations){
            text = text.replaceAll(neg + " ", neg + "_");
        }
        return text;
    }

}
