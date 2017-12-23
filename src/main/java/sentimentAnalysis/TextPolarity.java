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

        List<Double> pol = sentiWordNet.getTextPolarity(input);


        double polarity = 0;
        for(Double p: pol){
            polarity += p;
        }
        return polarity / pol.size();
    }
}
