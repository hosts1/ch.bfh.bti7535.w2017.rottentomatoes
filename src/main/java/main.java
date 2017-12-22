import features.BagOfWordFeature;
import pipeline.Pipeline;
import preprocessing.Preprocessing;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

public class main {

    public static void main(String[] args) throws Exception {
        ReviewData rvw = new ReviewData("txt_sentoken");
        Features features = new Features();

        // build a bag of words / vocabulary
        Pipeline<String, Void> bagOfWordsChain = Pipeline
                .start(Preprocessing.luceneTokenizer)
                .append(Preprocessing.stopwordFilter)
                .append(Preprocessing.wordFilter)
                .append(Preprocessing.vocabularyBuilder);

        rvw.getReviews().parallelStream().forEach((review) -> {
            bagOfWordsChain.run(review.getSecond());
        });

        // sort the vocabulary in descending order and reduce it to n words
        Preprocessing.vocabularyBuilder.sortAndLimit(500);

        System.out.println(Preprocessing.vocabularyBuilder._vocab);

        // add a feature for every word
        Iterator it = Preprocessing.vocabularyBuilder._vocab.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String word = (String) pair.getKey();
            features.addFeature(new BagOfWordFeature(word));
        }

        // the following pipeline creates vectors (actually maps) for every document, containing the number of occurrences of the words within the vocabulary
        Pipeline<String, HashMap<String,Integer>> stringToWordVectorChain = Pipeline
                .start(Preprocessing.luceneTokenizer)
                .append(Preprocessing.stopwordFilter)
                .append(Preprocessing.wordFilter)
                .append(Preprocessing.toWordVector);

        // Create training instances
        Instances trainingSet = new Instances("Data", features.getAttributes(), 2000); // trainingSet with our features and a capacity of 1000 records
        trainingSet.setClassIndex(0); // the class attribute is the first one in the vector

        // process every review, extract feature values and add them to the training-set
        rvw.getReviews().parallelStream().forEach((review) -> {
            Instance inst = new DenseInstance(features.getNumberOfFeatures());

            // set the sentiment class to positive or negative label
            HashMap<String,Integer> wordVector = stringToWordVectorChain.run(review.getSecond());
            features.setClass(inst, review.getFirst());
            features.determineFeatureValues(inst, review.getSecond(), wordVector); // for each feature it will do "setValue" on the instance

            trainingSet.add(inst);
        });


        // Create a naïve bayes classifier
        try {
            Classifier cModel = (Classifier) new NaiveBayes();
            cModel.buildClassifier(trainingSet);

            // Test the model
            Evaluation eTest = new Evaluation(trainingSet);
            eTest.crossValidateModel(cModel, trainingSet, 10, new Random(1));

            // print results
            String strSummary = eTest.toSummaryString();
            System.out.println(strSummary);

            //double[][] cmMatrix = eTest.confusionMatrix();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}