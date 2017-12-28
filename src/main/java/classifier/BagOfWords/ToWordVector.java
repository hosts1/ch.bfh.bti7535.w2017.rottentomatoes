package classifier.BagOfWords;

import pipeline.Pipe;
import pipeline.Pipeline;
import preprocessing.Preprocessing;
import preprocessing.Tokenizers.NGramTokenizer;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/*
    ToWordVector: Converts a document into a vector, noting which ngrams/words of the vocabulary are present within the document
    - returnCount defines whether the vector should contain the number of occurrences or simply 0-or-1- count seems to slightly improve accuracy
 */
public class ToWordVector implements Pipe<String, HashMap<String, Integer>> {
    private NGramTokenizer tokenizer;
    public Map<String, Integer> vocabulary;
    private boolean returnCount;

    public ToWordVector(IVocabularyBuilder vocabularyBuilder, boolean returnCount){
        this.vocabulary = vocabularyBuilder.getVocabulary();
        this.tokenizer = vocabularyBuilder.getTokenizer();
        this.returnCount = returnCount;
    }

    @Override
    public HashMap<String, Integer> process(String input) {

        Pipeline<String, List<String>> tokenizedStringChain = Pipeline
                .start(this.tokenizer)
                .append(Preprocessing.stopwordFilter)
                .append(Preprocessing.wordFilter);

        List<String> tokens = tokenizedStringChain.run(input);

        HashMap<String, Integer> vec = new HashMap();

        Iterator it = this.vocabulary.keySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            String word = (String) it.next();
            Map<String, Long> tokensCount = tokens.stream().collect(
                    Collectors.groupingBy(Function.identity(), Collectors.counting()));
            int count = tokensCount.getOrDefault(word, 0L).intValue();

            //vec.add(count);
            if(count > 0 && this.returnCount)
                vec.put(word, count);
            else if(count > 0 && !this.returnCount)
                vec.put(word, 1);
            else
                vec.put(word, 0);
            i += 1;
        }
        return vec;
    }

}
