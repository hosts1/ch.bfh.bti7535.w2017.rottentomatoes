package classifier;

import features.BagOfWordFeature;
import features.Features;
import pipeline.PipelineFactory;
import preprocessing.Preprocessing;
import preprocessing.Tokenizers.NGramTokenizer;
import sentimentAnalysis.SentiAnalysis;
import utils.FileReader;
import utils.Review;

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
    private boolean ignorePolarity = false;

    public NGramVocabularyBuilder(NGramTokenizer tokenizer){

        this.setTokenizer(tokenizer);

    }

    public Void process(Review rvw)
    {
        PipelineFactory<String, List<String>> tokenizedStringChain = PipelineFactory
                .start(Preprocessing.negationFilter)
                .append(this.tokenizer)
                .append(Preprocessing.stopwordFilter)
                .append(Preprocessing.wordFilter);

        List<String> tokens = BagOfWordModel.queryTokenizedStringCache(rvw.getText());
        if(tokens == null) {
            tokens = tokenizedStringChain.run(rvw.getText());
        }

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
                if(rvw.getSentimentClass() == "positive")
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
    public void reset() {
        this. _vocab = new ConcurrentHashMap<String,Integer>();
        this._tmpPositive = new ConcurrentHashMap<String,Integer>();
        this._tmpNegative = new ConcurrentHashMap<String,Integer>();

        try {
            FileReader fr = new FileReader();
            String vocabularyStr = fr.readFile("vocab.txt");
            StringTokenizer stok = new StringTokenizer(vocabularyStr, "\n");
            while (stok.hasMoreTokens()) {
                String token = stok.nextToken();  // get and save in variable so it can be used more than once
                token = token.replaceAll("@attribute \'", "");
                token = token.replaceAll("\' numeric", "");
                token = token.replaceAll("@attribute ", "");
                token = token.replaceAll(" numeric", "");
                this._vocab.put(token, 999999);      // weight doesn't matter here
            }
        }catch(Exception ex){
        }
    }

    @Override
    public void setUp(Features features, int numberOfFeaturesToKeep) {
        this._tmpNegative = sortAndLimit(this._tmpNegative, numberOfFeaturesToKeep);
        this._tmpPositive = sortAndLimit(this._tmpPositive, numberOfFeaturesToKeep);
        this._vocab.putAll(this._tmpPositive);
        this._vocab.putAll(this._tmpNegative);
        this._vocab = sortAndLimit(this._vocab, numberOfFeaturesToKeep);

        // add a feature for every word
        Iterator it = this._vocab.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String word = (String) pair.getKey();
            features.addFeature(new BagOfWordFeature(word));
        }

        System.out.println("NGramVocabulary:");
        System.out.println(this._vocab);
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
