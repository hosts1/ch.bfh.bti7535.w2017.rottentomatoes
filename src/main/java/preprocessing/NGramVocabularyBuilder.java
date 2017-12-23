package preprocessing;

import pipeline.Pipe;
import sentimentAnalysis.SentiAnalysis;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toMap;

public class NGramVocabularyBuilder implements Pipe<List<String>, Void> {
    public Map<String, Integer> _vocab = new ConcurrentHashMap<String,Integer>();

    @Override
    public Void process(List<String> input)
    {
        for(String token: input){
            String[] words = token.split(" ");
            Boolean isPol = false;
            for(String word: words){
                double pol = Math.max(Math.max(Math.max(Math.abs(SentiAnalysis.sentiWordNet.extract(word, "n")),Math.abs(SentiAnalysis.sentiWordNet.extract(word, "a"))), Math.abs(SentiAnalysis.sentiWordNet.extract(word, "r"))),Math.abs(SentiAnalysis.sentiWordNet.extract(word, "v")));

                if(pol >= 0.25) {
                    isPol = true;
                }
            }
            if(isPol)
                this._vocab.put(token, this._vocab.getOrDefault(token, 0) + 1);

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

}
