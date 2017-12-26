package preprocessing.BagOfWords;

import features.BagOfWordFeature;
import features.Features;
import pipeline.Pipe;
import preprocessing.Preprocessing;
import sentimentAnalysis.SentiAnalysis;

import java.util.*;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.*;

public class VocabularyBuilder implements Pipe<List<String>, Void>, IVocabularyBuilder {
    public Map<String, Integer> _vocab = new ConcurrentHashMap<String,Integer>();

    @Override
    public Void process(List<String> input)
    {
        for(String token: input){
            double pol = Math.max(Math.max(Math.max(Math.abs(SentiAnalysis.sentiWordNet.extract(token, "n")),Math.abs(SentiAnalysis.sentiWordNet.extract(token, "a"))), Math.abs(SentiAnalysis.sentiWordNet.extract(token, "r"))),Math.abs(SentiAnalysis.sentiWordNet.extract(token, "v")));

            if(pol >= 0.25) {
                this._vocab.put(token, this._vocab.getOrDefault(token, 0) + 1);
            }
            if(pol >= 0.75) {
                this._vocab.put(token, this._vocab.getOrDefault(token, 0) + 30);
            }

        }
        return null;
    }

    public void sortAndLimit(int n){
        this._vocab = this._vocab.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(n)
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
    }


    @Override
    public Map<String, Integer> getVocabulary() {
        return this._vocab;
    }

    @Override
    public void setUp(Features features, int numberOfFeaturesToKeep) {
        this.sortAndLimit(numberOfFeaturesToKeep);
        System.out.println(this._vocab);

        // add a feature for every word
        Iterator it = this._vocab.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String word = (String) pair.getKey();
            features.addFeature(new BagOfWordFeature(word));
        }

    }
}
