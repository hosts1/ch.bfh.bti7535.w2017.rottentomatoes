package classifier;

import pipeline.Pipe;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;

/**
 * Created by hk on 26.12.2017.
 */
public class ClassifyNaiveBayes implements Pipe<ClassifierArguments, ClassifierArguments> {
    @Override
    public ClassifierArguments process(ClassifierArguments input){
        Classifier cModel = (Classifier) new NaiveBayes();
        try {
            System.out.println("Classify using naive bayes...");
            cModel.buildClassifier(input.instances);
        } catch (Exception e) {
            e.printStackTrace();
        }
        input.classifier = cModel;

        return input;
    }
}
