package classifier;

import pipeline.Pipe;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.filters.Filter;

/**
 * Created by hk on 26.12.2017.
 */
public class OptimizeAttributes implements Pipe<ClassifierArguments, ClassifierArguments> {

    @Override
    public ClassifierArguments process(ClassifierArguments input) throws Exception {
        System.out.println("Number of features: " + input.features.getNumberOfFeatures());

        System.out.println("Optimizing attribute selection...");
        weka.filters.supervised.attribute.AttributeSelection attributeSelection = new weka.filters.supervised.attribute.AttributeSelection();

        BestFirst bestFirstSearch = new BestFirst();
        bestFirstSearch.setDirection(new SelectedTag("forward", BestFirst.TAGS_SELECTION));
        bestFirstSearch.setSearchTermination(5);
        bestFirstSearch.setLookupCacheSize(4);
        attributeSelection.setSearch(bestFirstSearch);

        CfsSubsetEval evaluator = new CfsSubsetEval();
        evaluator.setPoolSize(16);
        evaluator.setNumThreads(16);
        attributeSelection.setEvaluator(evaluator);

        attributeSelection.setInputFormat(input.trainInstances);

        Instances resultSet = Filter.useFilter(input.testInstances, attributeSelection);
        input.testInstances = resultSet;

        Instances resultSet2 = Filter.useFilter(input.trainInstances, attributeSelection);
        input.trainInstances = resultSet2;

        input.testInstances.setClassIndex(input.testInstances.numAttributes()-1); // the class attribute is the first one in the vector
        input.trainInstances.setClassIndex(input.trainInstances.numAttributes()-1); // the class attribute is the first one in the vector

        System.out.println("Number of attributes after optimization: " + input.trainInstances.numAttributes());

        return input;


    }
}
