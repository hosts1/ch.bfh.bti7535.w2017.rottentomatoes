package classifier;

import features.Features;
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
    public int k = 1;
    public int vectorSize = 0;
    public Instances testInstances;
    public Instances trainInstances;
    public ReviewData reviews;
    public Features features;
    public Classifier classifier;
    public IVocabularyBuilder vocabulary;

    public ClassifierArguments(int k, int vectorSize, ReviewData reviews, Features features){
        this.k = k;
        this.vectorSize = vectorSize;
        this.reviews = reviews;
        this.features = features;
    }
}
