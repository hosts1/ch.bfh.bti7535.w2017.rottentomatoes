package classifier;

import pipeline.Pipe;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
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
        CfsSubsetEval cfsSubsetEval = new CfsSubsetEval();
        cfsSubsetEval.setPoolSize(16);
        cfsSubsetEval.setNumThreads(16);

        BestFirst bestFirstSearch = new BestFirst();
        bestFirstSearch.setDirection(new SelectedTag("forward", BestFirst.TAGS_SELECTION));
        bestFirstSearch.setLookupCacheSize(4);
        bestFirstSearch.setSearchTermination(5);

        attributeSelection.setEvaluator(cfsSubsetEval);
        attributeSelection.setSearch(bestFirstSearch);
        attributeSelection.setInputFormat(input.instances);

        Instances resultSet = Filter.useFilter(input.instances, attributeSelection);
        input.instances = resultSet;
        return input;
    }
}
