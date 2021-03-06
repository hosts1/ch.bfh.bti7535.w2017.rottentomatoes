package sentimentAnalysis;

import pipeline.Pipe;
import utils.Pair;

import java.util.List;

import static sentimentAnalysis.SentiAnalysis.sentiWordNet;

/**
 * Created by hk on 21.12.2017.
 */
public class CountNegativeWords implements Pipe<Pair<List<String>,List<String>>, Double>{

    @Override
    public Double process(Pair<List<String>,List<String>> input) {
        List<Double> pol = sentiWordNet.getTextPolarity(input);

        Double count = 0.0;
        for(Double p: pol){
            if(p <= -0.25){
                count += 1;
            }
        }

        return count;
    }
}
