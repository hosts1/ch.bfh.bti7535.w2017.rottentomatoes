package preprocessing.BagOfWords;

import features.BagOfWordFeature;
import features.Features;
import pipeline.Pipe;
import sentimentAnalysis.SentiAnalysis;
import utils.FileReader;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toMap;

public class FileBasedVocabularyBuilder implements Pipe<Void, Void>, IVocabularyBuilder {
    public Map<String, Integer> _vocab = new ConcurrentHashMap<String,Integer>();

    @Override
    public Void process(Void input)
    {

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
                this._vocab.put(token, 1);      // value doesn't matter here
            }

            return null;
        }catch(Exception ex){
            return null;
        }
    }

    @Override
    public Map<String, Integer> getVocabulary() {
        return this._vocab;
    }

    @Override
    public void setUp(Features features, int numberOfFeaturesToKeep) {
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
