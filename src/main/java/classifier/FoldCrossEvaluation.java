package classifier;

import pipeline.Pipe;
import weka.classifiers.Evaluation;

import java.util.Random;

/**
 * Created by hk on 26.12.2017.
 */
public class FoldCrossEvaluation implements Pipe<ClassifierArguments, ClassifierArguments> {

    @Override
    public ClassifierArguments process(ClassifierArguments input) {
        // Test the model
        try {
            System.out.println("Run evaluation...");
            Evaluation eTest = new Evaluation(input.instances);
            eTest.crossValidateModel(input.classifier, input.instances, 10, new Random(1));

            // print results
            String strSummary = eTest.toSummaryString();
            System.out.println(strSummary);

            double[][] cmMatrix = eTest.confusionMatrix();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return input;
    }
}
