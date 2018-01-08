package classifier;

import pipeline.Pipe;
import weka.classifiers.Evaluation;

import java.util.Random;

/**
 * Created by hk on 26.12.2017.
 */
public class EvaluateClassifier implements Pipe<ClassifierArguments, Double> {

    @Override
    public Double process(ClassifierArguments input) {
        // Test the model
        double percentage = 0;
        try {
            Evaluation eTest = new Evaluation( input.testInstances);
            eTest.evaluateModel(input.classifier,  input.testInstances);

            String strSummary = eTest.toSummaryString();
            percentage = eTest.pctCorrect();
            System.out.println("Correctly classified instances: " + percentage);
            //System.out.println(strSummary);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return percentage;
    }
}
