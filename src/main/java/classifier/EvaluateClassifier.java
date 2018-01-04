package classifier;

import pipeline.Pipe;
import weka.classifiers.Evaluation;

import java.util.Random;

/**
 * Created by hk on 26.12.2017.
 */
public class EvaluateClassifier implements Pipe<ClassifierArguments, ClassifierArguments> {

    @Override
    public ClassifierArguments process(ClassifierArguments input) {
        // Test the model
        try {
            Evaluation eTest = new Evaluation( input.testInstances);
            eTest.evaluateModel(input.classifier,  input.testInstances);

            String strSummary = eTest.toSummaryString();
            System.out.println(strSummary);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return input;
    }
}
