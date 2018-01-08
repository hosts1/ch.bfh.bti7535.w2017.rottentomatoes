import classifier.*;
import features.Features;
import pipeline.PipelineFactory;
import preprocessing.Preprocessing;
import utils.ReviewData;


import static classifier.BagOfWordModel.*;

public class main {

    public static void main(String[] args) throws Exception {

        int folds = 10;
        ReviewData reviews = new ReviewData("txt_sentoken", folds);     // holds the 2000 reviews

        try {
            double percentage = 0;
            for(int k = 1; k <= folds; k++) {
                System.out.println("Running k-fold cross naive bayes evaluation chain, iteration: " + k);
                System.out.println("**************************************************************");
                ClassifierArguments chainArgs = new ClassifierArguments(k, 5000, reviews, new Features());           // container used to send arguments and results along the pipeline

                percentage += PipelineFactory
                        .start(generateNewBagOfWordsModel)      // generate new BoW vocabulary, add features for every NGram
                        .append(trainModel)                     // determine feature values for every review document and create weka instances
                        .append(processDocuments)               // determine feature values for every review document and create weka instances
                        .append(optimizeAttributes)             // use wekas AttributeSelection algorithm to remove redundant features
                        .append(dumpToArffFile)                 // save BoW features (after optimization) to disc for debugging
                        .append(classifyNaiveBayes)             // naive bayes classification
                        .append(evaluateClassifier)             // evaluate model
                        .run(chainArgs);
            }
            System.out.println("Overall score: " + percentage/folds);
            System.out.println("**************************************************************");
        } catch (Exception ex) {
            ex.printStackTrace();
        }



    }


}