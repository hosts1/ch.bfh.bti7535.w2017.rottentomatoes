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
public class UseExistingBagOfWordsModel implements Pipe<ClassifierArguments, ClassifierArguments>{

    @Override
    public ClassifierArguments process(ClassifierArguments input) {
        Pipeline<Void, Void> bagOfWordsChain = Pipeline
                .start(BagOfWordModel.fileBasedVocabularyBuilder);
        bagOfWordsChain.run(null);
        BagOfWordModel.fileBasedVocabularyBuilder.setUp(input.features, 3000);
        input.vocabulary = BagOfWordModel.fileBasedVocabularyBuilder;
        return input;
    }
}
