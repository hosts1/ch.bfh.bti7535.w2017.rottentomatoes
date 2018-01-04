import classifier.*;
import features.Features;
import features.NummericFeature;
import pipeline.Pipeline;
import preprocessing.Preprocessing;
import sentimentAnalysis.SentiAnalysis;
import utils.ReviewData;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;

import java.util.Random;

import static classifier.BagOfWordModel.*;

public class main {

    public static void main(String[] args) throws Exception {

        int folds = 10;

        ReviewData reviews = new ReviewData("txt_sentoken", folds);     // holds the 2000 reviews

        Pipeline<ClassifierArguments, ClassifierArguments> naiveBayesChain = Pipeline
                .start(generateNewBagOfWordsModel)      // generate new BoW vocabulary, add features for every NGram
                .append(trainModel)                     // determine feature values for every review document and create weka instances
                .append(processDocuments)               // determine feature values for every review document and create weka instances
                //.append(dumpToArffFile)                 // save BoW features (before optimization) to disc for debugging
                .append(optimizeAttributes)             // use wekas AttributeSelection algorithm to remove redundant features
                .append(dumpToArffFile)                 // save BoW features (after optimization) to disc for debugging
                .append(classifyNaiveBayes)             // naive bayes classification
                .append(evaluateClassifier);            // cross evaluation

        try {
            for(int k = 1; k <= folds; k++) {
                System.out.println("Running k-fold cross naive bayes evaluation chain with k=" + k);
                System.out.println("**************************************************************");
                ClassifierArguments chainArgs = new ClassifierArguments(k, reviews, new Features());           // container used to send arguments and results along the pipeline
                naiveBayesChain.run(chainArgs);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}