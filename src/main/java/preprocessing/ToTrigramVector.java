package preprocessing;

import pipeline.Pipe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ToTrigramVector implements Pipe<List<String>, HashMap<String, Integer>> {

    @Override
    public HashMap<String, Integer> process(List<String> input) {
        // List<Integer> vec = new ArrayList<Integer>();
        HashMap<String, Integer> vec = new HashMap();

        Iterator it = Preprocessing.trigramVocabularyBuilder._vocab.entrySet().iterator();
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
