package classifier;

import features.Features;
import classifier.BagOfWords.IVocabularyBuilder;
import utils.ReviewData;
import weka.classifiers.Classifier;
import weka.core.Instances;

/**
 * Created by hk on 26.12.2017.
 */

/*
  our pipeline can only handle single result objects / arguments...everything is attached to this object
 */
public class ClassifierArguments {
    public Instances instances;
    public ReviewData reviews;
    public Features features;
    public Classifier classifier;
    public IVocabularyBuilder vocabulary;
}
