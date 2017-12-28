import classifier.*;
import features.Features;
import features.NummericFeature;
import pipeline.Pipeline;
import preprocessing.Preprocessing;
import sentimentAnalysis.SentiAnalysis;
import utils.ReviewData;
import static classifier.BagOfWordModel.*;

public class main {

    public static void main(String[] args) throws Exception {

        ClassifierArguments input = new ClassifierArguments();    // container used to send arguments and results along the pipeline
        input.reviews = new ReviewData("txt_sentoken");     // holds the 2000 reviews
        input.features = new Features();                          // feature + weka attribute container

        /*
         Add features (BagOfWord model features will be added separately)
         */
        // Review-Length-Feature
        input.features.addFeature(new NummericFeature("reviewLengthFeature",
                (review) -> { return (double)review.length(); }
        ));

        // Review-polarity
        input.features.addFeature(new NummericFeature("reviewPolarity",
                (review) -> {
                    Pipeline<String, Double> chain = Pipeline
                            .start(Preprocessing.tokenizer)
                            .append(Preprocessing.maxEntPosTagger)
                            .append(SentiAnalysis.textPolarity);
                    return chain.run(review);
                }
        ));

        // Review-purity
        input.features.addFeature(new NummericFeature("reviewPurity",
                (review) -> {
                    Pipeline<String, Double> chain = Pipeline
                            .start(Preprocessing.tokenizer)
                            .append(Preprocessing.maxEntPosTagger)
                            .append(SentiAnalysis.textPurity);
                    return chain.run(review);
                }
        ));

        Pipeline<ClassifierArguments, ClassifierArguments> naiveBayesChain = Pipeline
                //.start(useExistingBagOfWordsModel)    // use existing BoW features of vocabulary.txt, add features for every NGram
                .start(generateNewBagOfWordsModel)      // generate new BoW vocabulary, add features for every NGram
                .append(processDocuments)               // determine feature values for every review document and create weka instances
                .append(optimizeAttributes)             // use wekas AttributeSelection algorithm to remove redundant features
                .append(dumpToArffFile)                 // save BoW features to disc for future use
                .append(classifyNaiveBayes)             // naive bayes classification
                .append(foldCrossEvaluation);           // 10-fold cross evaluation

        try {
            naiveBayesChain.run(input);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}