package preprocessing.BagOfWords;

import pipeline.Pipe;
import preprocessing.Preprocessing;

import java.util.*;

import static java.util.stream.Collectors.toMap;

public class ToWordVector implements Pipe<List<String>, HashMap<String, Integer>> {
    public Map<String, Integer> vocabulary;

    public ToWordVector(IVocabularyBuilder vocabularyBuilder){
        this.vocabulary = vocabularyBuilder.getVocabulary();
    }

    @Override
    public HashMap<String, Integer> process(List<String> input) {
        // List<Integer> vec = new ArrayList<Integer>();
        HashMap<String, Integer> vec = new HashMap();

        Iterator it = this.vocabulary.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String word = (String) pair.getKey();
            int count = 0;
            for (String w : input) {
                if (w.equals(word)) {
                    count += 1;
                }
            }

            //vec.add(count);
            vec.put(word, count);
            i += 1;
        }
        return vec;
    }

}
