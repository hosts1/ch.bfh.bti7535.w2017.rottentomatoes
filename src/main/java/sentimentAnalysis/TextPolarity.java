package sentimentAnalysis;

import pipeline.Pipe;
import utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static sentimentAnalysis.SentiAnalysis.sentiWordNet;

/**
 * Created by hk on 21.12.2017.
 */
public class TextPolarity  implements Pipe<Pair<List<String>,List<String>>, Double>{

    @Override
    public Double process(Pair<List<String>,List<String>> input) {
        List<String> negations = new ArrayList<>(Arrays.asList("not", "don't", "didn't", "n't", "no"));

        List<Double> pol = sentiWordNet.getTextPolarity(input);

        int index = 0;
        for(String word: input.getFirst()){
            for(String neg: negations){
                if(word.equals(neg) && pol.size()-1 >= index+1){
                    pol.set(index+1, -pol.get(index+1));
                }
            }
            index += 1;
        }

        double polarity = 0;
        for(Double p: pol){
            polarity += p;
        }
        return polarity / pol.size();
    }
}
