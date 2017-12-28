import classifier.*;
import features.Features;
import pipeline.Pipeline;
import utils.ReviewData;
import static classifier.BagOfWordModel.*;

public class main {

    public static void main(String[] args) throws Exception {

        ClassifierArguments input = new ClassifierArguments();
        input.reviews = new ReviewData("txt_sentoken");
        input.features = new Features();

        Pipeline<ClassifierArguments, ClassifierArguments> naiveBayesChain = Pipeline
                //.start(useExistingBagOfWordsModel)
                .start(generateNewBagOfWordsModel)
                .append(processDocuments)
                .append(optimizeAttributes)
                .append(dumpToArffFile)
                .append(classifyNaiveBayes)
                .append(foldCrossEvaluation);

        try {
            naiveBayesChain.run(input);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}