package preprocessing.BagOfWords;

import features.Features;

import java.util.Map;

/**
 * Created by hk on 25.12.2017.
 */
public interface IVocabularyBuilder {
    public Map<String, Integer> getVocabulary();

    public void setUp(Features features, int numberOfFeaturesToKeep);
}
