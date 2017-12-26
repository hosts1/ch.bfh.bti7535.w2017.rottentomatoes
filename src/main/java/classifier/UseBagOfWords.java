package classifier;

import features.Features;
import pipeline.Pipe;
import pipeline.Pipeline;
import preprocessing.Preprocessing;
import utils.Pair;
import utils.ReviewData;
import weka.core.Instances;

/**
 * Created by hk on 26.12.2017.
 */
public class UseBagOfWords implements Pipe<ClassifierArguments, ClassifierArguments>{

    @Override
    public ClassifierArguments process(ClassifierArguments input) {
        Pipeline<Void, Void> bagOfWordsChain = Pipeline
                .start(Preprocessing.fileBasedVocabularyBuilder);
        bagOfWordsChain.run(null);
        Preprocessing.fileBasedVocabularyBuilder.setUp(input.features, 3000);
        input.vocabulary = Preprocessing.fileBasedVocabularyBuilder;
        return input;
    }
}
