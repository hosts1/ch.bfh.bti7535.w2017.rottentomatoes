package classifier.BagOfWords;

import features.BagOfWordFeature;
import features.Features;
import pipeline.Pipe;
import pipeline.Pipeline;
import preprocessing.Preprocessing;
import preprocessing.Tokenizers.NGramTokenizer;
import sentimentAnalysis.SentiAnalysis;
import utils.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toMap;

/*
    NGramVocabularyBuilder: Generates a vocabulary of the most frequently used nGrams within a document.
    Send every document to
 */
public class NGramVocabularyBuilder implements IVocabularyBuilder {
    // store the tokenizer here because toWordVector will need to use the exact same tokenizer!
    private NGramTokenizer tokenizer;

    // build a separate dictionary for every class, they will be merged in setUp()
    public Map<String, Integer> _vocab = new ConcurrentHashMap<String,Integer>();
    public Map<String, Integer> _tmpPositive = new ConcurrentHashMap<String,Integer>();
    public Map<String, Integer> _tmpNegative = new ConcurrentHashMap<String,Integer>();

    // filtering the polarity does for some reason decrease accuracy
    private boolean ignorePolarity = true;

    public NGramVocabularyBuilder(NGramTokenizer tokenizer){
        this.setTokenizer(tokenizer);
    }

    public Void process(Pair<String,String> input)
    {
        String str = input.getSecond();
        String sentClass = input.getFirst();

        Pipeline<String, List<String>> tokenizedStringChain = Pipeline
                .start(this.tokenizer)
                .append(Preprocessing.stopwordFilter)
                .append(Preprocessing.wordFilter);

        List<String> tokens = tokenizedStringChain.run(str);

        for(String token: tokens){
            String[] words = token.split(" ");
            Boolean isPol = false;
            for(String word: words){
                double pol = 0;
                if(!ignorePolarity)
                    pol = Math.max(Math.max(Math.max(Math.abs(SentiAnalysis.sentiWordNet.extract(word, "n")),Math.abs(SentiAnalysis.sentiWordNet.extract(word, "a"))), Math.abs(SentiAnalysis.sentiWordNet.extract(word, "r"))),Math.abs(SentiAnalysis.sentiWordNet.extract(word, "v")));
                else
                    pol = 1;

                if(pol >= 0.25) {
                    isPol = true;
                }
            }
            if(isPol) {
                if(sentClass == "positive")
                    this._tmpPositive.put(token, this._tmpPositive.getOrDefault(token, 0) + 1);
                else
                    this._tmpNegative.put(token, this._tmpNegative.getOrDefault(token, 0) + 1);
            }

        }

        return null;
    }

    public Map<String, Integer> sortAndLimit(Map<String, Integer> map, int n){
        return map.entrySet()
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
        this._tmpNegative = sortAndLimit(this._tmpNegative, numberOfFeaturesToKeep/2);
        this._tmpPositive = sortAndLimit(this._tmpPositive, numberOfFeaturesToKeep/2);
        this._vocab.putAll(this._tmpPositive);
        this._vocab.putAll(this._tmpNegative);
        this._vocab = sortAndLimit(this._vocab, numberOfFeaturesToKeep);
        System.out.println("NGramVocabulary:");
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

    @Override
    public NGramTokenizer getTokenizer() {
        return this.tokenizer;
    }

    @Override
    public void setTokenizer(NGramTokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

}
