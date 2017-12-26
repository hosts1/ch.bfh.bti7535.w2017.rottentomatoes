import classifier.*;
import features.Features;
import pipeline.Pipeline;
import utils.ReviewData;

public class main {

    public static void main(String[] args) throws Exception {

        ClassifierArguments input = new ClassifierArguments();
        input.reviews = new ReviewData("txt_sentoken");
        input.features = new Features();


        // pipeline steps
        UseBagOfWords useBagOfWords = new UseBagOfWords();
        BuildBagOfWords buildBagOfWordsModel = new BuildBagOfWords();
        ProcessDocuments processDocuments = new ProcessDocuments();
        OptimizeAttributes optimizeAttributes = new OptimizeAttributes();
        DumpToArffFile dumpToArffFile = new DumpToArffFile();
        ClassifyNaiveBayes classifyNaiveBayes = new ClassifyNaiveBayes();
        FoldCrossEvaluation foldCrossEvaluation = new FoldCrossEvaluation();


        Pipeline<ClassifierArguments, ClassifierArguments> naiveBayesChain = Pipeline
                .start(buildBagOfWordsModel)
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