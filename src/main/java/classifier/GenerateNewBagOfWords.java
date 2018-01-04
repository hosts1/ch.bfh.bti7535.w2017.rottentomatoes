package classifier;

import pipeline.Pipe;

/**
 * Created by hk on 26.12.2017.
 */
public class GenerateNewBagOfWords implements Pipe<ClassifierArguments, ClassifierArguments>{

    @Override
    public ClassifierArguments process(ClassifierArguments input) {
        BagOfWordModel.nGramVocabularyBuilder.reset();

        input.reviews.getTrainingReviews(input.k).parallelStream().forEach((review) -> {
            BagOfWordModel.nGramVocabularyBuilder.process(review);
        });

        // sort the vocabulary in descending order and reduce it to n words
        BagOfWordModel.nGramVocabularyBuilder.setUp(input.features, 2000);
        input.vocabulary = BagOfWordModel.nGramVocabularyBuilder;

        System.out.println("Size of nGram vocabulary: " + input.vocabulary.getVocabulary().size());
        return input;
    }
}
