package classifier;

import classifier.BagOfWords.ToWordVector;
import pipeline.Pipe;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.HashMap;

/**
 * Created by hk on 26.12.2017.
 */
public class TrainModel implements Pipe<ClassifierArguments, ClassifierArguments> {

    @Override
    public ClassifierArguments process(ClassifierArguments input) {
        // Create training instances
        input.trainInstances = new Instances("Data", input.features.getAttributes(), input.reviews.getTrainingSize()); // trainingSet with our features and a capacity of 1000 records
        input.trainInstances.setClassIndex(0); // the class attribute is the first one in the vector
        ToWordVector toWordVector = new ToWordVector(input.vocabulary, true);
        System.out.println("Vectorizing training set: " + input.reviews.getTrainingSize() + " reviews");
        // process every review, extract feature values and add them to the training-set
        input.reviews.getTrainingReviews(input.k).parallelStream().forEach((review) -> {
            Instance inst = new DenseInstance(input.features.getNumberOfFeatures());
            input.features.setClass(inst, review.getFirst());                   // set the sentiment class to positive or negative label

            HashMap<String,Integer> wordVector = toWordVector.process(review.getSecond());  // vectorize the document (count vocabulary NGram occurrences)
            input.features.determineFeatureValues(inst, review.getSecond(), wordVector); // for each feature it will do "setValue" on the instance
            synchronized(this) {
                input.trainInstances.add(inst);
            }
            // System.out.println(input.instances.size());
        });
       

        return input;
    }
}
