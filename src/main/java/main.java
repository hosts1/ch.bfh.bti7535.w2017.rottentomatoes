import features.NummericFeature;
import features.NummericWordFeature;
import pipeline.Pipeline;
import preprocessing.Preprocessing;
import preprocessing.VocabularyBuilder;
import sentimentAnalysis.SentiAnalysis;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class main {

    public static void main(String[] args) throws Exception {
        ReviewData rvw = new ReviewData("txt_sentoken");
        Features features = new Features();

        Pipeline<String, Void> testChain = Pipeline
                .start(Preprocessing.luceneTokenizer)
                .append(Preprocessing.vocabularyBuilder);


        rvw.getReviews().parallelStream().forEach((review) -> {
            testChain.run(review.getSecond());
        });

        Preprocessing.vocabularyBuilder.sortAndLimit(2000);

        System.out.println(Preprocessing.vocabularyBuilder._vocab.size());
        System.out.println(Preprocessing.vocabularyBuilder._vocab);
        Pipeline<String, HashMap<String,Integer>> stringToWordVectorChain = Pipeline
                .start(Preprocessing.luceneTokenizer)
                .append(Preprocessing.toWordVector);


        // add a feature for every word
        Iterator it = Preprocessing.vocabularyBuilder._vocab.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String word = (String) pair.getKey();
            features.addFeature(new NummericWordFeature(word));
        }


        // Create an empty training set
        Instances trainingSet = new Instances("Rel", features.getAttributes(), 2000); // trainingSet with our features and a capacity of 1000 records
        trainingSet.setClassIndex(0); // the class attribute is the first one in the vector

        // process every review, extract feature values and add them to the training-set
        rvw.getReviews().parallelStream().forEach((review) -> {
            HashMap<String,Integer> wordVector = stringToWordVectorChain.run(review.getSecond());

            Instance inst = new DenseInstance(features.getNumberOfFeatures());

            // set the sentiment class to positive or negative
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