package classifier;

import pipeline.Pipe;

/**
 * Created by hk on 26.12.2017.
 */
public class GenerateNewBagOfWords implements Pipe<ClassifierArguments, ClassifierArguments>{

    @Override
    public ClassifierArguments process(ClassifierArguments input) {
        BagOfWordModel.nGramVocabularyBuilder.reset();

        // take all training reviews, build a vocabulary of the words that occur most frequently
        input.reviews.getTrainingReviews(input.k).parallelStream().forEach((review) -> {
            BagOfWordModel.nGramVocabularyBuilder.process(review);
        });

        // sort the vocabulary in descending order and reduce it to n words
        BagOfWordModel.nGramVocabularyBuilder.setUp(input.features, input.vectorSize);
        System.out.println("Size of nGram vocabulary: " + input.vocabulary.getVocabulary().size());

        // add it to the ClassifierArguments object so it can be used by the next pipeline steps
        input.vocabulary = BagOfWordModel.nGramVocabularyBuilder;
        return input;
    }
}
