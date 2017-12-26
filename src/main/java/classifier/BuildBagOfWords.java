package classifier;

import features.BagOfWordFeature;
import pipeline.Pipe;
import pipeline.Pipeline;
import preprocessing.Preprocessing;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by hk on 26.12.2017.
 */
public class BuildBagOfWords implements Pipe<ClassifierArguments, ClassifierArguments>{

    @Override
    public ClassifierArguments process(ClassifierArguments input) {
        Pipeline<String, Void> bagOfWordsChain = Pipeline
                .start(Preprocessing.biGramTokenizer)
                .append(Preprocessing.stopwordFilter)
                .append(Preprocessing.wordFilter)
                .append(Preprocessing.trigramVocabularyBuilder);

        input.reviews.getReviews().parallelStream().forEach((review) -> {
            bagOfWordsChain.run(review.getSecond());
        });

        // sort the vocabulary in descending order and reduce it to n words
        Preprocessing.trigramVocabularyBuilder.setUp(input.features, 10000);

        input.vocabulary = Preprocessing.trigramVocabularyBuilder;
        return input;
    }
}
