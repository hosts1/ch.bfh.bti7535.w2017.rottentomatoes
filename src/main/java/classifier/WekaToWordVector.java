package classifier;

import pipeline.Pipe;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.core.tokenizers.NGramTokenizer;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * Created by hk on 26.12.2017.
 */
public class WekaToWordVector implements Pipe<ClassifierArguments, ClassifierArguments>{

    @Override
    public ClassifierArguments process(ClassifierArguments input) {
        ConverterUtils.DataSource source = null;
        try {
            source = new ConverterUtils.DataSource("data/reviews.arff");
            Instances dataset = source.getDataSet();
            dataset.setClassIndex(dataset.numAttributes()-1);


            //set class index to the last attribute
            StringToWordVector stringToWordVector = new StringToWordVector();
            NGramTokenizer wordTokenizer = new NGramTokenizer();
            wordTokenizer.setNGramMinSize(1);
            wordTokenizer.setNGramMaxSize(3);
            stringToWordVector.setIDFTransform(false);
            stringToWordVector.setTFTransform(false);
            //stringToWordVector.setAttributeIndices("first");
            int[] array ={0};
            stringToWordVector.setAttributeIndicesArray(array);
            stringToWordVector.setDoNotOperateOnPerClassBasis(false);
            //stringToWordVector.setNormalizeDocLength(new SelectedTag());
            stringToWordVector.setInvertSelection(false);
            stringToWordVector.setLowerCaseTokens(false);
            stringToWordVector.setMinTermFreq(2);
            stringToWordVector.setOutputWordCounts(true);
            stringToWordVector.setIDFTransform(true);
            //stringToWordVector.setPeriodicPruning(-1.0);
            //stringToWordVector.setStemmer(stemmer);
            stringToWordVector.setWordsToKeep(20000);
            stringToWordVector.setTokenizer(wordTokenizer);

            stringToWordVector.setInputFormat(dataset);
            input.trainInstances = Filter.useFilter(dataset, stringToWordVector);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }
}
