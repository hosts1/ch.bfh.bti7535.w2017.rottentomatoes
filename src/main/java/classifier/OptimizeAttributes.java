package classifier;

import pipeline.Pipe;
import weka.attributeSelection.*;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.filters.Filter;

/**
 * Created by hk on 26.12.2017.
 */
public class OptimizeAttributes implements Pipe<ClassifierArguments, ClassifierArguments> {

    @Override
    public ClassifierArguments process(ClassifierArguments input) throws Exception {
        System.out.println("Optimizing attribute selection...");
        weka.filters.supervised.attribute.AttributeSelection attributeSelection = new weka.filters.supervised.attribute.AttributeSelection();
        CorrelationAttributeEval eval = new CorrelationAttributeEval();
        Ranker ranker = new Ranker();
        ranker.setNumToSelect(Math.round(input.features.getNumberOfFeatures()/10)); // select top 10%

        attributeSelection.setEvaluator(eval);
        attributeSelection.setSearch(ranker);
        attributeSelection.setInputFormat(input.trainInstances);

        Instances resultSet = Filter.useFilter(input.testInstances, attributeSelection);
        input.testInstances = resultSet;

        Instances resultSet2 = Filter.useFilter(input.trainInstances, attributeSelection);
        input.trainInstances = resultSet2;
        return input;
    }
}
