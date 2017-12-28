package classifier;

import features.Features;
import classifier.BagOfWords.IVocabularyBuilder;
import utils.ReviewData;
import weka.classifiers.Classifier;
import weka.core.Instances;

/**
 * Created by hk on 26.12.2017.
 */
public class ClassifierArguments {
    public Instances instances;
    public ReviewData reviews;
    public Features features;
    public Classifier classifier;
    public IVocabularyBuilder vocabulary;
}
