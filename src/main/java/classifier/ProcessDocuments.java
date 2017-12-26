package classifier;

import pipeline.Pipe;
import pipeline.Pipeline;
import preprocessing.BagOfWords.ToWordVector;
import preprocessing.Preprocessing;
import utils.Pair;
import utils.ReviewData;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.HashMap;

/**
 * Created by hk on 26.12.2017.
 */
public class ProcessDocuments implements Pipe<ClassifierArguments, ClassifierArguments> {

    @Override
    public ClassifierArguments process(ClassifierArguments input) {
        // Create training instances
        input.instances = new Instances("Data", input.features.getAttributes(), 2000); // trainingSet with our features and a capacity of 1000 records
        input.instances.setClassIndex(0); // the class attribute is the first one in the vector


        // process every review, extract feature values and add them to the training-set
        // the following pipeline creates vectors (actually maps) for every document, containing the number of occurrences of the words within the vocabulary
        ToWordVector toWordVector = new ToWordVector(input.vocabulary);
        Pipeline<String, HashMap<String,Integer>> stringToWordVectorChain = Pipeline
                .start(Preprocessing.mixedTriGramTokenizer)
                .append(Preprocessing.stopwordFilter)
                .append(Preprocessing.wordFilter)
                .append(toWordVector);

        input.reviews.getReviews().parallelStream().forEach((review) -> {
            Instance inst = new DenseInstance(input.features.getNumberOfFeatures());
            // set the sentiment class to positive or negative label
            input.features.setClass(inst, review.getFirst());
            HashMap<String,Integer> wordVector = stringToWordVectorChain.run(review.getSecond());

            input.features.determineFeatureValues(inst, review.getSecond(), wordVector); // for each feature it will do "setValue" on the instance
            input.instances.add(inst);
        });

        return input;
    }
}
