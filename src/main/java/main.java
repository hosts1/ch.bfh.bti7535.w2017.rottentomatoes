import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import java.util.Random;

public class main {

    public static void main(String[] args) {
        /* SentiWordNet usage example:
        Pipeline<String, Double> chain = Pipeline
                .start(Preprocessing.tokenizer)
                .append(Preprocessing.maxEntPosTagger)
                .append(sentimentAnalysis.textPolarity);
        System.out.println(chain.run("This is a good film"));
        System.out.println(chain.run("This is a bad film"));
        */

        Features features = new Features();

        ReviewData data = new ReviewData("txt_sentoken");

        // Create an empty training set
        Instances trainingSet = new Instances("Rel", features.getAttributes(), 2000); // trainingSet with our features and a capacity of 1000 records
        trainingSet.setClassIndex(0); // the class attribute is the first one in the vector


        // process every review, extract feature values and add them to the training-set
        data.getReviews().parallelStream().forEach((review) -> {
            Instance inst = new DenseInstance(features.getNumberOfFeatuers());
            features.setClass(inst, review.getFirst());
            features.determineFeatureValues(inst, review.getSecond()); // for each feature it will do "setValue" on the instance
            trainingSet.add(inst);
        });

        // Create a naïve bayes classifier
        try {
            Classifier cModel = (Classifier) new NaiveBayes();
            cModel.buildClassifier(trainingSet);

            // Test the model
            Evaluation eTest = new Evaluation(trainingSet);
            Random randomGenerator = new Random();

            eTest.crossValidateModel(cModel, trainingSet, 10, randomGenerator);

            // print results
            String strSummary = eTest.toSummaryString();
            System.out.println(strSummary);

            //double[][] cmMatrix = eTest.confusionMatrix();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}