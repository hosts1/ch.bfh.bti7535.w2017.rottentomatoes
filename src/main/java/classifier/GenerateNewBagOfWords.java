package classifier;

import pipeline.Pipe;

/**
 * Created by hk on 26.12.2017.
 */
public class GenerateNewBagOfWords implements Pipe<ClassifierArguments, ClassifierArguments>{

    @Override
    public ClassifierArguments process(ClassifierArguments input) {

        input.reviews.getReviews().parallelStream().forEach((review) -> {
            BagOfWordModel.nGramVocabularyBuilder.process(review);
        });

        // sort the vocabulary in descending order and reduce it to n words
        BagOfWordModel.nGramVocabularyBuilder.setUp(input.features, 50000);

        input.vocabulary = BagOfWordModel.nGramVocabularyBuilder;
        return input;
    }
}
