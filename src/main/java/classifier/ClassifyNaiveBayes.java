package classifier;

import pipeline.Pipe;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;

/**
 * Created by hk on 26.12.2017.
 */
public class ClassifyNaiveBayes implements Pipe<ClassifierArguments, ClassifierArguments> {
    @Override
    public ClassifierArguments process(ClassifierArguments input){
        // build (train) the classifier using the training reviews

        Classifier cModel = new NaiveBayes();
        input.classifier = cModel;
        try {
            System.out.println("Classify using naive bayes...");
            cModel.buildClassifier(input.trainInstances);
        } catch (Exception e) {
            e.printStackTrace();
        }
        input.classifier = cModel;

        return input;
    }
}
