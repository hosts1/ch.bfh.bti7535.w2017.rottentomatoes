package classifier;

import features.Features;
import preprocessing.Tokenizers.NGramTokenizer;

import java.util.Map;

/**
 * Created by hk on 25.12.2017.
 */
public interface IVocabularyBuilder {
    public Map<String, Integer> getVocabulary();
    public void reset();
    public void setUp(Features features, int numberOfFeaturesToKeep);
    public NGramTokenizer getTokenizer();
    public void setTokenizer(NGramTokenizer tokenizer);
}
