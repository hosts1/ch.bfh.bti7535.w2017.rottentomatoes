import classifier.*;
import features.Features;
import pipeline.PipelineFactory;
import utils.ReviewData;


import static classifier.BagOfWordModel.*;

public class main {

    public static void main(String[] args) throws Exception {

        int folds = 10;
        int vectorSize = 1000;      // max. number of words in the vocabulary / word vector

        ReviewData reviews = new ReviewData("txt_sentoken", folds);     // holds the 2000 reviews

        PipelineFactory<ClassifierArguments, ClassifierArguments> naiveBayesChain = PipelineFactory
                .start(generateNewBagOfWordsModel)      // generate new BoW vocabulary, add features for every NGram
                .append(trainModel)                     // determine feature values for every review document and create weka instances
                .append(processDocuments)               // determine feature values for every review document and create weka instances
                .append(optimizeAttributes)             // use wekas AttributeSelection algorithm to remove redundant features
                .append(dumpToArffFile)                 // save BoW features (after optimization) to disc for debugging
                .append(classifyNaiveBayes)             // naive bayes classification
                .append(evaluateClassifier);            // evaluate model

        try {
            for(int k = 1; k <= folds; k++) {
                System.out.println("Running k-fold cross naive bayes evaluation chain with k=" + k);
                System.out.println("**************************************************************");
                ClassifierArguments chainArgs = new ClassifierArguments(k, vectorSize, reviews, new Features());           // container used to send arguments and results along the pipeline
                naiveBayesChain.run(chainArgs);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}